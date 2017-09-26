package models;

import java.io.Serializable;

public class County implements Serializable {
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    private String name = "";
    private String state = "";
}
