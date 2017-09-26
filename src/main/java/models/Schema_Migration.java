package models;

import java.io.Serializable;

public class Schema_Migration implements Serializable {
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }    
    
    private String version = "";
}
