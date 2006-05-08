package org.hibernate.netbeans.console.model;

/**
 * @author leon
 */
public class Store {

    private Long id;

    private String name;

    private String name2;

    public Store() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName2() {
        return name2;
    }
    
}
