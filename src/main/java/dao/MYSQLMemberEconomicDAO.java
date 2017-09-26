package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Member;
import models.Member_Economic;
import models.Subscription_Type;
import util.exceptions.DataSourceException;

public class MYSQLMemberEconomicDAO implements MemberEconomicDAO {

    public MYSQLMemberEconomicDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }
    
    @Override
    public ArrayList<Member_Economic> getAllMemberEconomics() throws DataSourceException {
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM member_economics");
            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member_Economic> member_economics = new ArrayList<Member_Economic>();

            while (rs.next()) {
                Member_Economic member_economic = new Member_Economic();
                
                member_economic.setMember_id(rs.getString("member_id"));
                member_economic.setPayment_type(rs.getString("payment_type"));
                member_economic.setPayment_date(rs.getString("payment_date"));
                member_economic.setVoucher_file_id(rs.getLong("voucher_file_id"));
                member_economic.setVoucher_file_type(rs.getString("voucher_file_type"));
                member_economic.setInvoice(rs.getString("invoice"));
                member_economic.setSubscription_type(rs.getString("subscription_type"));
                member_economic.setSubscription_year(rs.getString("subscription_year"));
                
                Member member = dao_factory.getMemberDAO().getMember(member_economic.getMember_id());
                member_economic.setMember(member);
                
                if(!member.getMember_id().equals("")){
                    member_economics.add(member_economic);
                }
            }

            return member_economics;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member_Economic> getAllMemberEconomics(String fromdate, String todate) throws DataSourceException {
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
           
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM member_economics "
                                                                + "WHERE STR_TO_DATE(payment_date,'%d/%m/%Y') >= STR_TO_DATE(?,'%d/%m/%Y') AND "
                                                                + "STR_TO_DATE(payment_date,'%d/%m/%Y') <= STR_TO_DATE(?,'%d/%m/%Y')");
            
            pstmt.setString(1, fromdate);
            pstmt.setString(2, todate);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member_Economic> member_economics = new ArrayList<Member_Economic>();

            while (rs.next()) {
                Member_Economic member_economic = new Member_Economic();
                
                member_economic.setMember_id(rs.getString("member_id"));
                member_economic.setPayment_type(rs.getString("payment_type"));
                member_economic.setPayment_date(rs.getString("payment_date"));
                member_economic.setVoucher_file_id(rs.getLong("voucher_file_id"));
                member_economic.setVoucher_file_type(rs.getString("voucher_file_type"));
                member_economic.setInvoice(rs.getString("invoice"));
                member_economic.setSubscription_type(rs.getString("subscription_type"));
                member_economic.setSubscription_year(rs.getString("subscription_year"));
                
                Member member = dao_factory.getMemberDAO().getMember(member_economic.getMember_id());
                member_economic.setMember(member);
                
                if(!member.getMember_id().equals("")){
                    member_economics.add(member_economic);
                }
            }

            return member_economics;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member_Economic> getDepartmentMemberEconomics(String state_id, String city_id) throws DataSourceException {
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM member_economics");
            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member_Economic> member_economics = new ArrayList<Member_Economic>();

            while (rs.next()) {
                Member_Economic member_economic = new Member_Economic();
                
                member_economic.setMember_id(rs.getString("member_id"));
                member_economic.setPayment_type(rs.getString("payment_type"));
                member_economic.setPayment_date(rs.getString("payment_date"));
                member_economic.setVoucher_file_id(rs.getLong("voucher_file_id"));
                member_economic.setVoucher_file_type(rs.getString("voucher_file_type"));
                member_economic.setInvoice(rs.getString("invoice"));
                member_economic.setSubscription_type(rs.getString("subscription_type"));
                member_economic.setSubscription_year(rs.getString("subscription_year"));
                
                Member member = dao_factory.getMemberDAO().getDepartmentMember(member_economic.getMember_id(), state_id, city_id);
                member_economic.setMember(member);
                
                if(!member.getMember_id().equals("")){
                    member_economics.add(member_economic);
                }
            }

            return member_economics;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member_Economic> getDepartmentMemberEconomics(String state_id, String city_id, String fromdate, String todate) throws DataSourceException {
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM member_economics "
                                                                + "WHERE STR_TO_DATE(payment_date,'%d/%m/%Y') >= STR_TO_DATE(?,'%d/%m/%Y') AND "
                                                                + "STR_TO_DATE(payment_date,'%d/%m/%Y') <= STR_TO_DATE(?,'%d/%m/%Y')");
            
            pstmt.setString(1, fromdate);
            pstmt.setString(2, todate);
            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member_Economic> member_economics = new ArrayList<Member_Economic>();

            while (rs.next()) {
                Member_Economic member_economic = new Member_Economic();
                
                member_economic.setMember_id(rs.getString("member_id"));
                member_economic.setPayment_type(rs.getString("payment_type"));
                member_economic.setPayment_date(rs.getString("payment_date"));
                member_economic.setVoucher_file_id(rs.getLong("voucher_file_id"));
                member_economic.setVoucher_file_type(rs.getString("voucher_file_type"));
                member_economic.setInvoice(rs.getString("invoice"));
                member_economic.setSubscription_type(rs.getString("subscription_type"));
                member_economic.setSubscription_year(rs.getString("subscription_year"));
                
                Member member = dao_factory.getMemberDAO().getDepartmentMember(member_economic.getMember_id(), state_id, city_id);
                member_economic.setMember(member);
                
                if(!member.getMember_id().equals("")){
                    member_economics.add(member_economic);
                }
            }

            return member_economics;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member_Economic> getAllStateMemberEconomics(String state_id) throws DataSourceException {
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM member_economics");
            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member_Economic> member_economics = new ArrayList<Member_Economic>();

            while (rs.next()) {
                Member_Economic member_economic = new Member_Economic();
                
                member_economic.setMember_id(rs.getString("member_id"));
                member_economic.setPayment_type(rs.getString("payment_type"));
                member_economic.setPayment_date(rs.getString("payment_date"));
                member_economic.setVoucher_file_id(rs.getLong("voucher_file_id"));
                member_economic.setVoucher_file_type(rs.getString("voucher_file_type"));
                member_economic.setInvoice(rs.getString("invoice"));
                member_economic.setSubscription_type(rs.getString("subscription_type"));
                member_economic.setSubscription_year(rs.getString("subscription_year"));
                
                Member member = dao_factory.getMemberDAO().getStateMember(member_economic.getMember_id(), state_id);
                member_economic.setMember(member);
                
                if(!member.getMember_id().equals("")){
                    member_economics.add(member_economic);
                }
            }

            return member_economics;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member_Economic> getAllStateMemberEconomics(String state_id, String fromdate, String todate) throws DataSourceException {
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
           
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM member_economics "
                                                                + "WHERE STR_TO_DATE(payment_date,'%d/%m/%Y') >= STR_TO_DATE(?,'%d/%m/%Y') AND "
                                                                + "STR_TO_DATE(payment_date,'%d/%m/%Y') <= STR_TO_DATE(?,'%d/%m/%Y')");
            
            pstmt.setString(1, fromdate);
            pstmt.setString(2, todate);
            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member_Economic> member_economics = new ArrayList<Member_Economic>();

            while (rs.next()) {
                Member_Economic member_economic = new Member_Economic();
                
                member_economic.setMember_id(rs.getString("member_id"));
                member_economic.setPayment_type(rs.getString("payment_type"));
                member_economic.setPayment_date(rs.getString("payment_date"));
                member_economic.setVoucher_file_id(rs.getLong("voucher_file_id"));
                member_economic.setVoucher_file_type(rs.getString("voucher_file_type"));
                member_economic.setInvoice(rs.getString("invoice"));
                member_economic.setSubscription_type(rs.getString("subscription_type"));
                member_economic.setSubscription_year(rs.getString("subscription_year"));
                
                Member member = dao_factory.getMemberDAO().getStateMember(member_economic.getMember_id(), state_id);
                member_economic.setMember(member);
                
                if(!member.getMember_id().equals("")){
                    member_economics.add(member_economic);
                }
            }

            return member_economics;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public Member_Economic getMemberEconomic(String member_id, String invoice) throws DataSourceException {
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM member_economics WHERE member_id=? AND invoice=?");
            
            pstmt.setString(1,  member_id);
            pstmt.setString(2,  invoice);
            ResultSet rs = pstmt.executeQuery();

            Member_Economic member_economic = new Member_Economic();

            while (rs.next()) {                
                member_economic.setMember_id(rs.getString("member_id"));
                member_economic.setPayment_type(rs.getString("payment_type"));
                member_economic.setPayment_date(rs.getString("payment_date"));
                member_economic.setVoucher_file_id(rs.getLong("voucher_file_id"));
                member_economic.setVoucher_file_type(rs.getString("voucher_file_type"));
                member_economic.setInvoice(rs.getString("invoice"));
                member_economic.setSubscription_type(rs.getString("subscription_type"));
                member_economic.setSubscription_year(rs.getString("subscription_year"));
                
                Member member = dao_factory.getMemberDAO().getMember(member_economic.getMember_id());
                member_economic.setMember(member);
            }
            
            return member_economic;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member_Economic> getMemberEconomics(String member_id) throws DataSourceException {
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM member_economics WHERE member_id=?");
            
            pstmt.setString(1,  member_id);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member_Economic> member_economics = new ArrayList<Member_Economic>();

            while (rs.next()) {
                Member_Economic member_economic = new Member_Economic();
                
                member_economic.setMember_id(rs.getString("member_id"));
                member_economic.setPayment_type(rs.getString("payment_type"));
                member_economic.setPayment_date(rs.getString("payment_date"));
                member_economic.setVoucher_file_id(rs.getLong("voucher_file_id"));
                member_economic.setVoucher_file_type(rs.getString("voucher_file_type"));
                member_economic.setInvoice(rs.getString("invoice"));
                member_economic.setSubscription_type(rs.getString("subscription_type"));
                member_economic.setSubscription_year(rs.getString("subscription_year"));
                
                Member member = dao_factory.getMemberDAO().getMember(member_economic.getMember_id());
                member_economic.setMember(member);
                
                if(!member.getMember_id().equals("")){
                    member_economics.add(member_economic);
                }
            }

            return member_economics;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public int getMaxPaidSubscriptionYear(String member_id) throws DataSourceException {
        try {            
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT last_paid_year "
                    + "FROM members "
                    + "WHERE member_id=?");
            
            pstmt.setString(1,  member_id);
            ResultSet rs = pstmt.executeQuery();

            int max_paid_year=-1;
            
            while (rs.next()) {
                try{
                    max_paid_year = Integer.parseInt(rs.getString("last_paid_year"));
                }catch(Exception e){
                    max_paid_year=-1;
                }
            }
            
            if(max_paid_year==-1){
                PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT MAX(subscription_year) AS max_paid_year "
                        + "FROM member_economics "
                        + "WHERE member_id=?");

                pstmt2.setString(1,  member_id);
                ResultSet rs2 = pstmt2.executeQuery();

                while (rs2.next()) {
                    try{
                        max_paid_year = Integer.parseInt(rs2.getString("max_paid_year"));
                    }catch(Exception e){
                        max_paid_year=-1;
                    }
                }
            }

            return max_paid_year;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public long getMaxVoucherFileId() throws DataSourceException {
        try {            
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT MAX(voucher_file_id) AS  voucher_file_id "
                    + "FROM member_economics");
            
            ResultSet rs = pstmt.executeQuery();

            long max_voucher_file_id=0;
            
            while (rs.next()) {
                try{
                    max_voucher_file_id = rs.getLong("voucher_file_id");
                }catch(Exception e){
                    max_voucher_file_id=0;
                }
            }
            

            return max_voucher_file_id;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public String InsertMemberEconomic(String member_id, String payment_type, String payment_date, Long voucher_file_id, String voucher_file_type, String invoice, String subscription_type, String subscription_year) throws DataSourceException {
        String result="unknown";
        
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            ArrayList<Subscription_Type> subscription_types = dao_factory.getSubscriptionTypeDAO().getAllSubscriptionTypes();
        
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "INSERT INTO member_economics(member_id, payment_type, payment_date, voucher_file_id, voucher_file_type, invoice, subscription_type, subscription_year) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            
            pstmt.setString(1,  member_id);
            pstmt.setString(2,  payment_type);
            pstmt.setString(3,  payment_date);
            pstmt.setLong(4, voucher_file_id);
            pstmt.setString(5, voucher_file_type);
            pstmt.setString(6,  invoice);
            pstmt.setString(7,  subscription_type);
            pstmt.setString(8,  subscription_year);
            
            pstmt.executeUpdate();
            
            PreparedStatement pstmt1 = db_conn.prepareStatement("SELECT remaining "
                    + "FROM members "
                    + "WHERE member_id=?");
            
            pstmt1.setString(1,  member_id);
            ResultSet rs1 = pstmt1.executeQuery();

            int remaining=0;
            
            while (rs1.next()) {
                try{
                    String remainingStr = rs1.getString("remaining");
                    String[] remaining_parts1 = remainingStr.split("\\.");
                    String[] remaining_parts2 = remaining_parts1[0].split(",");
                    remaining = Integer.parseInt(remaining_parts2[0]);
                }catch(Exception e){
                    remaining=0;
                }
            }
            
            for (Subscription_Type subscription_type_record : subscription_types) {
                if(subscription_type.equals(subscription_type_record.getType())){
                    remaining -= Integer.parseInt(subscription_type_record.getCost());
                }
            }
            
            PreparedStatement pstmt2 = db_conn.prepareStatement(
                    "UPDATE members SET last_paid_year = ?, remaining = ? WHERE member_id = ?"
            );
            
            pstmt2.setString(1,  subscription_year);
            pstmt2.setString(2,  remaining+"");
            pstmt2.setString(3,  member_id);
            
            pstmt2.executeUpdate();

            result = "insert ok";
        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
        
        return result;
    }
    
    @Override
    public String UpdateMemberEconomic(String member_id, String old_invoice, String old_subscription_type, String payment_type, String payment_date, Long voucher_file_id, String voucher_file_type, String invoice, String subscription_type) throws DataSourceException {
        String result="unknown";
        
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            ArrayList<Subscription_Type> subscription_types = dao_factory.getSubscriptionTypeDAO().getAllSubscriptionTypes();
        
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "UPDATE member_economics SET payment_type=?, payment_date=?, voucher_file_id=?, voucher_file_type=?, invoice=?, subscription_type=? WHERE member_id=? AND invoice=?"
            );
            
            pstmt.setString(1,  payment_type);
            pstmt.setString(2,  payment_date);
            pstmt.setLong(3, voucher_file_id);
            pstmt.setString(4, voucher_file_type);
            pstmt.setString(5,  invoice);
            pstmt.setString(6,  subscription_type);
            pstmt.setString(7,  member_id);
            pstmt.setString(8,  old_invoice);
            
            pstmt.executeUpdate();
            
            if(!subscription_type.equals(old_subscription_type)){
                int old_cost=0, new_cost=0, cost_dif=0;
                for (Subscription_Type subscription_type_record : subscription_types) {
                    if(old_subscription_type.equals(subscription_type_record.getType())){
                        old_cost=Integer.parseInt(subscription_type_record.getCost());
                    }
                    if(subscription_type.equals(subscription_type_record.getType())){
                        new_cost=Integer.parseInt(subscription_type_record.getCost());
                    }
                }
                cost_dif = old_cost - new_cost;
                
                PreparedStatement pstmt1 = db_conn.prepareStatement("SELECT remaining "
                    + "FROM members "
                    + "WHERE member_id=?");
            
                pstmt1.setString(1,  member_id);
                ResultSet rs1 = pstmt1.executeQuery();

                int remaining=0;

                while (rs1.next()) {
                    try{
                        String remainingStr = rs1.getString("remaining");
                        String[] remaining_parts1 = remainingStr.split("\\.");
                        String[] remaining_parts2 = remaining_parts1[0].split(",");
                        remaining = Integer.parseInt(remaining_parts2[0]);
                    }catch(Exception e){
                        remaining=0;
                    }
                }
                
                remaining += cost_dif;
                
                PreparedStatement pstmt2 = db_conn.prepareStatement(
                    "UPDATE members SET remaining = ? WHERE member_id = ?"
                );

                pstmt2.setString(1,  remaining+"");
                pstmt2.setString(2,  member_id);

                pstmt2.executeUpdate();
            }
            
            result = "insert ok";
        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
        
        return result;
    }

    private final Connection db_conn;

}
