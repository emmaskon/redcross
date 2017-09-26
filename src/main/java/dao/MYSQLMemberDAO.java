package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import models.Member;
import models.Subscription_Type;
import util.exceptions.DataSourceException;

public class MYSQLMemberDAO implements MemberDAO {

    public MYSQLMemberDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<Member> getAllMembers() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.pt_forward=? AND members.kd_approval=?");

            pstmt.setInt(1, 1);
            pstmt.setInt(2, 1);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();

            while (rs.next()) {
                Member member = new Member();
                
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
                
                members.add(member);
            }

            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getMembers(String member_id, String last_name, String first_name, String father_name,
            String amka, String afm, String doy, String adt, String job, String address, String zip_code,
            String address_area, String municipality, String county, String state, String phone, String cell,
            String email, String fax, String dep_state, String dep_city, String applicationdate, String ptstatus, String kdstatus,
            String decisiondatekd, String remainingcost) throws DataSourceException {
        try {
            
            String query = "SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                        + "FROM members "
                        + "INNER JOIN states ON members.address_state=states.id "
                        + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id WHERE ";
            
            String op = "AND ";
            boolean params_exist=false;
            
            boolean member_id_exist=false; 
            boolean last_name_exist=false; 
            boolean first_name_exist=false; 
            boolean father_name_exist=false;
            boolean amka_exist=false; 
            boolean afm_exist=false; 
            boolean doy_exist=false; 
            boolean adt_exist=false; 
            boolean job_exist=false; 
            boolean address_exist=false; 
            boolean zip_code_exist=false;
            boolean address_area_exist=false; 
            boolean municipality_exist=false; 
            boolean county_exist=false; 
            boolean state_exist=false; 
            boolean phone_exist=false; 
            boolean cell_exist=false;
            boolean email_exist=false; 
            boolean fax_exist=false;
            boolean dep_state_exist=false; 
            boolean dep_city_exist=false; 
            boolean applicationdate_exist=false; 
            boolean ptstatus_exist=false; 
            boolean kdstatus_exist=false;
            boolean decisiondatekd_exist=false; 
            boolean remainingcost_exist=false;
            
            if(!member_id.equals("")){
                query+="members.member_id LIKE ? ";
                params_exist=true;
                member_id_exist=true;
            }
            if(!last_name.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.surname LIKE ?";
                params_exist=true;
                last_name_exist=true;
            }
            if(!first_name.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.name LIKE ? ";
                params_exist=true;
                first_name_exist=true;
            }
            if(!father_name.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.fathersname LIKE ? ";
                params_exist=true;
                father_name_exist=true;
            }
            if(!amka.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.amka LIKE ? ";
                params_exist=true;
                amka_exist=true;
            }
            if(!afm.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.afm LIKE ? ";
                params_exist=true;
                afm_exist=true;
            }
            if(!doy.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.doy=? ";
                params_exist=true;
                doy_exist=true;
            }
            if(!adt.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.identity_number LIKE ? ";
                params_exist=true;
                adt_exist=true;
            }
            if(!job.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.job LIKE ? ";
                params_exist=true;
                job_exist=true;
            }
            if(!address.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.address LIKE ? ";
                params_exist=true;
                address_exist=true;
            }
            if(!zip_code.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.address_zipcode LIKE ? ";
                params_exist=true;
                zip_code_exist=true;
            }
            if(!address_area.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.address_area LIKE ? ";
                params_exist=true;
                address_area_exist=true;
            }
            if(!municipality.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.address_municipality=? ";
                params_exist=true;
                municipality_exist=true;
            }
            if(!county.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.address_county=? ";
                params_exist=true;
                county_exist=true;
            }
            if(!state.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.address_state=? ";
                params_exist=true;
                state_exist=true;
            }
            if(!phone.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.phone LIKE ? ";
                params_exist=true;
                phone_exist=true;
            }
            if(!cell.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.mobile_phone LIKE ? ";
                params_exist=true;
                cell_exist=true;
            }
            if(!email.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.email LIKE ? ";
                params_exist=true;
                email_exist=true;
            }
            if(!fax.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.fax LIKE ? ";
                params_exist=true;
                fax_exist=true;
            }
            if(!dep_state.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.address_state=? ";
                params_exist=true;
                dep_state_exist=true;
            }
            if(!dep_city.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.city_id=? ";
                params_exist=true;
                dep_city_exist=true;
            }
            if(!applicationdate.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.applicationdate=? ";
                params_exist=true;
                applicationdate_exist=true;
            }
            if(!ptstatus.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.pt_forward=? ";
                params_exist=true;
                ptstatus_exist=true;
            }
            if(!kdstatus.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.kd_approval=? ";
                params_exist=true;
                kdstatus_exist=true;
            }
            if(!decisiondatekd.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.decisiondatekd=? ";
                params_exist=true;
                decisiondatekd_exist=true;
            }
            if(!remainingcost.equals("")){
                if(params_exist==true){query+=op;}
                query+="members.remaining=? ";
                params_exist=true;
                remainingcost_exist=true;
            }
            
            query+="ORDER BY members.surname";
            
            PreparedStatement pstmt = db_conn.prepareStatement(query);
            
            int i=1;
            if(member_id_exist==true){pstmt.setString(i, "%"+member_id+"%"); i++;} 
            if(last_name_exist==true){pstmt.setString(i, "%"+last_name+"%"); i++;} 
            if(first_name_exist==true){pstmt.setString(i, "%"+first_name+"%"); i++;} 
            if(father_name_exist==true){pstmt.setString(i, "%"+father_name+"%"); i++;}
            if(amka_exist==true){pstmt.setString(i, "%"+amka+"%"); i++;} 
            if(afm_exist==true){pstmt.setString(i, "%"+afm+"%"); i++;} 
            if(doy_exist==true){pstmt.setString(i, doy); i++;} 
            if(adt_exist==true){pstmt.setString(i, "%"+adt+"%"); i++;} 
            if(job_exist==true){pstmt.setString(i, "%"+job+"%"); i++;} 
            if(address_exist==true){pstmt.setString(i, "%"+address+"%"); i++;} 
            if(zip_code_exist==true){pstmt.setString(i, "%"+zip_code+"%"); i++;}
            if(address_area_exist==true){pstmt.setString(i, "%"+address_area+"%"); i++;} 
            if(municipality_exist==true){pstmt.setString(i, municipality); i++;} 
            if(county_exist==true){pstmt.setString(i, county); i++;} 
            if(state_exist==true){pstmt.setString(i, state); i++;} 
            if(phone_exist==true){pstmt.setString(i, "%"+phone+"%"); i++;} 
            if(cell_exist==true){pstmt.setString(i, "%"+cell+"%"); i++;}
            if(email_exist==true){pstmt.setString(i, "%"+email+"%"); i++;} 
            if(fax_exist==true){pstmt.setString(i, "%"+fax+"%"); i++;}
            if(dep_state_exist==true){pstmt.setString(i, dep_state); i++;} 
            if(dep_city_exist==true){pstmt.setString(i, dep_city); i++;} 
            if(applicationdate_exist==true){pstmt.setString(i, applicationdate); i++;} 
            if(ptstatus_exist==true){pstmt.setString(i, ptstatus); i++;} 
            if(kdstatus_exist==true){pstmt.setString(i, kdstatus); i++;}
            if(decisiondatekd_exist==true){pstmt.setString(i, decisiondatekd); i++;} 
            if(remainingcost_exist==true){pstmt.setString(i, remainingcost); i++;}

            ArrayList<Member> members = new ArrayList<Member>();
            
            if(params_exist==true){
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Member member = new Member();

                    member.setSurname(rs.getString("surname"));
                    member.setName(rs.getString("name"));
                    member.setFathersname(rs.getString("fathersname"));
                    member.setJob(rs.getString("job"));
                    member.setAddress(rs.getString("address"));
                    member.setAddress_zipcode(rs.getString("address_zipcode"));
                    member.setAddress_area(rs.getString("address_area"));
                    member.setAddress_municipality(rs.getString("address_municipality"));
                    member.setAddress_county(rs.getString("address_county"));
                    member.setAddress_state(rs.getString("address_state"));
                    member.setAddress_state_name(rs.getString("state_name"));
                    member.setCity_id(rs.getString("city_id"));
                    member.setPhone(rs.getString("phone"));
                    member.setMobile_phone(rs.getString("mobile_phone"));
                    member.setEmail(rs.getString("email"));
                    member.setFax(rs.getString("fax"));
                    member.setAmka(rs.getString("amka"));
                    member.setAfm(rs.getString("afm"));
                    member.setDoy(rs.getString("doy"));
                    member.setIdentity_number(rs.getString("identity_number"));
                    member.setMember_id(rs.getString("member_id"));
                    member.setTt_notes(rs.getString("tt_notes"));
                    member.setPt_notes(rs.getString("pt_notes"));
                    member.setPt_forward(rs.getString("pt_forward"));
                    member.setKd_notes(rs.getString("kd_notes"));
                    member.setKd_approval(rs.getString("kd_approval"));
                    member.setDecisiondatekd(rs.getString("decisiondatekd"));
                    member.setApplicationdate(rs.getString("applicationdate"));
                    member.setRegistrationdate(rs.getString("registrationdate"));
                    member.setRegistration_fee(rs.getString("registration_fee"));
                    member.setAnnual_membership(rs.getString("annual_membership"));
                    member.setFriend(rs.getString("friend"));
                    member.setLast_paid_year(rs.getString("last_paid_year"));
                    member.setRemaining(rs.getString("remaining"));

                    member.setDepartment_name(rs.getString("department_name"));
                    member.setDepartment_type(rs.getString("department_type"));

                    if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                        member.setPt(member.getDepartment_name());
                        member.setTt("");
                    }

                    if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                        member.setTt(member.getDepartment_name());

                        PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                            + "WHERE type=? AND state_id=?");

                        pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                        pstmt2.setString(2, member.getAddress_state());

                        ResultSet rs2 = pstmt2.executeQuery();

                        while (rs2.next()) {
                            member.setPt(rs2.getString("name"));
                        }
                    }

                    members.add(member);
                }
            }
            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getAllMembersOrderedByName() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.pt_forward=? AND members.kd_approval=? "                                            
                                                                + "ORDER BY members.surname");

            pstmt.setInt(1, 1);
            pstmt.setInt(2, 1);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();

            while (rs.next()) {
                Member member = new Member();
                
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
                
                members.add(member);
            }

            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getAllMembersNoPaidOrderedByName(int overyears) throws DataSourceException {
        
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        
        ArrayList<Member> members = new ArrayList<Member>();
        
        try{
            ArrayList<Member> tmpmembers = dao_factory.getMemberDAO().getAllMembersOrderedByName();
            
            for (Member tmpmember : tmpmembers) {
                boolean remaining_exists=false;
                try{
                    String[] remaining_parts1 = tmpmember.getRemaining().split("\\.");
                    String[] remaining_parts2 = remaining_parts1[0].split(",");
                    if(Integer.parseInt(remaining_parts2[0])>0){
                        remaining_exists=true;
                    }
                }catch(Exception e){}
                
                int remaining_years = 0;
                
                if(remaining_exists==true){
                    Date date=new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    
                    try{
                        remaining_years = year - Integer.parseInt(tmpmember.getLast_paid_year());
                    }catch(Exception e){}
                }
                
                if(remaining_years > overyears){
                    members.add(tmpmember);
                }
            }
        }catch(Exception e){}
        
        return members;
    }
    
    @Override
    public ArrayList<Member> getAllPendingMembersOrderedByName() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.pt_forward=? AND members.kd_approval=? "                                            
                                                                + "ORDER BY members.surname");

            pstmt.setInt(1, 1);
            pstmt.setInt(2, 0);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();

            while (rs.next()) {
                Member member = new Member();
                
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
                
                members.add(member);
            }

            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getAllRepeatedMembersOrderedByName() throws DataSourceException {
        try {
            PreparedStatement pstmt1 = db_conn.prepareStatement("SELECT members.afm FROM members "
                                                                + "WHERE CHAR_LENGTH(members.afm)>5 "
                                                                + "GROUP BY members.afm HAVING COUNT(*) > 1");


            ResultSet rs1 = pstmt1.executeQuery();
            
            ArrayList<Member> members = new ArrayList<Member>();
            
            while (rs1.next()) {
                PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                    + "FROM members "
                                                                    + "INNER JOIN states ON members.address_state=states.id "
                                                                    + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                    + "WHERE members.afm = ? "
                                                                    + "ORDER BY members.surname");

                pstmt2.setString(1, rs1.getString("afm"));
                //pstmt2.setInt(2, 0);
                ResultSet rs2 = pstmt2.executeQuery();

                while (rs2.next()) {
                    Member member = new Member();

                    member.setSurname(rs2.getString("surname"));
                    member.setName(rs2.getString("name"));
                    member.setFathersname(rs2.getString("fathersname"));
                    member.setJob(rs2.getString("job"));
                    member.setAddress(rs2.getString("address"));
                    member.setAddress_zipcode(rs2.getString("address_zipcode"));
                    member.setAddress_area(rs2.getString("address_area"));
                    member.setAddress_municipality(rs2.getString("address_municipality"));
                    member.setAddress_county(rs2.getString("address_county"));
                    member.setAddress_state(rs2.getString("address_state"));
                    member.setAddress_state_name(rs2.getString("state_name"));
                    member.setCity_id(rs2.getString("city_id"));
                    member.setPhone(rs2.getString("phone"));
                    member.setMobile_phone(rs2.getString("mobile_phone"));
                    member.setEmail(rs2.getString("email"));
                    member.setFax(rs2.getString("fax"));
                    member.setAmka(rs2.getString("amka"));
                    member.setAfm(rs2.getString("afm"));
                    member.setDoy(rs2.getString("doy"));
                    member.setIdentity_number(rs2.getString("identity_number"));
                    member.setMember_id(rs2.getString("member_id"));
                    member.setTt_notes(rs2.getString("tt_notes"));
                    member.setPt_notes(rs2.getString("pt_notes"));
                    member.setPt_forward(rs2.getString("pt_forward"));
                    member.setKd_notes(rs2.getString("kd_notes"));
                    member.setKd_approval(rs2.getString("kd_approval"));
                    member.setDecisiondatekd(rs2.getString("decisiondatekd"));
                    member.setApplicationdate(rs2.getString("applicationdate"));
                    member.setRegistrationdate(rs2.getString("registrationdate"));
                    member.setRegistration_fee(rs2.getString("registration_fee"));
                    member.setAnnual_membership(rs2.getString("annual_membership"));
                    member.setFriend(rs2.getString("friend"));
                    member.setLast_paid_year(rs2.getString("last_paid_year"));
                    member.setRemaining(rs2.getString("remaining"));

                    member.setDepartment_name(rs2.getString("department_name"));
                    member.setDepartment_type(rs2.getString("department_type"));

                    if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                        member.setPt(member.getDepartment_name());
                        member.setTt("");
                    }

                    if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                        member.setTt(member.getDepartment_name());

                        PreparedStatement pstmt3 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                            + "WHERE type=? AND state_id=?");

                        pstmt3.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                        pstmt3.setString(2, member.getAddress_state());

                        ResultSet rs3 = pstmt3.executeQuery();

                        while (rs3.next()) {
                            member.setPt(rs2.getString("name"));
                        }
                    }

                    members.add(member);
                }
            }

            return members;

        } catch (Exception ex) {
            String exep = ex.toString();
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getAllStateMembersOrderedByName(String state_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.address_state=? AND members.pt_forward=? AND members.kd_approval=? "
                                                                + "ORDER BY members.surname");

            pstmt.setString(1, state_id);
            pstmt.setInt(2, 1);
            pstmt.setInt(3, 1);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();

            while (rs.next()) {
                Member member = new Member();
                
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
                
                members.add(member);
            }

            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getAllStateMembersNoPaidOrderedByName(String state_id, int overyears) throws DataSourceException {
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        
        ArrayList<Member> members = new ArrayList<Member>();
        
        try{
            ArrayList<Member> tmpmembers = dao_factory.getMemberDAO().getAllStateMembersOrderedByName(state_id);
            
            for (Member tmpmember : tmpmembers) {
                boolean remaining_exists=false;
                try{
                    String[] remaining_parts1 = tmpmember.getRemaining().split("\\.");
                    String[] remaining_parts2 = remaining_parts1[0].split(",");
                    if(Integer.parseInt(remaining_parts2[0])>0){
                        remaining_exists=true;
                    }
                }catch(Exception e){}
                
                int remaining_years = 0;
                
                if(remaining_exists==true){
                    Date date=new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    
                    try{
                        remaining_years = year - Integer.parseInt(tmpmember.getLast_paid_year());
                    }catch(Exception e){}
                }
                
                if(remaining_years > overyears){
                    members.add(tmpmember);
                }
            }
        }catch(Exception e){}
        
        return members;
    }
    
    @Override
    public ArrayList<Member> getAllStatePendingMembersOrderedByName(String state_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.address_state=? AND members.pt_forward=? "
                                                                + "ORDER BY members.surname");

            pstmt.setString(1, state_id);
            pstmt.setInt(2, 0);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();

            while (rs.next()) {
                Member member = new Member();
                
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
                
                members.add(member);
            }

            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getAllStateRepeatedMembersOrderedByName(String state_id) throws DataSourceException {
        try {
            PreparedStatement pstmt1 = db_conn.prepareStatement("SELECT members.afm FROM members "
                                                                + "WHERE CHAR_LENGTH(members.afm)>5 "
                                                                + "GROUP BY members.afm HAVING COUNT(*) > 1");


            ResultSet rs1 = pstmt1.executeQuery();
            
            ArrayList<Member> members = new ArrayList<Member>();
            
            while (rs1.next()) {
            
                PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                    + "FROM members "
                                                                    + "INNER JOIN states ON members.address_state=states.id "
                                                                    + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                    + "WHERE members.address_state=? AND members.pt_forward=? AND members.afm=? "
                                                                    + "ORDER BY members.surname");

                pstmt.setString(1, state_id);
                pstmt.setInt(2, 0);
                pstmt.setString(3, rs1.getString("afm"));
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Member member = new Member();

                    member.setSurname(rs.getString("surname"));
                    member.setName(rs.getString("name"));
                    member.setFathersname(rs.getString("fathersname"));
                    member.setJob(rs.getString("job"));
                    member.setAddress(rs.getString("address"));
                    member.setAddress_zipcode(rs.getString("address_zipcode"));
                    member.setAddress_area(rs.getString("address_area"));
                    member.setAddress_municipality(rs.getString("address_municipality"));
                    member.setAddress_county(rs.getString("address_county"));
                    member.setAddress_state(rs.getString("address_state"));
                    member.setAddress_state_name(rs.getString("state_name"));
                    member.setCity_id(rs.getString("city_id"));
                    member.setPhone(rs.getString("phone"));
                    member.setMobile_phone(rs.getString("mobile_phone"));
                    member.setEmail(rs.getString("email"));
                    member.setFax(rs.getString("fax"));
                    member.setAmka(rs.getString("amka"));
                    member.setAfm(rs.getString("afm"));
                    member.setDoy(rs.getString("doy"));
                    member.setIdentity_number(rs.getString("identity_number"));
                    member.setMember_id(rs.getString("member_id"));
                    member.setTt_notes(rs.getString("tt_notes"));
                    member.setPt_notes(rs.getString("pt_notes"));
                    member.setPt_forward(rs.getString("pt_forward"));
                    member.setKd_notes(rs.getString("kd_notes"));
                    member.setKd_approval(rs.getString("kd_approval"));
                    member.setDecisiondatekd(rs.getString("decisiondatekd"));
                    member.setApplicationdate(rs.getString("applicationdate"));
                    member.setRegistrationdate(rs.getString("registrationdate"));
                    member.setRegistration_fee(rs.getString("registration_fee"));
                    member.setAnnual_membership(rs.getString("annual_membership"));
                    member.setFriend(rs.getString("friend"));
                    member.setLast_paid_year(rs.getString("last_paid_year"));
                    member.setRemaining(rs.getString("remaining"));

                    member.setDepartment_name(rs.getString("department_name"));
                    member.setDepartment_type(rs.getString("department_type"));

                    if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                        member.setPt(member.getDepartment_name());
                        member.setTt("");
                    }

                    if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                        member.setTt(member.getDepartment_name());

                        PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                            + "WHERE type=? AND state_id=?");

                        pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                        pstmt2.setString(2, member.getAddress_state());

                        ResultSet rs2 = pstmt2.executeQuery();

                        while (rs2.next()) {
                            member.setPt(rs2.getString("name"));
                        }
                    }

                    members.add(member);
                }
            }

            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getDepartmentMembers(String state_id, String city_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.address_state=? AND members.city_id=? AND members.pt_forward=? AND members.kd_approval=?");

            pstmt.setString(1, state_id);
            pstmt.setString(2, city_id);
            pstmt.setInt(3, 1);
            pstmt.setInt(4, 1);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();

            while (rs.next()) {
                Member member = new Member();
                
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
                
                members.add(member);
            }

            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getDepartmentMembersOrderedByName(String state_id, String city_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.address_state=? AND members.city_id=? AND members.pt_forward=? AND members.kd_approval=? "
                                                                + "ORDER BY members.surname");

            pstmt.setString(1, state_id);
            pstmt.setString(2, city_id);
            pstmt.setInt(3, 1);
            pstmt.setInt(4, 1);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();

            while (rs.next()) {
                Member member = new Member();
                
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
                
                members.add(member);
            }

            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getDepartmentMembersNoPaidOrderedByName(String state_id, String city_id, int overyears) throws DataSourceException {
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        
        ArrayList<Member> members = new ArrayList<Member>();
        
        try{
            ArrayList<Member> tmpmembers = dao_factory.getMemberDAO().getDepartmentMembersOrderedByName(state_id, city_id);
            
            for (Member tmpmember : tmpmembers) {
                boolean remaining_exists=false;
                try{
                    String[] remaining_parts1 = tmpmember.getRemaining().split("\\.");
                    String[] remaining_parts2 = remaining_parts1[0].split(",");
                    if(Integer.parseInt(remaining_parts2[0])>0){
                        remaining_exists=true;
                    }
                }catch(Exception e){}
                
                int remaining_years = 0;
                
                if(remaining_exists==true){
                    Date date=new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    
                    try{
                        remaining_years = year - Integer.parseInt(tmpmember.getLast_paid_year());
                    }catch(Exception e){}
                }
                
                if(remaining_years > overyears){
                    members.add(tmpmember);
                }
            }
        }catch(Exception e){}
        
        return members;
    }
    
    @Override
    public ArrayList<Member> getDepartmentPendingMembersOrderedByName(String state_id, String city_id, String role) throws DataSourceException {
        try {
            PreparedStatement pstmt = null;
            
            if(role.equals("kd") || role.equals("su")){
                pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.address_state=? AND members.city_id=? AND members.pt_forward=? AND members.kd_approval=? "
                                                                + "ORDER BY members.surname");
                
                pstmt.setString(1, state_id);
                pstmt.setString(2, city_id);
                pstmt.setInt(3, 1);
                pstmt.setInt(4, 0);
            }else if(role.equals("pt")){
                pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.address_state=? AND members.city_id=? AND members.pt_forward=? "
                                                                + "ORDER BY members.surname");
                
                pstmt.setString(1, state_id);
                pstmt.setString(2, city_id);
                pstmt.setInt(3, 0);
            }else if(role.equals("tt")){
                pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.address_state=? AND members.city_id=? AND (members.pt_forward=? OR members.kd_approval=?) "
                                                                + "ORDER BY members.surname");
                
                pstmt.setString(1, state_id);
                pstmt.setString(2, city_id);
                pstmt.setInt(3, 0);
                pstmt.setInt(4, 0);
            }

            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();

            while (rs.next()) {
                Member member = new Member();
                
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
                
                members.add(member);
            }

            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Member> getDepartmentRepeatedMembersOrderedByName(String state_id, String city_id, String role) throws DataSourceException {
        try {
            PreparedStatement pstmt1 = db_conn.prepareStatement("SELECT members.afm FROM members "
                                                                + "WHERE CHAR_LENGTH(members.afm)>5 "
                                                                + "GROUP BY members.afm HAVING COUNT(*) > 1");


            ResultSet rs1 = pstmt1.executeQuery();
            
            ArrayList<Member> members = new ArrayList<Member>();
            
            while (rs1.next()) {
            
                PreparedStatement pstmt = null;
                //Use of the following structure for extendable code. Currently all queries all same.
                if(role.equals("kd") || role.equals("su")){
                    pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                    + "FROM members "
                                                                    + "INNER JOIN states ON members.address_state=states.id "
                                                                    + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                    + "WHERE members.address_state=? AND members.city_id=? AND members.afm=? "
                                                                    + "ORDER BY members.surname");

                    pstmt.setString(1, state_id);
                    pstmt.setString(2, city_id);
                    pstmt.setString(3, rs1.getString("afm"));
                }else if(role.equals("pt")){
                    pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                    + "FROM members "
                                                                    + "INNER JOIN states ON members.address_state=states.id "
                                                                    + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                    + "WHERE members.address_state=? AND members.city_id=? AND members.afm=? "
                                                                    + "ORDER BY members.surname");

                    pstmt.setString(1, state_id);
                    pstmt.setString(2, city_id);
                    pstmt.setString(3, rs1.getString("afm"));
                }else if(role.equals("tt")){
                    pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                    + "FROM members "
                                                                    + "INNER JOIN states ON members.address_state=states.id "
                                                                    + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                    + "WHERE members.address_state=? AND members.city_id=? AND members.afm=? "
                                                                    + "ORDER BY members.surname");

                    pstmt.setString(1, state_id);
                    pstmt.setString(2, city_id);
                    pstmt.setString(3, rs1.getString("afm"));
                }

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Member member = new Member();

                    member.setSurname(rs.getString("surname"));
                    member.setName(rs.getString("name"));
                    member.setFathersname(rs.getString("fathersname"));
                    member.setJob(rs.getString("job"));
                    member.setAddress(rs.getString("address"));
                    member.setAddress_zipcode(rs.getString("address_zipcode"));
                    member.setAddress_area(rs.getString("address_area"));
                    member.setAddress_municipality(rs.getString("address_municipality"));
                    member.setAddress_county(rs.getString("address_county"));
                    member.setAddress_state(rs.getString("address_state"));
                    member.setAddress_state_name(rs.getString("state_name"));
                    member.setCity_id(rs.getString("city_id"));
                    member.setPhone(rs.getString("phone"));
                    member.setMobile_phone(rs.getString("mobile_phone"));
                    member.setEmail(rs.getString("email"));
                    member.setFax(rs.getString("fax"));
                    member.setAmka(rs.getString("amka"));
                    member.setAfm(rs.getString("afm"));
                    member.setDoy(rs.getString("doy"));
                    member.setIdentity_number(rs.getString("identity_number"));
                    member.setMember_id(rs.getString("member_id"));
                    member.setTt_notes(rs.getString("tt_notes"));
                    member.setPt_notes(rs.getString("pt_notes"));
                    member.setPt_forward(rs.getString("pt_forward"));
                    member.setKd_notes(rs.getString("kd_notes"));
                    member.setKd_approval(rs.getString("kd_approval"));
                    member.setDecisiondatekd(rs.getString("decisiondatekd"));
                    member.setApplicationdate(rs.getString("applicationdate"));
                    member.setRegistrationdate(rs.getString("registrationdate"));
                    member.setRegistration_fee(rs.getString("registration_fee"));
                    member.setAnnual_membership(rs.getString("annual_membership"));
                    member.setFriend(rs.getString("friend"));
                    member.setLast_paid_year(rs.getString("last_paid_year"));
                    member.setRemaining(rs.getString("remaining"));

                    member.setDepartment_name(rs.getString("department_name"));
                    member.setDepartment_type(rs.getString("department_type"));

                    if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                        member.setPt(member.getDepartment_name());
                        member.setTt("");
                    }

                    if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                        member.setTt(member.getDepartment_name());

                        PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                            + "WHERE type=? AND state_id=?");

                        pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                        pstmt2.setString(2, member.getAddress_state());

                        ResultSet rs2 = pstmt2.executeQuery();

                        while (rs2.next()) {
                            member.setPt(rs2.getString("name"));
                        }
                    }

                    members.add(member);
                }
            }
            return members;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public String getHighestMemberCode() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT MAX(RIGHT(member_id, 9)) AS max_id FROM members");

            ResultSet rs = pstmt.executeQuery();

            String max_id = "";

            while (rs.next()) {
                max_id = rs.getString("max_id");
            }

            return max_id;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public int getMembersCountWithJob(String job) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT COUNT(job) AS count_job FROM members WHERE job=?");

            pstmt.setString(1, job);
            ResultSet rs = pstmt.executeQuery();
            
            int count_job=0;

            while (rs.next()) {
                try{
                    count_job = rs.getInt("count_job");
                }catch(Exception e){}
            }

            return count_job;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public int getMembersCountWithDoy(String doy) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT COUNT(doy) AS count_doy FROM members WHERE doy=?");

            pstmt.setString(1, doy);
            ResultSet rs = pstmt.executeQuery();
            
            int count_doy=0;

            while (rs.next()) {
                try{
                    count_doy = rs.getInt("count_doy");
                }catch(Exception e){}
            }

            return count_doy;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public int getMembersCountWithZipcode(String zipcode) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT COUNT(address_zipcode) AS count_zipcode FROM members WHERE address_zipcode=?");

            pstmt.setString(1, zipcode);
            ResultSet rs = pstmt.executeQuery();
            
            int count_zipcode=0;

            while (rs.next()) {
                try{
                    count_zipcode = rs.getInt("count_zipcode");
                }catch(Exception e){}
            }

            return count_zipcode;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public int getMembersCountWithMunicipality(String municipality) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT COUNT(address_municipality) AS count_municipality FROM members WHERE address_municipality=?");

            pstmt.setString(1, municipality);
            ResultSet rs = pstmt.executeQuery();
            
            int count_municipality=0;

            while (rs.next()) {
                try{
                    count_municipality = rs.getInt("count_municipality");
                }catch(Exception e){}
            }

            return count_municipality;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public Member getMember(String member_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.member_id=?");

            pstmt.setString(1, member_id);
            ResultSet rs = pstmt.executeQuery();

            Member member = new Member();

            while (rs.next()) {
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
            }

            return member;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public Member getMemberUsingAfm(String afm) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT members.*, states.*, departments.name AS department_name, departments.type AS department_type "
                                                                + "FROM members "
                                                                + "INNER JOIN states ON members.address_state=states.id "
                                                                + "INNER JOIN departments ON members.address_state=departments.state_id AND members.city_id=departments.city_id "
                                                                + "WHERE members.afm=?");

            pstmt.setString(1, afm);
            ResultSet rs = pstmt.executeQuery();

            Member member = new Member();

            while (rs.next()) {
                member.setSurname(rs.getString("surname"));
                member.setName(rs.getString("name"));
                member.setFathersname(rs.getString("fathersname"));
                member.setJob(rs.getString("job"));
                member.setAddress(rs.getString("address"));
                member.setAddress_zipcode(rs.getString("address_zipcode"));
                member.setAddress_area(rs.getString("address_area"));
                member.setAddress_municipality(rs.getString("address_municipality"));
                member.setAddress_county(rs.getString("address_county"));
                member.setAddress_state(rs.getString("address_state"));
                member.setAddress_state_name(rs.getString("state_name"));
                member.setCity_id(rs.getString("city_id"));
                member.setPhone(rs.getString("phone"));
                member.setMobile_phone(rs.getString("mobile_phone"));
                member.setEmail(rs.getString("email"));
                member.setFax(rs.getString("fax"));
                member.setAmka(rs.getString("amka"));
                member.setAfm(rs.getString("afm"));
                member.setDoy(rs.getString("doy"));
                member.setIdentity_number(rs.getString("identity_number"));
                member.setMember_id(rs.getString("member_id"));
                member.setTt_notes(rs.getString("tt_notes"));
                member.setPt_notes(rs.getString("pt_notes"));
                member.setPt_forward(rs.getString("pt_forward"));
                member.setKd_notes(rs.getString("kd_notes"));
                member.setKd_approval(rs.getString("kd_approval"));
                member.setDecisiondatekd(rs.getString("decisiondatekd"));
                member.setApplicationdate(rs.getString("applicationdate"));
                member.setRegistrationdate(rs.getString("registrationdate"));
                member.setRegistration_fee(rs.getString("registration_fee"));
                member.setAnnual_membership(rs.getString("annual_membership"));
                member.setFriend(rs.getString("friend"));
                member.setLast_paid_year(rs.getString("last_paid_year"));
                member.setRemaining(rs.getString("remaining"));
                
                member.setDepartment_name(rs.getString("department_name"));
                member.setDepartment_type(rs.getString("department_type"));
                
                if(member.getDepartment_type().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                    member.setPt(member.getDepartment_name());
                    member.setTt("");
                }
                
                if(member.getDepartment_type().equals("ΤΟΠΙΚΟ ΤΜΗΜΑ")){
                    member.setTt(member.getDepartment_name());
                    
                    PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT name FROM departments "
                                                                        + "WHERE type=? AND state_id=?");
                    
                    pstmt2.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
                    pstmt2.setString(2, member.getAddress_state());
                    
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    while (rs2.next()) {
                        member.setPt(rs2.getString("name"));
                    }
                }
            }

            return member;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public Member getDepartmentMember(String member_id, String state_id, String city_id) throws DataSourceException {
        
        Member member = getMember(member_id);
        
        if(member.getAddress_state().equals(state_id) && member.getCity_id().equals(city_id)){
            return member;
        }else{
            return new Member();
        }
    }
    
    @Override
    public Member getStateMember(String member_id, String state_id) throws DataSourceException {
        Member member = getMember(member_id);
        
        if(member.getAddress_state().equals(state_id)){
            return member;
        }else{
            return new Member();
        }
    }
    
    @Override
    public String InsertMember(String member_id, String last_name, String first_name, String father_name, String job, String address, String zip_code, 
                    String address_area, String municipality, String county, String phone, String cell, String email, String fax, String amka, String afm, 
                    String doy, String adt, String state, String department, String ttcomments, String ptcomments, String kdcomments, 
                    String ptstatus, String kdstatus, String decisiondatekd, String applicationdate, String registrationdate, String cost, 
                    String annualcost, String remainingcost, String friend, String last_paid_year) throws DataSourceException {
        String result="unknown";
        
        try {

            PreparedStatement pstmt = db_conn.prepareStatement(
                    "INSERT INTO members(surname, name, fathersname, job, address, address_zipcode, address_area, address_municipality, address_county, city_id, address_state, phone, mobile_phone, email, fax, amka, afm, doy, identity_number, member_id, tt_notes, pt_notes, pt_forward, kd_notes, kd_approval, decisiondatekd, applicationdate, registrationdate, registration_fee, annual_membership, friend, last_paid_year, remaining) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
                
            pstmt.setString(1,  last_name.toUpperCase());
            pstmt.setString(2,  first_name.toUpperCase());
            pstmt.setString(3,  father_name.toUpperCase());
            pstmt.setString(4,  job.toUpperCase());
            pstmt.setString(5,  address.toUpperCase());
            pstmt.setString(6,  zip_code);
            pstmt.setString(7,  address_area.toUpperCase());
            pstmt.setString(8,  municipality.toUpperCase());
            pstmt.setString(9,  county.toUpperCase());
            pstmt.setString(10,  department.toUpperCase());
            pstmt.setString(11,  state.toUpperCase());
            pstmt.setString(12,  phone);
            pstmt.setString(13,  cell);
            pstmt.setString(14,  email);
            pstmt.setString(15,  fax);
            pstmt.setString(16,  amka);
            pstmt.setString(17,  afm);
            pstmt.setString(18,  doy.toUpperCase());
            pstmt.setString(19,  adt.toUpperCase());
            pstmt.setString(20,  member_id);
            pstmt.setString(21,  ttcomments);
            pstmt.setString(22,  ptcomments);
            pstmt.setString(23,  ptstatus);
            pstmt.setString(24,  kdcomments);
            pstmt.setString(25,  kdstatus);
            pstmt.setString(26,  decisiondatekd);
            pstmt.setString(27,  applicationdate);
            pstmt.setString(28,  registrationdate);
            pstmt.setString(29,  cost);
            pstmt.setString(30,  annualcost);
            pstmt.setString(31,  friend);
            pstmt.setString(32,  last_paid_year);
            pstmt.setString(33,  remainingcost);
            
            pstmt.executeUpdate();

            result = "insert ok";
        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
        
        return result;
    }
    
    @Override
    public String UpdateMember(String role, String member_id, String last_name, String first_name, String father_name, String job, String address, String zip_code, 
                    String address_area, String municipality, String county, String phone, String cell, String email, String fax, String amka, String afm, 
                    String doy, String adt, String state, String department, String ttcomments, String ptcomments, String kdcomments, 
                    String ptstatus, String kdstatus, String decisiondatekd, String applicationdate, String registrationdate, String cost, String annualcost, String remainingcost, String friend) throws DataSourceException {
        String result="unknown";
        
        try {

            PreparedStatement pstmt = null;
            
            if(role.equals("su") || role.equals("kd")){
                pstmt = db_conn.prepareStatement(
                        "UPDATE members SET surname=?, name=?, fathersname=?, job=?, address=?, address_zipcode=?, address_area=?, address_municipality=?, address_county=?, "
                                + "city_id=?, address_state=?, phone=?, mobile_phone=?, email=?, fax=?, amka=?, afm=?, doy=?, identity_number=?, tt_notes=?, pt_notes=?, "
                                + "pt_forward=?, kd_notes=?, kd_approval=?, decisiondatekd=?, applicationdate=?, registrationdate=?, registration_fee=?, annual_membership=?, friend=?, remaining=? "
                                + "WHERE member_id=?"
                );

                pstmt.setString(1,  last_name.toUpperCase());
                pstmt.setString(2,  first_name.toUpperCase());
                pstmt.setString(3,  father_name.toUpperCase());
                pstmt.setString(4,  job.toUpperCase());
                pstmt.setString(5,  address.toUpperCase());
                pstmt.setString(6,  zip_code);
                pstmt.setString(7,  address_area.toUpperCase());
                pstmt.setString(8,  municipality.toUpperCase());
                pstmt.setString(9,  county.toUpperCase());
                pstmt.setString(10,  department.toUpperCase());
                pstmt.setString(11,  state.toUpperCase());
                pstmt.setString(12,  phone);
                pstmt.setString(13,  cell);
                pstmt.setString(14,  email);
                pstmt.setString(15,  fax);
                pstmt.setString(16,  amka);
                pstmt.setString(17,  afm);
                pstmt.setString(18,  doy.toUpperCase());
                pstmt.setString(19,  adt.toUpperCase());
                pstmt.setString(20,  ttcomments);
                pstmt.setString(21,  ptcomments);
                pstmt.setString(22,  ptstatus);
                pstmt.setString(23,  kdcomments);
                pstmt.setString(24,  kdstatus);
                pstmt.setString(25,  decisiondatekd);
                pstmt.setString(26,  applicationdate);
                pstmt.setString(27,  registrationdate);
                pstmt.setString(28,  cost);
                pstmt.setString(29,  annualcost);
                pstmt.setString(30,  friend);
                pstmt.setString(31,  remainingcost);
                pstmt.setString(32,  member_id);
            }else if(role.equals("pt")){
                pstmt = db_conn.prepareStatement(
                        "UPDATE members SET surname=?, name=?, fathersname=?, address=?, address_zipcode=?, address_area=?, address_municipality=?, address_county=?, "
                                + "city_id=?, address_state=?, phone=?, mobile_phone=?, email=?, fax=?, amka=?, afm=?, doy=?, identity_number=?, pt_notes=?, "
                                + "pt_forward=?, registration_fee=?, annual_membership=?, friend=?, remaining=? "
                                + "WHERE member_id=?"
                );

                pstmt.setString(1,  last_name);
                pstmt.setString(2,  first_name);
                pstmt.setString(3,  father_name);
                pstmt.setString(4,  address);
                pstmt.setString(5,  zip_code);
                pstmt.setString(6,  address_area);
                pstmt.setString(7,  municipality);
                pstmt.setString(8,  county);
                pstmt.setString(9,  department);
                pstmt.setString(10,  state);
                pstmt.setString(11,  phone);
                pstmt.setString(12,  cell);
                pstmt.setString(13,  email);
                pstmt.setString(14,  fax);
                pstmt.setString(15,  amka);
                pstmt.setString(16,  afm);
                pstmt.setString(17,  doy);
                pstmt.setString(18,  adt);
                pstmt.setString(19,  ptcomments);
                pstmt.setString(20,  ptstatus);
                pstmt.setString(21,  cost);
                pstmt.setString(22,  annualcost);
                pstmt.setString(23,  friend);
                pstmt.setString(24,  remainingcost);
                pstmt.setString(25,  member_id);
            }else if(role.equals("tt")){
                pstmt = db_conn.prepareStatement(
                        "UPDATE members SET surname=?, name=?, fathersname=?, address=?, address_zipcode=?, address_area=?, address_municipality=?, address_county=?, "
                                + "city_id=?, address_state=?, phone=?, mobile_phone=?, email=?, fax=?, amka=?, afm=?, doy=?, identity_number=?, tt_notes=?, "
                                + "registration_fee=?, annual_membership=?, friend=?, remaining=? "
                                + "WHERE member_id=?"
                );

                pstmt.setString(1,  last_name);
                pstmt.setString(2,  first_name);
                pstmt.setString(3,  father_name);
                pstmt.setString(4,  address);
                pstmt.setString(5,  zip_code);
                pstmt.setString(6,  address_area);
                pstmt.setString(7,  municipality);
                pstmt.setString(8,  county);
                pstmt.setString(9,  department);
                pstmt.setString(10,  state);
                pstmt.setString(11,  phone);
                pstmt.setString(12,  cell);
                pstmt.setString(13,  email);
                pstmt.setString(14,  fax);
                pstmt.setString(15,  amka);
                pstmt.setString(16,  afm);
                pstmt.setString(17,  doy);
                pstmt.setString(18,  adt);
                pstmt.setString(19,  ttcomments);
                pstmt.setString(20,  cost);
                pstmt.setString(21,  annualcost);
                pstmt.setString(22,  friend);
                pstmt.setString(23,  remainingcost);
                pstmt.setString(24,  member_id);
            }
            
            pstmt.executeUpdate();

            result = "insert ok";
        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
        
        return result;
    }
    
    @Override
    public String deleteMember(String member_id) throws DataSourceException{
        
        String result = "unknown";
        
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "DELETE FROM members WHERE member_id=?"
            );

            pstmt.setString(1, member_id);
            pstmt.executeUpdate();

            result = "delete ok";
        } catch (SQLException ex) {
            throw new DataSourceException(ex);
        }
        
        return result;
    }

    @Override
    public String newYearRemainingUpdate() throws DataSourceException{
        String result="unknown";
        
        try{
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT member_id, remaining FROM members");

            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();
            
            int count = 0;
            
            ArrayList<Subscription_Type> subscription_types = dao_factory.getSubscriptionTypeDAO().getAllSubscriptionTypes();
                    
            double additional_cost = 0;

            for (Subscription_Type subscription_type : subscription_types) {
                if(subscription_type.getType().equals("Συνδρομή ανά έτος")){
                    additional_cost = Double.parseDouble(subscription_type.getCost());
                }
            }
            
            while (rs.next()) {
                try{
                    double remaining = Double.parseDouble(rs.getString("remaining"));
                    
                    remaining+=additional_cost;
                    
                    pstmt = db_conn.prepareStatement("UPDATE members SET remaining=? "
                                                        + "WHERE member_id=?");

                    pstmt.setString(1,  remaining+"");
                    pstmt.setString(2,  rs.getString("member_id"));
                    
                    pstmt.executeUpdate();
                    
                    count++;
                }catch(Exception e){}
            }
            
            result = count+"";
        }catch(Exception e){}
        
        return result;
    }
    
    @Override
    public String increaseRemaining() throws DataSourceException{
        String result="unknown";
        
        try{
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT member_id, last_paid_year, remaining FROM members");

            ResultSet rs = pstmt.executeQuery();

            ArrayList<Member> members = new ArrayList<Member>();
            
            int count = 0;
            
            ArrayList<Subscription_Type> subscription_types = dao_factory.getSubscriptionTypeDAO().getAllSubscriptionTypes();
                    
            double additional_cost = 0;

            for (Subscription_Type subscription_type : subscription_types) {
                if(subscription_type.getType().equals("Συνδρομή ανά έτος")){
                    additional_cost = Double.parseDouble(subscription_type.getCost());
                }
            }
            
            while (rs.next()) {
                try{
                    double remaining = Double.parseDouble(rs.getString("remaining"));
                    int last_paid_year = Integer.parseInt(rs.getString("last_paid_year"));
                    
                    remaining+=additional_cost;
                    last_paid_year--;
                    
                    pstmt = db_conn.prepareStatement("UPDATE members SET remaining=?, last_paid_year=? "
                                                        + "WHERE member_id=?");

                    pstmt.setString(1,  remaining+"");
                    pstmt.setString(2,  last_paid_year+"");
                    pstmt.setString(3,  rs.getString("member_id"));
                    
                    pstmt.executeUpdate();
                    
                    count++;
                }catch(Exception e){}
            }
            
            result = count+"";
        }catch(Exception e){}
        
        return result;
    }
         
    private final Connection db_conn;

}
