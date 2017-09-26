package models;

import java.io.Serializable;

public class Annual_Scheduler implements Serializable {

    public String getYear() { 
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String year = "";
}
