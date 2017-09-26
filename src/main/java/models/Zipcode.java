package models;

import java.io.Serializable;

public class Zipcode implements Serializable {

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getFrom_num() {
        return from_num;
    }

    public void setFrom_num(String from_num) {
        this.from_num = from_num;
    }

    public String getTo_num() {
        return to_num;
    }

    public void setTo_num(String to_num) {
        this.to_num = to_num;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    
    private String zipcode = "";
    private String from_num = "";
    private String to_num = "";
    private String area = "";
}
