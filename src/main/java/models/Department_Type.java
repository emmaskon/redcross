package models;

import java.io.Serializable;

public class Department_Type implements Serializable {
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    private String name = "";

}
