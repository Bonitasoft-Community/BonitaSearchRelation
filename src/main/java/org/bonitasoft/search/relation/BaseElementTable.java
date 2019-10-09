package org.bonitasoft.search.relation;

import java.util.List;

public abstract class BaseElementTable {
    
    
   
    public abstract String getTableName();
    
    public abstract Class<?> getBaseClass();

    /**
     * return the list of Link
     * THe SourceItem is supposed to be THIS BaseElementTable, so getSourceTable() == this, getSourceAttribut() == attribut in this table
     * @return
     */
    public abstract List<BaseElementLink> getLinks();
    
    public String toString()
    { return getTableName(); };
  
    /**
     * redefine the equals method to have a simple code
     */
    @Override
    public boolean equals(Object o) { 
        if (o == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof BaseElementTable)) { 
            return false; 
        } 
          
        // typecast o to Complex so that we can compare data members  
        BaseElementTable c = (BaseElementTable) o; 
        return this.getTableName().equals(c.getTableName());
    }
    /**
     * return the link used by the attributName
     * @param attributName
     * @param onlyParent
     * @return
     */
    public BaseElementLink getLinkedByField( String fieldName, boolean onlyParent )
    {
        List<BaseElementLink> listLinks = getLinks();
        String attributName=BaseElementLink.extractAttribut( fieldName );
        
        for (BaseElementLink link : listLinks)
        {
            if (onlyParent && ! link.isParentRelation())
                continue;
            if (link.getSourceAttribut().equals( attributName ))
                return link;
        }
        return null;
    }
    
}
