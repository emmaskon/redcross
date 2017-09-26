package dao;

import java.util.ArrayList;
import models.Member_Economic;
import util.exceptions.DataSourceException;

public interface MemberEconomicDAO {
    
    public ArrayList<Member_Economic> getAllMemberEconomics() throws DataSourceException;
    public ArrayList<Member_Economic> getAllMemberEconomics(String fromdate, String todate) throws DataSourceException;
    public ArrayList<Member_Economic> getDepartmentMemberEconomics(String state_id, String city_id) throws DataSourceException;
    public ArrayList<Member_Economic> getDepartmentMemberEconomics(String state_id, String city_id, String fromdate, String todate) throws DataSourceException;
    public ArrayList<Member_Economic> getAllStateMemberEconomics(String state_id) throws DataSourceException;
    public ArrayList<Member_Economic> getAllStateMemberEconomics(String state_id, String fromdate, String todate) throws DataSourceException;
    
    public Member_Economic getMemberEconomic(String member_id, String invoice) throws DataSourceException;
    public ArrayList<Member_Economic> getMemberEconomics(String member_id) throws DataSourceException;
    
    public int getMaxPaidSubscriptionYear(String member_id) throws DataSourceException;
    
    public long getMaxVoucherFileId() throws DataSourceException;
    
    public String InsertMemberEconomic(String member_id, String payment_type, String payment_date, Long voucher_file_id, String voucher_file_type, String invoice, String subscription_type, String subscription_year) throws DataSourceException;
    public String UpdateMemberEconomic(String member_id, String old_invoice, String old_subscription_type, String payment_type, String payment_date, Long voucher_file_id, String voucher_file_type, String invoice, String subscription_type) throws DataSourceException;    
}
