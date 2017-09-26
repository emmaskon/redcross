package models;

import java.io.Serializable;

public class Member implements Serializable {

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFathersname() {
        return fathersname;
    }

    public void setFathersname(String fathersname) {
        this.fathersname = fathersname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(String address_street) {
        this.address_street = address_street;
    }

    public String getAddress_number() {
        return address_number;
    }

    public void setAddress_number(String address_number) {
        this.address_number = address_number;
    }

    public String getAddress_zipcode() {
        return address_zipcode;
    }

    public void setAddress_zipcode(String address_zipcode) {
        this.address_zipcode = address_zipcode;
    }

    public String getAddress_municipality() {
        return address_municipality;
    }

    public void setAddress_municipality(String address_municipality) {
        this.address_municipality = address_municipality;
    }

    public String getAddress_county() {
        return address_county;
    }

    public void setAddress_county(String address_county) {
        this.address_county = address_county;
    }

    public String getAddress_state() {
        return address_state;
    }

    public void setAddress_state(String address_state) {
        this.address_state = address_state;
    }

    public String getAddress_state_name() {
        return address_state_name;
    }

    public void setAddress_state_name(String address_state_name) {
        this.address_state_name = address_state_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getDoy() {
        return doy;
    }

    public void setDoy(String doy) {
        this.doy = doy;
    }

    public String getIdentity_number() {
        return identity_number;
    }

    public void setIdentity_number(String identity_number) {
        this.identity_number = identity_number;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTt_notes() {
        return tt_notes;
    }

    public void setTt_notes(String tt_notes) {
        this.tt_notes = tt_notes;
    }

    public String getPt_notes() {
        return pt_notes;
    }

    public void setPt_notes(String pt_notes) {
        this.pt_notes = pt_notes;
    }

    public String getPt_forward() {
        return pt_forward;
    }

    public void setPt_forward(String pt_forward) {
        this.pt_forward = pt_forward;
    }

    public String getKd_notes() {
        return kd_notes;
    }

    public void setKd_notes(String kd_notes) {
        this.kd_notes = kd_notes;
    }

    public String getKd_approval() {
        return kd_approval;
    }

    public void setKd_approval(String kd_approval) {
        this.kd_approval = kd_approval;
    }

    public String getRegistration_fee() {
        return registration_fee;
    }

    public void setRegistration_fee(String registration_fee) {
        this.registration_fee = registration_fee;
    }

    public String getAnnual_membership() {
        return annual_membership;
    }

    public void setAnnual_membership(String annual_membership) {
        this.annual_membership = annual_membership;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getAddress_area() {
        return address_area;
    }

    public void setAddress_area(String address_area) {
        this.address_area = address_area;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getDepartment_type() {
        return department_type;
    }

    public void setDepartment_type(String department_type) {
        this.department_type = department_type;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getLast_paid_year() {
        return last_paid_year;
    }

    public void setLast_paid_year(String last_paid_year) {
        this.last_paid_year = last_paid_year;
    }

    public String getDecisiondatekd() {
        return decisiondatekd;
    }

    public void setDecisiondatekd(String decisiondatekd) {
        this.decisiondatekd = decisiondatekd;
    }

    public String getApplicationdate() {
        return applicationdate;
    }

    public void setApplicationdate(String applicationdate) {
        this.applicationdate = applicationdate;
    }

    public String getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(String registrationdate) {
        this.registrationdate = registrationdate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    private String surname = "";
    private String name = "";
    private String fathersname = "";
    private String job = "";
    private String address = "";
    private String address_street = "";
    private String address_number = "";
    private String address_zipcode = "";
    private String address_area = "";
    private String address_municipality = "";
    private String address_county = "";
    private String address_state = "";
    private String address_state_name = "";
    private String city_id = "";
    private String phone = "";
    private String mobile_phone = "";
    private String email = "";
    private String fax = "";
    private String amka = "";
    private String afm = "";
    private String doy = "";
    private String identity_number = "";
    private String member_id = "";
    private String tt_notes = "";
    private String pt_notes = "";
    private String pt_forward = "";
    private String kd_notes = "";
    private String kd_approval = "";
    private String registration_fee = "";
    private String annual_membership = "";
    private String friend = "";
    private String last_paid_year = "";
    private String remaining = "";
    private String pt = "";
    private String tt = "";
    private String decisiondatekd = "";
    private String applicationdate = "";
    private String registrationdate = "";

    private String department_type = "";
    private String department_name = "";
}
