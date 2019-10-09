package org.bonitasoft.search;

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.search.relation.ProcessDeploymentTable;

public class ProcessDeploymentInfoSearchDescriptorRelation {
    
    public static String SEARCH_ID = org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor.ID;    

    /**
     * The field corresponding to the activation state of the process.
     */
    public static String   ACTIVATION_STATE = ProcessDeploymentTable.PREFIX_TABLE+".ACTIVATION_STATE";
    /**
     * The field corresponding to the identifier of the category of the process.
     */
    public static String   CATEGORY_ID= ProcessDeploymentTable.PREFIX_TABLE+".CATEGORY_ID";
    /**
     * The field corresponding to the configuration state of the process.
     */
    public static String   CONFIGURATION_STATE= ProcessDeploymentTable.PREFIX_TABLE+".CONFIGURATION_STATE";
    /**
     * The field corresponding to the identifier of the user who deployed the process.
     */
    public static String   DEPLOYED_BY= ProcessDeploymentTable.PREFIX_TABLE+".DEPLOYED_BY";
    /**
     * The field corresponding to the date of the deployment of the process.
     */
    public static String   DEPLOYMENT_DATE= ProcessDeploymentTable.PREFIX_TABLE+".DEPLOYMENT_DATE";
    /**
     * The field corresponding to the display name of the process.
     */
    public static String   DISPLAY_NAME= ProcessDeploymentTable.PREFIX_TABLE+".DISPLAY_NAME";
    /**
     * The field corresponding to the identifier of the process in the database.
     */
    public static String   ID= ProcessDeploymentTable.PREFIX_TABLE+".";
    
    /**
     * The field corresponding to the last date of the updating of the process.
     */
    public static String   LAST_UPDATE_DATE= ProcessDeploymentTable.PREFIX_TABLE+".LAST_UPDATE_DATE";
    /*
     * The field corresponding to the name of the process.
     */
    public static String   NAME= ProcessDeploymentTable.PREFIX_TABLE+".NAME";
    /**
     * The field corresponding to the identifier of the process definition (in the bonita home).
     */
    public static String   PROCESS_ID= ProcessDeploymentTable.PREFIX_TABLE+".PROCESS_ID";
    /**
     * The field corresponding to the version of the process.
     */
    public static String   VERSION= ProcessDeploymentTable.PREFIX_TABLE+".VERSION";
}
