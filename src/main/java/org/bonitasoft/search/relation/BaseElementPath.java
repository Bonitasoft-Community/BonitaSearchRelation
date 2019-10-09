package org.bonitasoft.search.relation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * reference a path between two tables.
 * 
 * @author Firstname Lastname
 */
public class BaseElementPath {

    public List<BaseElementLink> listLinks = new ArrayList<BaseElementLink>();

    public BaseElementPath(BaseElementLink link) {
        listLinks.add(link);
    }

    public void addInFront(BaseElementLink link) {
        listLinks.add(0, link);
    }

    public Set<BaseElementTable> getTables() {
        Set<BaseElementTable> listTables = new HashSet<BaseElementTable>();
        for (BaseElementLink link : listLinks) {
            listTables.add(link.getSourceTable());
            listTables.add(link.getDestinationTable());
        }
        return listTables;
    }

    public List<BaseElementLink> getLinks() {
        return listLinks;
    }

    public int size() {
        return listLinks.size();
    }
    public String toString() {
        String path="Path ("+listLinks.size()+") :";
        for (BaseElementLink link: listLinks)
            path+=" / "+link.getSourceTable()+"~"+link.getDestinationTable()+" ";
        return path;
        
    }
}
