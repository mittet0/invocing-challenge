package com.dt.invocing.api;

public class CustomerDTO {
    private Long vat;
    private String name;
    /**
     * @param vat
     * @param name
     * @param number
     */
    public CustomerDTO(Long vat, String name) {
        super();
        this.vat = vat;
        this.name = name;
    }
    public Long getVat() {
        return vat;
    }
    public void setVat(Long vat) {
        this.vat = vat;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
