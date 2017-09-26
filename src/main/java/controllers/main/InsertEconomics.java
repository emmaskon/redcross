/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.main;

import controllers.log.InsertLogRecord;
import dao.DAOFactory;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import models.County;
import models.Department;
import javax.servlet.annotation.MultipartConfig;
import models.Member_Economic;
import models.Payment_Type;
import models.State;
import models.Subscription_Type;
import models.User;
import util.EnvironmentVariables;
import util.exceptions.DataSourceException;

/**
 *
 * @author unknown
 */

@MultipartConfig(location = "", maxFileSize = EnvironmentVariables.UPLOAD_MAX_FILESIZE)
public class InsertEconomics extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String edit_mode= "no";
        String existing_economic_record = request.getParameter("edit");
        
        if(existing_economic_record != null){
            edit_mode="yes";
        }else{
            existing_economic_record = (String)request.getAttribute("edit");
            
            if(existing_economic_record != null){
                edit_mode="yes";
            }else{
                existing_economic_record = "";
            }
        }
        
        String m_id = request.getParameter("memberid");
        if(m_id == null){
            m_id="";
        }
        String registration_payment="no";
        
        String[] reg_check=m_id.split("\\.");
        if(reg_check.length==1){
            registration_payment="yes";
        }else if(reg_check.length>1){
            m_id=reg_check[0];
            registration_payment="no";
        }
        
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        
        ArrayList<Department> departments = null;
        ArrayList<State> states = null;
        ArrayList<County> counties = null;
        ArrayList<Payment_Type> payment_types = null;
        ArrayList<Subscription_Type> subscription_types = null;
        
        String successInsert = "unknown";
        
        try{
            successInsert = (String)request.getAttribute("successInsert");

            if(successInsert==null){
                    successInsert = "unknown";
            }
        }catch(Exception e){}
        
        request.setAttribute("successInsert", successInsert);
        request.setAttribute("m_id", m_id);
        request.setAttribute("registration_payment", registration_payment);
        
        Member_Economic member_economic = new Member_Economic();
        
        try {
            if(edit_mode.equals("yes")){
                String[] parts = existing_economic_record.split("\\.");
                member_economic = dao_factory.getMemberEconomicDAO().getMemberEconomic(parts[0], parts[1]);
            }
            
            if(user.getRole().equals("pt")){
                if(edit_mode.equals("yes")){
                    departments = dao_factory.getDepartmentDAO().getPtAndTtsDepartments(user.getState_id());
                }else{
                    departments = dao_factory.getDepartmentDAO().getPtDepartment(user.getState_id(), user.getCity_id());
                }
                
                states = dao_factory.getStateDAO().getState(user.getState_id());
                counties = dao_factory.getCountyDAO().getCountiesForState(user.getState_id());
            }else if(user.getRole().equals("tt")){
                departments = dao_factory.getDepartmentDAO().getTtDepartment(user.getState_id(), user.getCity_id());
                states = dao_factory.getStateDAO().getState(user.getState_id());
                
                if(departments.size()>0){
                    counties = dao_factory.getCountyDAO().getCounty(departments.get(0).getCounty());
                }
                
            }else if(user.getRole().equals("kd") || user.getRole().equals("su")){
                departments = dao_factory.getDepartmentDAO().getAllDepartments();
                states = dao_factory.getStateDAO().getAllStates();
                counties = dao_factory.getCountyDAO().getAllCounties();
            }
            
            payment_types = dao_factory.getPaymentTypeDAO().getAllPaymentTypes();
            subscription_types = dao_factory.getSubscriptionTypeDAO().getAllSubscriptionTypes();
        } catch(Exception e){
        } finally {
            dao_factory.release();
        }
        
        request.setAttribute("member_economic", member_economic);
        request.setAttribute("departments", departments);
        request.setAttribute("states", states);
        request.setAttribute("counties", counties);
        request.setAttribute("edit_mode", edit_mode);
        request.setAttribute("payment_types", payment_types);
        request.setAttribute("subscription_types", subscription_types);
        
        /*if(member==null){
            member = new Member();
        }
        
        request.setAttribute("member", member);*/
                
        request.setAttribute("view_relative_path", "InsertEconomics.jsp");
        RequestDispatcher request_dispatcher = request.getRequestDispatcher("/main/MainViewWrapper.jsp");
        request_dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        DAOFactory dao_factory = null;
        
        try {
            dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        } catch (DataSourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        String successInsert = "unknown";
        String edit = null;

        try {
            String last_name = null;
            String first_name = null;
            String member_id = null;
            String subscription_type = null;
            String old_subscription_type = null;
            String subscription_year = null;
            String payment_type = null;
            String payment_date = null;
            String voucher_filepath = null;
            String invoice = null;
            String old_invoice = null;
            String old_voucher_file_id = null;
            String edit_mode = null;
            
            try{ last_name = new Scanner(request.getPart("last-name").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ first_name = new Scanner(request.getPart("first-name").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ member_id = new Scanner(request.getPart("member_id").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ subscription_type = new Scanner(request.getPart("subscription-type").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ old_subscription_type = new Scanner(request.getPart("old_subscription_type").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ subscription_year = new Scanner(request.getPart("subscription-year").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ payment_type = new Scanner(request.getPart("payment-type").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ payment_date = new Scanner(request.getPart("payment-date").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ voucher_filepath = new Scanner(request.getPart("voucher_filepath").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ invoice = new Scanner(request.getPart("invoice").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ old_invoice = new Scanner(request.getPart("old_invoice").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ old_voucher_file_id = new Scanner(request.getPart("old_voucher_file_id").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            try{ edit_mode = new Scanner(request.getPart("edit_mode").getInputStream(), "UTF-8").nextLine(); }catch(Exception e){}
            
            if(last_name==null){last_name="";}
            if(first_name==null){first_name="";}
            if(member_id==null){member_id="";}
            if(subscription_type==null){subscription_type="";}
            if(old_subscription_type==null){old_subscription_type="";}
            if(payment_type==null){payment_type="";}
            if(payment_date==null){payment_date="";}
            if(voucher_filepath==null){voucher_filepath="";}
            if(invoice==null){invoice="";}
            if(old_invoice==null){old_invoice="";}
            if(old_voucher_file_id==null){old_voucher_file_id="0";}
            if(edit_mode==null){edit_mode="no";}
            
            Part voucher_file = null;
            
            try {
                voucher_file = request.getPart("voucher_file");
            } catch (Exception ex) {
                //successInsert+=ex.toString();
            }
            
            String result = "";
            
            long voucher_file_id = 0;
            String voucher_file_type = "";
            
            if(payment_type.equals("Μέσω Τραπέζης") && !voucher_filepath.equals("")){
                if (voucher_file != null) {
                    try {
                        if(edit_mode.equals("yes")){
                            try{
                                if(Long.parseLong(old_voucher_file_id)>0){ //a voucher exists
                                    voucher_file_id = Long.parseLong(old_voucher_file_id);
                                }else{
                                    voucher_file_id = dao_factory.getMemberEconomicDAO().getMaxVoucherFileId()+1;
                                }
                            }catch(Exception e){
                                voucher_file_id = dao_factory.getMemberEconomicDAO().getMaxVoucherFileId()+1;
                            }
                        }else{
                            voucher_file_id = dao_factory.getMemberEconomicDAO().getMaxVoucherFileId()+1;
                        }
                        
                        String filepath_parts[] = voucher_filepath.split("\\.");
                        voucher_file_type = filepath_parts[filepath_parts.length-1];
                        
                        voucher_file.write(EnvironmentVariables.STORAGE_DIRECTORY + File.separator + voucher_file_id+"."+voucher_file_type);
                    } catch (Exception ex) {
                        successInsert+=ex.toString();
                    }
                }else{
                    successInsert+="voucher_file_null";
                }
            }else{
                try{
                    if(edit_mode.equals("yes") && Long.parseLong(old_voucher_file_id)>0){
                        voucher_file_id = 0;
                    }
                }catch(Exception e){}
            }
            
            if(edit_mode.equals("yes")){
                result = dao_factory.getMemberEconomicDAO().UpdateMemberEconomic(member_id, old_invoice, old_subscription_type, payment_type, payment_date, voucher_file_id, voucher_file_type, invoice, subscription_type);
                InsertLogRecord.insert(user.getUsername(), "Επεξεργασία οικονομικών στοιχείων μέλους (Κωδικός μέλους: "+member_id+", Αριθμός παραστατικού: "+invoice+")");
                edit = member_id+"."+invoice;
            }else{
                if(invoice.equals("")){
                    invoice="auto"+member_id+getCurrentDateTime();
                }
                
                result = dao_factory.getMemberEconomicDAO().InsertMemberEconomic(member_id, payment_type, payment_date, voucher_file_id, voucher_file_type, invoice, subscription_type, subscription_year);
                InsertLogRecord.insert(user.getUsername(), "Εισαγωγή οικονομικών στοιχείων μέλους (Κωδικός μέλους: "+member_id+", Αριθμός παραστατικού: "+invoice+")");
            }
            
            if (result.equals("insert ok")) {
                successInsert = "yes";
            } else {
                successInsert = "no";
            }
            
        } catch (Exception ex) {
            successInsert+=ex.toString();
        } finally {
            dao_factory.release();
        }
        
        request.setAttribute("successInsert", successInsert);
        request.setAttribute("edit", edit);

        doGet(request, response);
    }
    
    String getCurrentDateTime(){
        Date date=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dayStr = day+"";
        int month = cal.get(Calendar.MONTH)+1;
        String monthStr = month+"";
        int year = cal.get(Calendar.YEAR);

        if(day<10){
            dayStr = "0"+dayStr;
        }

        if(month<10){
            monthStr = "0"+monthStr;
        }

        int minute = cal.get(Calendar.MINUTE);
        String minuteStr = minute+"";
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String hourStr = hour+"";

        if(minute<10){
            minuteStr = "0"+minuteStr;
        }

        if(hour<10){
            hourStr = "0"+hourStr;
        }

        String current_date = dayStr+monthStr+year;
        String current_time = hourStr+minuteStr;
        return current_date+current_time;
    }
}
