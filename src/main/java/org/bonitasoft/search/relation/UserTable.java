package org.bonitasoft.search.relation;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.identity.User;

public class UserTable extends BaseElementTable {

    public static String TABLE_NAME = "USER_";
    
    public String getTableName() {
        return TABLE_NAME;
    }

    public Class<?> getBaseClass() {
       return User.class;
    }

    @Override
    public List<BaseElementLink> getLinks() {
        List<BaseElementLink> listLinks = new ArrayList<BaseElementLink>();
        return listLinks;
    }

}
