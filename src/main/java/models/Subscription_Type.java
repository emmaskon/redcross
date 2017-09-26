package models;

import java.io.Serializable;

public class Subscription_Type implements Serializable {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    private String type = "";
    private String cost = "";
}
