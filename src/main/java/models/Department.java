
package models;

import java.io.Serializable;

public class Department implements Serializable {
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
     public String getStateId() {
        return state_id;
    }

    public void setStateId(String state_id) {
        this.state_id = state_id;
    }
    
     public String getCityId() {
        return city_id;
    }

    public void setCityId(String city_id) {
        this.city_id = city_id;
    }
    
     public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }        
    
    private String name = "";
    private String type = "";
    private String state_id = "";
    private String city_id = "";
    private String county = "";
}
