package models;

import java.io.Serializable;

public class State implements Serializable {
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
    
    private String id = "";
    private String state_name = "";
}
