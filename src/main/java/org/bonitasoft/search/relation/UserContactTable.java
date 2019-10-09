package org.bonitasoft.search.relation;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.identity.ContactData;
import org.bonitasoft.search.ContactDataSearchDescriptorRelation;
import org.bonitasoft.search.ProcessInstanceSearchDescriptorRelation;

public class UserContactTable extends BaseElementTable {

    public static String PREFIX_TABLE = "USER_CONTACTINFO";
    
    public String getTableName() {
        return PREFIX_TABLE;
    }

    public Class<?> getBaseClass() {
       return ContactData.class;
    }

    @Override
    public List<BaseElementLink> getLinks() {
        List<BaseElementLink> listLinks = new ArrayList<BaseElementLink>();
        
        listLinks.add( BaseElementLink.getInstanceParent(this, ContactDataSearchDescriptorRelation.USERID, new UserTable()));
        
        return listLinks;
    }
}
