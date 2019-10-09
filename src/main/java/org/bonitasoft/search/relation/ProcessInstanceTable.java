package org.bonitasoft.search.relation;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.search.ProcessInstanceSearchDescriptorRelation;

public class ProcessInstanceTable extends BaseElementTable {

    public static String PREFIX_TABLE = "PROCESS_INSTANCE";

    public String getTableName() {
        return PREFIX_TABLE;
    }

    public Class<?> getBaseClass() {
      return ProcessInstance.class;
    }
    
    public List<BaseElementLink> getLinks() {
        List<BaseElementLink> listLinks = new ArrayList<BaseElementLink>();
        // listLinks.add( BaseElementLink.getInstanceComposition(this, ProcessInstanceSearchDescriptor.ASSIGNEE_ID, new UserTable()));
        // listLinks.add( BaseElementLink.getInstanceComposition(this, ProcessInstanceSearchDescriptor.CALLER_ID, new ActivityTable()));
        // GROUP_ID
        listLinks.add( BaseElementLink.getInstanceParent(this, ProcessInstanceSearchDescriptorRelation.PROCESS_DEFINITION_ID, new ProcessDeploymentTable()));
        // PROCESS_DEFINITION_ID
        // ROLE_ID
        listLinks.add( BaseElementLink.getInstanceComposition(this, ProcessInstanceSearchDescriptorRelation.STARTED_BY, new UserTable()));
        listLinks.add( BaseElementLink.getInstanceComposition(this, ProcessInstanceSearchDescriptorRelation.STARTED_BY_SUBSTITUTE, new UserTable()));
        // listLinks.add( BaseElementLink.getInstanceComposition(this, ProcessInstanceSearchDescriptor.USER_ID, new UserTable()));
        
        return listLinks;
    }

}
