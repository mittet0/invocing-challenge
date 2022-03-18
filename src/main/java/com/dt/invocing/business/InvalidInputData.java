package com.dt.invocing.business;

import java.util.List;

public class InvalidInputData extends IllegalArgumentException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<ValidationException> exceptions;

    public InvalidInputData(List<ValidationException> exceptions) {
        super("Ivalid csv content. See nested exceptions for more details.");
        this.setExceptions(exceptions);
    }

    public List<ValidationException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ValidationException> exceptions) {
        this.exceptions = exceptions;
    }

}
