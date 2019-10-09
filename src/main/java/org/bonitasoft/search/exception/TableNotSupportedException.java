package org.bonitasoft.search.exception;


public class TableNotSupportedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Class typeElement;
    private String fieldName;
    
    public TableNotSupportedException( Class typeElement)
    {
        this.typeElement = typeElement;
    }
    public TableNotSupportedException( String fieldName)
    {
        this.fieldName = fieldName;
    }
    
}
