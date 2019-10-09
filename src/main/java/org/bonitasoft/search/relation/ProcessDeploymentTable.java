package org.bonitasoft.search.relation;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;


public class ProcessDeploymentTable extends BaseElementTable {

    public static String PREFIX_TABLE = "PROCESS_DEFINITION";

    public String getTableName() {
        return PREFIX_TABLE;
    }

    public Class<?> getBaseClass() {
      return ProcessDeploymentInfo.class;
    }
    
    public List<BaseElementLink> getLinks() {
        List<BaseElementLink> listLinks = new ArrayList<BaseElementLink>();
        return listLinks;
    }
}
