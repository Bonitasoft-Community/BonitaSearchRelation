package org.bonitasoft.search;

import org.bonitasoft.search.relation.UserContactTable;
import org.bonitasoft.search.relation.UserTable;

public class ContactDataSearchDescriptorRelation {
    /**
     * Id
     */
     static String ID = UserContactTable.PREFIX_TABLE+".ID";
     /**
      * User Id attached to this contact data
      */
      public static String USERID = UserContactTable.PREFIX_TABLE+".USERID";
      public static String EMAIL = UserContactTable.PREFIX_TABLE+".EMAIL";
      public static String PHONE = UserContactTable.PREFIX_TABLE+".PHONE";
      public static String MOBILE = UserContactTable.PREFIX_TABLE+".MOBILE";
      public static String FAX = UserContactTable.PREFIX_TABLE+".FAX";
      public static String BUILDING = UserContactTable.PREFIX_TABLE+".BUILDING";
      public static String ROOM = UserContactTable.PREFIX_TABLE+".ROOM";
      public static String ADDRESS = UserContactTable.PREFIX_TABLE+".ADDRESS";
      public static String ZIPCODE = UserContactTable.PREFIX_TABLE+".ZIPCODE";
      public static String CITY = UserContactTable.PREFIX_TABLE+".CITY";
      public static String STATE = UserContactTable.PREFIX_TABLE+".STATE";
      public static String COUNTRY = UserContactTable.PREFIX_TABLE+".COUNTRY";
      public static String WEBSITE = UserContactTable.PREFIX_TABLE+".WEBSITE";
      public static String PERSONAL = UserContactTable.PREFIX_TABLE+".PERSONAL";


}
