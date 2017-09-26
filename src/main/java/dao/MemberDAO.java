package dao;

import java.util.ArrayList;
import util.exceptions.DataSourceException;
import models.Member;

public interface MemberDAO {

    public ArrayList<Member> getAllMembers() throws DataSourceException;
    public ArrayList<Member> getAllMembersOrderedByName() throws DataSourceException;
    public ArrayList<Member> getAllMembersNoPaidOrderedByName(int overyears) throws DataSourceException;
    public ArrayList<Member> getAllPendingMembersOrderedByName() throws DataSourceException;
    public ArrayList<Member> getAllRepeatedMembersOrderedByName() throws DataSourceException;
    public ArrayList<Member> getAllStateMembersOrderedByName(String state_id) throws DataSourceException;
    public ArrayList<Member> getAllStateMembersNoPaidOrderedByName(String state_id, int overyears) throws DataSourceException;
    public ArrayList<Member> getAllStatePendingMembersOrderedByName(String state_id) throws DataSourceException;
    public ArrayList<Member> getAllStateRepeatedMembersOrderedByName(String state_id) throws DataSourceException;
    public ArrayList<Member> getDepartmentMembers(String state_id, String city_id) throws DataSourceException;
    public ArrayList<Member> getDepartmentMembersOrderedByName(String state_id, String city_id) throws DataSourceException;
    public ArrayList<Member> getDepartmentMembersNoPaidOrderedByName(String state_id, String city_id, int overyears) throws DataSourceException;
    public ArrayList<Member> getDepartmentPendingMembersOrderedByName(String state_id, String city_id, String role) throws DataSourceException;
    public ArrayList<Member> getDepartmentRepeatedMembersOrderedByName(String state_id, String city_id, String role) throws DataSourceException;
    public ArrayList<Member> getMembers(String member_id, String last_name, String first_name, String father_name,
            String amka, String afm, String doy, String adt, String job, String address, String zip_code,
            String address_area, String municipality, String county, String state, String phone, String cell,
            String email, String fax, String dep_state, String dep_city, String applicationdate, String ptstatus, String kdstatus,
            String decisiondatekd, String remainingcost) throws DataSourceException;
    public String getHighestMemberCode() throws DataSourceException;
    
    public Member getMember(String member_id) throws DataSourceException;
    public Member getMemberUsingAfm(String afm) throws DataSourceException;
    public Member getDepartmentMember(String member_id, String state_id, String city_id) throws DataSourceException;
    public Member getStateMember(String member_id, String state_id) throws DataSourceException;
    
    public int getMembersCountWithJob(String job) throws DataSourceException;
    public int getMembersCountWithZipcode(String zipcode) throws DataSourceException;
    public int getMembersCountWithMunicipality(String municipality) throws DataSourceException;
    public int getMembersCountWithDoy(String doy) throws DataSourceException;
    
    public String InsertMember(String member_id, String last_name, String first_name, String father_name, String job, String address, String zip_code, 
                    String address_area, String municipality, String county, String phone, String cell, String email, String fax, String amka, String afm, 
                    String doy, String adt, String state, String department, String ttcomments, String ptcomments, String kdcomments, 
                    String ptstatus, String kdstatus, String decisiondatekd, String applicationdate, String registrationdate, String cost, 
                    String annualcost, String remainingcost, String friend, String last_paid_year) throws DataSourceException;
    
    public String UpdateMember(String role, String member_id, String last_name, String first_name, String father_name, String job, String address, String zip_code, 
                    String address_area, String municipality, String county, String phone, String cell, String email, String fax, String amka, String afm, 
                    String doy, String adt, String state, String department, String ttcomments, String ptcomments, String kdcomments, 
                    String ptstatus, String kdstatus, String decisiondatekd, String applicationdate, String registrationdate, String cost, 
                    String annualcost, String remainingcost, String friend) throws DataSourceException;
    
    public String deleteMember(String member_id) throws DataSourceException;
    
    public String newYearRemainingUpdate() throws DataSourceException;
    public String increaseRemaining() throws DataSourceException;
}