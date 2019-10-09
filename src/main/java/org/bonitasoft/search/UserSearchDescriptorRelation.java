package org.bonitasoft.search;

import org.bonitasoft.search.relation.UserTable;

public class UserSearchDescriptorRelation {
    
 public static String SEARCH_ID = org.bonitasoft.engine.identity.UserSearchDescriptor.ID;    
 /**
 * filter search on User's activation
 */
 public static String ENABLED = UserTable.TABLE_NAME+".ENABLED";
 /**
 * filter search on User's firstname
 */
 public static String FIRST_NAME= UserTable.TABLE_NAME+".FIRSTNAME";
 /**
 * filter search on User's group id
 */
 // static String GROUP_ID= UserTable.TABLE_NAME+".GROUPID";
 /**
 * filter search on User's id
 */
 public static String ID= UserTable.TABLE_NAME+".ID";
 /**
 * filter search on User's last connection date
 */
 // static String LAST_CONNECTION= UserTable.TABLE_NAME+".LAST_CONNECTION";
 /**
 * filter search on User's lastname
 */
 public static String LAST_NAME= UserTable.TABLE_NAME+".LASTNAME";
 /**
 * filter search on the User's manager user id
 */
 public static String MANAGER_USER_ID= UserTable.TABLE_NAME+".MANAGERUSERID";
 /**
 * filter search on User's role id
 */
 // static String ROLE_ID= UserTable.TABLE_NAME+".ROLE_ID";
 /**
 * filter search on Us
 */
 public static String USER_NAME= UserTable.TABLE_NAME+".USERNAME";
}
