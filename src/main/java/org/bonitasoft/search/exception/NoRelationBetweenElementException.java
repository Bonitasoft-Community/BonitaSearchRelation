package org.bonitasoft.search.exception;


public class NoRelationBetweenElementException  extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String sourceTable;
    private String destinationTable;
    
    public NoRelationBetweenElementException( String sourceTable, String destinationTable)
    {
        this.sourceTable = sourceTable;
        this.destinationTable = destinationTable;
    }
}
