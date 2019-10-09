package org.bonitasoft.search.relation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.search.relation.BaseElementPath;
import org.bonitasoft.search.ProcessDeploymentInfoSearchDescriptorRelation;
import org.bonitasoft.search.ProcessInstanceSearchDescriptorRelation;
import org.bonitasoft.search.SearchRelationAPI;
import org.bonitasoft.search.UserSearchDescriptorRelation;
import org.bonitasoft.search.exception.TableNotSupportedException;

public class FactoryTable {

    public Logger logger = Logger.getLogger(FactoryTable.class.getName());

    
    public static FactoryTable getInstance()
    {
        return new FactoryTable();
    }
    
    public static class BaseElementReference
    {
        public BaseElementTable elementTable;
        public String apiName;
        public String apiSearchMethod;
        public String searchAttributId;
        public BaseElementReference( BaseElementTable elementTable, String searchAttributId, String apiName, String apiSearchMethod)
        {
            this.elementTable = elementTable;
            this.apiName = apiName;
            this.apiSearchMethod = apiSearchMethod;
            this.searchAttributId = searchAttributId;
        }
    }

    public static BaseElementReference[] listBaseElement = { new BaseElementReference( new UserTable(), UserSearchDescriptorRelation.SEARCH_ID, "IdentityAPI", "searchUsers"), 
            new BaseElementReference( new ProcessInstanceTable(), ProcessInstanceSearchDescriptorRelation.SEARCH_ID, "ProcessAPI", "searchProcessInstances"), 
            new BaseElementReference( new ProcessDeploymentTable(), ProcessDeploymentInfoSearchDescriptorRelation.SEARCH_ID, "ProcessAPI", "searchProcessDeploymentInfos"), 
            new BaseElementReference( new UserContactTable(), "", "IdentityAPI", "") // search does not exist 
   };
    
   
    
    public BaseElementTable getTableFromClass( Class typeElement) throws TableNotSupportedException
    {
        for( BaseElementReference baseElement : listBaseElement) {
            if (typeElement.equals(baseElement.elementTable.getBaseClass()))
                return baseElement.elementTable;
        }
        throw new TableNotSupportedException(typeElement);
    }
    
    public BaseElementTable getTableFromField( String fieldName) throws TableNotSupportedException
    {
        if (fieldName.indexOf(".")==-1)
            throw new TableNotSupportedException(fieldName);
        String tableName = fieldName.substring(0,fieldName.indexOf("."));
        for( BaseElementReference baseElement : listBaseElement) {
            if (tableName.equals(baseElement.elementTable.getTableName()))
                return baseElement.elementTable;
        }
        throw new TableNotSupportedException(fieldName);
    }
    
    public String getSearchAttributId( Class typeElement) throws TableNotSupportedException
    {
        for( BaseElementReference baseElement : listBaseElement) {
            if (typeElement.equals(baseElement.elementTable.getBaseClass()))
                return baseElement.searchAttributId;
        }
        throw new TableNotSupportedException(typeElement);
    }
    
    /**
     * 
     * @param typeElement
     * @param apiSession
     * @return
     */
    public static class SearchMethodInfo {
        public Method searchMethod;
        public Object callerObject;
    }
    public SearchMethodInfo getSearchMethod( Class typeElement, APISession apiSession )
    {
        SearchMethodInfo searchMethod = new SearchMethodInfo();
        for( BaseElementReference baseElement : listBaseElement) {
            if (typeElement.equals(baseElement.elementTable.getBaseClass()))
            {
                Class<?> c;
                try {
                    if ("ProcessAPI".equals( baseElement.apiName))
                        searchMethod.callerObject = TenantAPIAccessor.getProcessAPI(apiSession);
                    if ("IdentityAPI".equals( baseElement.apiName))
                        searchMethod.callerObject = TenantAPIAccessor.getIdentityAPI(apiSession);
                    if ("ProfileAPI".equals( baseElement.apiName))
                        searchMethod.callerObject = TenantAPIAccessor.getProcessAPI(apiSession);
                    
                    Method[] allMethods = searchMethod.callerObject.getClass().getDeclaredMethods();
                    for (Method m : allMethods) {
                        if (m.getName().equals(baseElement.apiSearchMethod))
                        {
                            searchMethod.searchMethod = m;
                            return searchMethod;
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.severe("Can't access one class["+baseElement.apiName+"]");

                }
                
            }
        }
        return null;
    }
    
    public SearchResult executeSearchMethod( Class typeElement, SearchOptionsBuilder sob, APISession apiSession )
    {
        SearchMethodInfo searchMethod = new SearchMethodInfo();
        for( BaseElementReference baseElement : listBaseElement) {
            if (typeElement.equals(baseElement.elementTable.getBaseClass()))
            {
                Class<?> c;
                try {
                    if ("ProcessAPI".equals( baseElement.apiName))
                    {
                        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(apiSession);
                        if ("searchProcessInstances".equals(baseElement.apiSearchMethod))
                            return processAPI.searchProcessInstances( sob.done() );
                        if ("searchProcessDeploymentInfos".equals(baseElement.apiSearchMethod)) 
                                return processAPI.searchProcessDeploymentInfos( sob.done() );
                    }
                    if ("IdentityAPI".equals( baseElement.apiName))
                    {
                        IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(apiSession);
                        
                        
                        if ("searchUsers".equals(baseElement.apiSearchMethod))
                            return identityAPI.searchUsers( sob.done() );
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.severe("Can't access one class["+baseElement.apiName+"] :"+e.getMessage());
                }
            }
        }
        return null;
    }
    /**
     * look for a path between the two different tables
     *  - multiple path may exist, the shorter is returned
     *  - path are using only the Parent relation.
     * @param sourceTable
     * @param destinationTable
     * @return the path, null if no path are found
     */
    public BaseElementPath getPath( BaseElementTable sourceTable, BaseElementTable destinationTable, int depth, Set<String> alreadyExplored)
    {
        // so, if we come back to this link, we can stop. Nota: doing that, we may not find the faster root, but the Bonita datamodel does not have cycle
        alreadyExplored.add( sourceTable.getTableName() );
        String debugGetpath="Search between ["+sourceTable.getTableName()+"] to ["+destinationTable.getTableName()+"] depth="+depth;
        // should be a loop somewhere ? 
        if (depth>5)
            return null;
        
        List<BaseElementPath> listExploration = new ArrayList<BaseElementPath>();
        // explore all parents relation
        for (BaseElementLink link : sourceTable.getLinks())
        {
            if (! link.isParentRelation())
                continue;
            // got it one directly ! No need to explore, we get one
            if (link.getDestinationTable().equals(destinationTable))
            {
                debugGetpath+="Found one relation";
                BaseElementPath baseElementPath = new BaseElementPath( link );
                return baseElementPath;
            }
            if ( ! alreadyExplored.contains( link.getDestinationTable().getTableName() ))
            {
                // explore
                BaseElementPath baseElementPath = getPath(link.getDestinationTable(), destinationTable, depth+1, alreadyExplored);
                if (baseElementPath!=null)
                {
                    debugGetpath+="Found one path "+baseElementPath.toString();
                    baseElementPath.addInFront( link );
                    listExploration.add( baseElementPath);
                }
            }
        }
        // explore all "I'm the parent of " relation
        debugGetpath+="Explore I'm the parent of ";
        for (BaseElementReference otherBaseElement : listBaseElement )
        {
            if (otherBaseElement.equals( sourceTable ))
                continue;
            for (BaseElementLink link : otherBaseElement.elementTable.getLinks())
            {
                if (! link.isParentRelation())
                    continue;
                // got it one directly ! No need to explore, we get one
                if (link.getDestinationTable().equals(destinationTable))
                {
                    debugGetpath+="Found one relation";
                    BaseElementPath baseElementPath = new BaseElementPath( link );
                    return baseElementPath;
                }
                // explore
                if ( ! alreadyExplored.contains( otherBaseElement.elementTable.getTableName() ))
                {
                    BaseElementPath baseElementPath = getPath( otherBaseElement.elementTable, destinationTable,depth+1,alreadyExplored);
                    if (baseElementPath!=null)
                    {
                        debugGetpath+="Found one path "+baseElementPath.toString();
                        baseElementPath.addInFront( link );
                        listExploration.add( baseElementPath);
                    }
                }
            }
        }
        
        
        // result
        // no path found
        if (listExploration.size()==0)
            return null;
        // return the sorter one
        BaseElementPath shorterPath = listExploration.get( 0 );
        for (BaseElementPath path  : listExploration)
            if (path.size() < shorterPath.size())
                shorterPath = path;
        
        debugGetpath+="Shorter path "+shorterPath.toString();

        return shorterPath;
    }
    
}
