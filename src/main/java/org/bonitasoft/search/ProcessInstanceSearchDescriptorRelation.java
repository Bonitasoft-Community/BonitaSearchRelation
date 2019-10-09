package org.bonitasoft.search;

import org.bonitasoft.search.relation.ProcessInstanceTable;
import org.bonitasoft.search.relation.UserTable;

public class ProcessInstanceSearchDescriptorRelation {
   public static String SEARCH_ID = org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor.ID;    

    /**
     * The field corresponding to the date of the archiving of the process instance.
     */
    // public static String   ARCHIVE_DATE = ProcessInstanceTable.PREFIX_TABLE+".ARCHIVE_DATE";
    /**
     * The field corresponding to the identifier of the user assignee to a user task of the process instance.
     */
    // public static String   ASSIGNEE_ID = ProcessInstanceTable.PREFIX_TABLE+".ASSIGNEE_ID";
    /**
     * The field corresponding to the identifier of the flow node that starts the process instance.
     */
    public static String   CALLER_ID = ProcessInstanceTable.PREFIX_TABLE+".CALLERID";
    /**
     * The field corresponding to the date of the end of the process instance.
     */
    public static String   END_DATE = ProcessInstanceTable.PREFIX_TABLE+".ENDDATE";
    /**
     * The field corresponding to the identifier of the group who supervised the process instance.
     */
    // public static String   GROUP_ID = ProcessInstanceTable.PREFIX_TABLE+".GROUP_ID";
    /**
     * The field corresponding to the identifier of the archived process instance.
     */
    public static String   ID = ProcessInstanceTable.PREFIX_TABLE+".ID";
    /**
     * The field corresponding to the last date of the updating of the process instance.
     */
    // public static String   LAST_UPDATE = ProcessInstanceTable.PREFIX_TABLE+".LAST_UPDATE";
    /**
     * The field corresponding to the name of the process.
     */
    public static String   NAME = ProcessInstanceTable.PREFIX_TABLE+".NAME";
    /**
     * The field corresponding to the identifier of the process definition.
     */
    public static String   PROCESS_DEFINITION_ID = ProcessInstanceTable.PREFIX_TABLE+".PROCESSDEFINITIONID";
    /**
     * The field corresponding to the identifier of the role who supervised the process instance.
     */
    // public static String   ROLE_ID = ProcessInstanceTable.PREFIX_TABLE+".ROLE_ID";
    /**
     * The field corresponding to the identifier of the running process instance.
     */
    // public static String   SOURCE_OBJECT_ID = ProcessInstanceTable.PREFIX_TABLE+".SOURCE_OBJECT_ID";
    /**
     * The field corresponding to the date when the process instance is started.
     */
    public static String   START_DATE = ProcessInstanceTable.PREFIX_TABLE+".STARTDATE";
    /**
     * The field corresponding to the identifier of the user who started the process instance.
     */
    public static String   STARTED_BY = ProcessInstanceTable.PREFIX_TABLE+".STARTEDBY";
    /**
     * The field corresponding to the identifier of the user who started the process instance for the user in ProcessInstance.getStartedBy().
     */
    public static String   STARTED_BY_SUBSTITUTE = ProcessInstanceTable.PREFIX_TABLE+".STARTEDBYSUBSTITUTE";
    /**
     * The field corresponding to the identifier of the state of the archived process instance.
     */
    public static String   STATE_ID = ProcessInstanceTable.PREFIX_TABLE+".STATEID";
    /**
     * The field corresponding to the identifier of the user who supervised the process instance.
     */
    // public static String   USER_ID = ProcessInstanceTable.PREFIX_TABLE+".USER_ID";
    public static String   STRING_INDEX_1 = ProcessInstanceTable.PREFIX_TABLE+".STRINGINDEX1";
    public static String   STRING_INDEX_2 = ProcessInstanceTable.PREFIX_TABLE+".STRINGINDEX2";
    public static String   STRING_INDEX_3 = ProcessInstanceTable.PREFIX_TABLE+".STRINGINDEX3";
    public static String   STRING_INDEX_4 = ProcessInstanceTable.PREFIX_TABLE+".STRINGINDEX4";
    public static String   STRING_INDEX_5 = ProcessInstanceTable.PREFIX_TABLE+".STRINGINDEX5";

}
