package org.bonitasoft.search;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.BaseElement;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.SearchFilterOperation;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.Sort;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.search.exception.NoRelationBetweenElementException;
import org.bonitasoft.search.exception.TableNotSupportedException;
import org.bonitasoft.search.options.SearchOptionsRelationImpl;
import org.bonitasoft.search.relation.BaseElementLink;
import org.bonitasoft.search.relation.BaseElementPath;
import org.bonitasoft.search.relation.BaseElementTable;
import org.bonitasoft.search.relation.FactoryTable;
import org.bonitasoft.search.relation.FactoryTable.SearchMethodInfo;
import org.bonitasoft.engine.search.impl.SearchFilter;

public class SearchRelationAPI {

            
    public Logger logger = Logger.getLogger(SearchRelationAPI.class.getName());
    public static String logHeader = SearchRelationAPI.class.getName();
    
    APISession apiSession;
    public SearchRelationAPI(APISession apiSession ) {
        this.apiSession = apiSession;
    }
    
    @SuppressWarnings("unchecked")
    public SearchResult<BaseElement> search(Class typeElement, SearchOptions searchOptions) throws TableNotSupportedException, NoRelationBetweenElementException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException
    {
        List<BaseElementTable> setTables= new ArrayList<BaseElementTable>();
        List<BaseElementLink> setLinks= new ArrayList<BaseElementLink>();
        List<BaseElementLink> onlyAdditionalLinks= new ArrayList<BaseElementLink>();
        List<SearchFilter> keepFilterOnAttribut= new ArrayList<SearchFilter>();
        
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI( apiSession );
        
        // add in the result the typeElement
        FactoryTable factoryTable = FactoryTable.getInstance();
        BaseElementTable referenceTable = factoryTable.getTableFromClass(typeElement);
        setTables.add( referenceTable );
        // first pass, identify all tables involved in the request
        for (SearchFilter searchFilter : searchOptions.getFilters())
        {
            if (searchFilter.getField()==null)
                continue;
            BaseElementTable baseElementTable = factoryTable.getTableFromField( searchFilter.getField());
            // this element can already exist if there are multiple filter on the same table
            if ( ! setTables.contains( baseElementTable ))
                setTables.add( baseElementTable );    
            // second, search all semi-implicite relation.
            // Example, user give a filter
            //   sob.filter(ProcessInstanceSearchDescriptor.STARTED_BY, null);
            // that's means he wants to rely the PROCESSINSTANCE and the USER table bu the startedBy
            if (searchFilter.getValue().equals(SearchOptionsRelationImpl.MARKER_FOR_RELATION_BETWEEN_TABLE))
            {
                BaseElementLink baseElementLink = baseElementTable.getLinkedByField( searchFilter.getField(), false );
                if (baseElementLink==null)
                {
                    throw new NoRelationBetweenElementException(baseElementTable.getTableName(), searchFilter.getField() );
                }
                if ( ! setTables.contains( baseElementLink.getDestinationTable() ))
                    setTables.add( baseElementLink.getDestinationTable());
                if ( ! setLinks.contains( baseElementLink ))
                    setLinks.add( baseElementLink);
            }
            else
                keepFilterOnAttribut.add( searchFilter );
        }
        
        // Now, get and complete any link between table. Link may be already done by the filter
        int maxTables= setTables.size();
        
        
        // we will add some tables in the list to perform the relation. We don't need to study the new tables
        // so, let's explore from the first tables
        List<String> exploreTable= new ArrayList<String>();
        exploreTable.add( setTables.get(0).getTableName());
        int pointer=0;
        
        while( pointer < exploreTable.size())
        {
            String tableNameExplore = exploreTable.get( pointer );
            for (BaseElementLink link : setLinks)
            {
                if (link.isRelationForTableName( tableNameExplore ))
                {
                    if (! exploreTable.contains( link.getSourceTable().getTableName() ))
                        exploreTable.add( link.getSourceTable().getTableName() );
                    if (! exploreTable.contains( link.getDestinationTable().getTableName() ))
                        exploreTable.add( link.getDestinationTable().getTableName() );
                }
            }
            pointer++;
        }
        if (setTables.size() != exploreTable.size())
        {
            // we miss some tables, so find now the missing one
            for (int i=1; i<maxTables;i++)
            {
                // is a relation exist between the first table and this one?
                BaseElementTable studyTable = setTables.get( i );
                boolean foundLink=false;
                for (BaseElementLink link : setLinks)
                {
                    if (link.isRelationBetween( referenceTable, studyTable))
                    {
                        onlyAdditionalLinks.add( link );
                        foundLink=true;
                    }
                }
                if (foundLink)
                    continue;
                BaseElementPath path = factoryTable.getPath( referenceTable, studyTable, 0, new HashSet<String>());
                if (path==null)
                    throw new NoRelationBetweenElementException( referenceTable.getTableName(), studyTable.getTableName());
                // complete all tables
                setTables.addAll( path.getTables() );
                setLinks.addAll( path.getLinks() );
                onlyAdditionalLinks.addAll( path.getLinks() );
            }
        }
        // ok, now we have all information to build the request
        StringBuffer sqlRequest = new StringBuffer();
        List<Object> sqlParameters = new ArrayList<Object>();
        sqlRequest.append( "select "+referenceTable.getTableName()+".ID from ");
        for (int i=0;i<setTables.size();i++)
        {
            if (i>0)
                sqlRequest.append( ", ");
            sqlRequest.append(setTables.get( i ).getTableName());
        }
        sqlRequest.append( " where (");
        SearchFilter previousFilter=null;
        for (SearchFilter searchFilter :keepFilterOnAttribut)
        {
            if (previousFilter!=null && ! ( previousFilter.getOperation()== SearchFilterOperation.AND || previousFilter.getOperation()== SearchFilterOperation.OR))
                sqlRequest.append( " AND ");
                    
            previousFilter = searchFilter;
            sqlRequest.append( getSqlFilter( searchFilter));
            if (searchFilter.getValue() !=null)
                sqlParameters.add( searchFilter.getValue() );
        }
        sqlRequest.append(" ) ");
        // add all the particular link
        for ( BaseElementLink baseElementLink : onlyAdditionalLinks)
        {
            sqlRequest.append( " and "+baseElementLink.getSqlFilter() );
        }
        
      
        for (int i=0;i<searchOptions.getSorts().size();i++ )
        {
            Sort sort = searchOptions.getSorts().get( i );
            if (i==0)
                sqlRequest.append(" order by ");
            else
                sqlRequest.append(",");
            sqlRequest.append(sort.getField());
        }
        
        // execute the request now
        
        List<BaseElement> listBaseElement = executeBaseElement( typeElement, sqlRequest.toString(), sqlParameters, processAPI, factoryTable);
        
        
        return new SearchResultImpl(listBaseElement.size(), listBaseElement);
    }
    
    
    /**
     * load all baseElements
     * @param typeElement
     * @param sqlRequest
     * @param sqlParameters
     * @return
     * @throws TableNotSupportedException 
     */
    private List<BaseElement> executeBaseElement(Class typeElement, String sqlRequest, List<Object> sqlParameters, ProcessAPI processAPI, FactoryTable factoryTable) throws TableNotSupportedException
    {
        // two step query: run the SQL to collect the ID
        List<Long> listId= executeSqlRequest( sqlRequest, sqlParameters);
        // then, now build the list of object, via the SearchAPI...
        List<BaseElement> listBaseElement = new ArrayList<BaseElement>();
        int pageIndex=0;
        int nbElementPerPage = 100;
        String searchAttributId = factoryTable.getSearchAttributId( typeElement );
        while (pageIndex< listId.size())
        {
            SearchOptionsBuilder sob = new SearchOptionsBuilder(0,nbElementPerPage);
            for (int i=0; i<nbElementPerPage;i++)
            {
                if (pageIndex + i>= listId.size())
                    break;
                if (i>0)
                    sob.or();
                sob.filter( searchAttributId, listId.get(pageIndex+i));
            }
            
            try {
                SearchResult sr =  factoryTable.executeSearchMethod( typeElement, sob, apiSession );
                listBaseElement.addAll( sr.getResult());
            } catch (Exception  e) {
                logger.severe(logHeader+".executeBaseElement : " + e.toString());
            }
            pageIndex+= nbElementPerPage;
        }
        return listBaseElement;
    }
    
    private List<Long> executeSqlRequest(String sqlRequest, List<Object> sqlParameters)
    {
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Long> listIds = new ArrayList<Long>();
        try {
            
            // search all process instance like with the root
            con = getConnection();
            pstmt = con.prepareStatement(sqlRequest);
            
            for (int i=0; i< sqlParameters.size();i++) 
            {
                pstmt.setObject(i+1, sqlParameters.get( i ));
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final HashMap<String, Object> mapContract = new HashMap<String, Object>();
                listIds.add( rs.getLong("ID"));
               
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

        } catch (Exception e) {
            final StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.severe(logHeader+".executeSqlRequest : " + e.toString() + " at " + sw.toString());

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (final SQLException localSQLException) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                    pstmt = null;
                } catch (final SQLException localSQLException) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                    con = null;
                } catch (final SQLException localSQLException1) {
                }
            }
        }
        return listIds;
    }
    
    /**
     * this method should exist somewhere
     * @param searchFilter
     * @return
     */
    private String getSqlFilter( SearchFilter searchFilter) {
        if ( SearchFilterOperation.OR.equals( searchFilter.getOperation() )
                || SearchFilterOperation.AND.equals( searchFilter.getOperation() )
                || SearchFilterOperation.L_PARENTHESIS.equals( searchFilter.getOperation())
                || SearchFilterOperation.R_PARENTHESIS.equals( searchFilter.getOperation()) )                
            return getSqlOperation(  searchFilter.getOperation() );

        return searchFilter.getField()+" "+getSqlOperation( searchFilter.getOperation()) +" ?";
    }
    
    private String getSqlOperation( SearchFilterOperation operation)
    {
       if (SearchFilterOperation.EQUALS.equals(operation))
           return "=";
       if (SearchFilterOperation.GREATER_THAN.equals( operation ))
               return ">"; 
       if (SearchFilterOperation.LESS_THAN.equals( operation ))
           return "<"; 
               if (SearchFilterOperation.GREATER_OR_EQUAL.equals( operation ))
                   return ">=";  
                       if (SearchFilterOperation.LESS_OR_EQUAL.equals( operation ))
                           return "<=";  
                               if (SearchFilterOperation.DIFFERENT.equals( operation ))
                                   return "<>";  
                                       if (SearchFilterOperation.BETWEEN.equals( operation ))
                                           return " in ";  
                                               if (SearchFilterOperation.OR.equals( operation ))
                                                   return "or";  
                                                       if (SearchFilterOperation.AND.equals( operation ))
                                                           return "and";  
                                                               if (SearchFilterOperation.L_PARENTHESIS.equals( operation ))
                                                                   return "(";  
                                                                       if (SearchFilterOperation.R_PARENTHESIS.equals( operation ))
                                                                           return ")"; 
            

        return "";
    }
    /* -------------------------------------------------------------------- */
    /*                                                                      */
    /* getConnection */
    /*                                                                      */
    /* -------------------------------------------------------------------- */
    private static List<String> listDataSources = Arrays.asList("java:/comp/env/bonitaSequenceManagerDS",
            "java:jboss/datasources/bonitaSequenceManagerDS");

    /**
     * getConnection
     * 
     * @return
     * @throws NamingException
     * @throws SQLException
     */

    public Connection getConnection() throws SQLException {
        // logger.info(loggerLabel+".getDataSourceConnection() start");

        String msg = "";
        List<String> listDatasourceToCheck = new ArrayList<String>();
        for (String dataSourceString : listDataSources)
            listDatasourceToCheck.add(dataSourceString);

        for (String dataSourceString : listDatasourceToCheck) {
            logger.info(logHeader + ".getDataSourceConnection() check[" + dataSourceString + "]");
            try {
                final Context ctx = new InitialContext();
                final DataSource dataSource = (DataSource) ctx.lookup(dataSourceString);
                logger.info(logHeader + ".getDataSourceConnection() [" + dataSourceString + "] isOk");
                return dataSource.getConnection();

            } catch (NamingException e) {
                logger.info(
                        logHeader + ".getDataSourceConnection() error[" + dataSourceString + "] : " + e.toString());
                msg += "DataSource[" + dataSourceString + "] : error " + e.toString() + ";";
            }
        }
        logger.severe(logHeader + ".getDataSourceConnection: Can't found a datasource : " + msg);
        return null;
    }

}
