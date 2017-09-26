package models;

import java.io.Serializable;

public class Payment_Type implements Serializable {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type = "";
}
