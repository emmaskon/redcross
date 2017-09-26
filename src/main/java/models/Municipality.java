package models;

import java.io.Serializable;

public class Municipality implements Serializable {
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    private String name = "";
}
