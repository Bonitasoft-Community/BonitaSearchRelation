package org.bonitasoft.search.relation;

import org.bonitasoft.search.ProcessInstanceSearchDescriptorRelation;

public class BaseElementLink {
    private  BaseElementTable sourceTable;
    private String sourceAttribut;
    private BaseElementTable destinationTable;
    private String destinationAttribut;
    private String sqlLink;
    private boolean isParentRelation;
    
    /**
     * Link with a table to its parent. 
     * when no relation is given between table, this is the relation to use
     * @param sourceTable
     * @param attribut
     * @param secondTable
     * @return
     */
    public static BaseElementLink getInstanceParent( BaseElementTable sourceTable,  String sourceAttribut, BaseElementTable parentTable)
    {
        BaseElementLink baseElementLink = new BaseElementLink();
        baseElementLink.sourceTable = sourceTable;
        baseElementLink.sourceAttribut = extractAttribut( sourceAttribut );
        baseElementLink.destinationTable = parentTable;
        baseElementLink.destinationAttribut="ID";
        baseElementLink.isParentRelation=true;
        return baseElementLink;
    }
    /**
     * the second table is the father
     * Example: USER_CONTACT_INFO.USERID => USER
     * @param sourceTable
     * @param secondTable
     * @param attribut
     * @return
     */
    public static BaseElementLink getInstanceComposition( BaseElementTable sourceTable, String sourceAttribut, BaseElementTable destinationTable )
    {
        BaseElementLink baseElementLink = new BaseElementLink();
        baseElementLink.sourceTable = sourceTable;
        baseElementLink.destinationTable = destinationTable;
        baseElementLink.sourceAttribut = extractAttribut( sourceAttribut );
        baseElementLink.destinationAttribut="ID";
        baseElementLink.sqlLink= sourceTable.getTableName()+sourceAttribut+" = "+destinationTable.getTableName()+baseElementLink.destinationAttribut;
        baseElementLink.isParentRelation=false;
        return baseElementLink;
    }

    
    /**
     * is this relation is between this two tables ? 
     * @param firstTable
     * @param secondTable
     * @return
     */
    public boolean isRelationBetween( BaseElementTable firstTable, BaseElementTable secondTable )
    {
        if (sourceTable.equals( firstTable ) && destinationTable.equals( secondTable ))
           return true;
        if (sourceTable.equals( secondTable ) && destinationTable.equals( firstTable ))
            return true;
        return false;
    }
    
    public boolean isRelationForTableName( String tableName )
    {
        if (sourceTable.getTableName().equals(tableName) || destinationTable.getTableName().equals( tableName ))
            return true;
        return false;
    }
    
    /**
     * 
     * @return
     */
    public String getSqlFilter()
    {
        return sourceTable.getTableName()+"."+sourceAttribut+" = "+destinationTable.getTableName()+"."+destinationAttribut;
    }

    
    public boolean isParentRelation() {
        return isParentRelation;
    }
    
    public void setParentRelation(boolean isParentRelation) {
        this.isParentRelation = isParentRelation;
    }
    
    public BaseElementTable getSourceTable() {
        return sourceTable;
    }
    
    public String getSourceAttribut() {
        return sourceAttribut;
    }
    
    public BaseElementTable getDestinationTable() {
        return destinationTable;
    }
    
    public String getDestinationAttribut() {
        return destinationAttribut;
    }
    
    public String getSqlLink() {
        return sqlLink;
    }
    
    /**
     * To debug
     */
    public String toString() {
        return getSqlFilter();
    }
    
    /**
     * 
     * fieldname is PROCESS_INSTANCE.STARTED_BY, return STARTED_BY
     */
    public static String extractAttribut(String fieldName )
    {
        if (fieldName.indexOf(".")==-1)
            return fieldName;
        return fieldName.substring(fieldName.indexOf(".")+1);
    }
}
