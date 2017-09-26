package models;

import java.io.Serializable;

public class Member_Economic implements Serializable {

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getSubscription_year() {
        return subscription_year;
    }

    public void setSubscription_year(String subscription_year) {
        this.subscription_year = subscription_year;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public long getVoucher_file_id() {
        return voucher_file_id;
    }

    public void setVoucher_file_id(long voucher_file_id) {
        this.voucher_file_id = voucher_file_id;
    }

    public String getVoucher_file_type() {
        return voucher_file_type;
    }

    public void setVoucher_file_type(String voucher_file_type) {
        this.voucher_file_type = voucher_file_type;
    }
    
    private String member_id = "";
    private String payment_date = "";
    private String payment_type = "";
    private long voucher_file_id = 0;
    private String voucher_file_type = "";
    private String invoice = "";
    private String subscription_type = "";
    private String subscription_year = "";
    
    private int paid = 0;
    
    private Member member = new Member();
}
