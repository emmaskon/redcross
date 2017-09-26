package controllers.main;

import controllers.log.InsertLogRecord;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DAOFactory;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import models.County;
import models.Department;
import models.Doy;
import models.Job;
import models.Member;
import models.Municipality;
import models.State;
import models.Subscription_Type;
import models.User;
import models.Zipcode;
import util.exceptions.DataSourceException;

public class InsertMember extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String edit_mode= "no";
        String existing_member_id = request.getParameter("edit");
        
        if(existing_member_id != null){
            edit_mode="yes";
        }else{
            existing_member_id = (String)request.getAttribute("edit");
            
            if(existing_member_id != null){
                edit_mode="yes";
            }else{
                existing_member_id = "";
            }
        }
        
        String pending_status = request.getParameter("pending");
        
        if(pending_status == null){
            pending_status = (String)request.getAttribute("pending");
            
            if(pending_status == null){
                pending_status="no";
            }
        }

        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        
        ArrayList<Department> departments = null;
        ArrayList<State> states = null;
        ArrayList<County> counties = null;
        Member member = null;
        ArrayList<Subscription_Type> subscription_types = null;
        ArrayList<Doy> doys = null;
        ArrayList<Municipality> municipalities = null;
        ArrayList<Zipcode> zipcodes = null;
        ArrayList<Job> jobs = null;
        
        String successInsert = "unknown";
        String m_id = "unknown";
        
        try{
            successInsert = (String)request.getAttribute("successInsert");
            m_id = (String)request.getAttribute("m_id");
            
            if(successInsert==null){
                    successInsert = "unknown";
            }
            if(m_id==null){
                    m_id = "unknown";
            }
        }catch(Exception e){}
        
        request.setAttribute("successInsert", successInsert);
        request.setAttribute("m_id", m_id);
        
        try {
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
            
            subscription_types = dao_factory.getSubscriptionTypeDAO().getAllSubscriptionTypes();
            doys = dao_factory.getDoyDAO().getAllDoys();
            municipalities = dao_factory.getMunicipalityDAO().getAllMunicipalities();
            zipcodes = dao_factory.getZipcodeDAO().getAllZipcodes();
            jobs = dao_factory.getJobDAO().getAllJobs();
            member = dao_factory.getMemberDAO().getMember(existing_member_id);
        } catch(Exception e){
        } finally {
            dao_factory.release();
        }
        
        request.setAttribute("departments", departments);
        request.setAttribute("states", states);
        request.setAttribute("counties", counties);
        request.setAttribute("doys", doys);
        request.setAttribute("municipalities", municipalities);
        request.setAttribute("zipcodes", zipcodes);
        request.setAttribute("jobs", jobs);
        request.setAttribute("edit_mode", edit_mode);
        request.setAttribute("pending_status", pending_status);
        request.setAttribute("subscription_types", subscription_types);
        
        if(member==null){
            member = new Member();
        }
        
        request.setAttribute("member", member);
        
        Date date=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int current_year = cal.get(Calendar.YEAR);
        request.setAttribute("current_year", current_year+"");
                
        request.setAttribute("view_relative_path", "InsertMember.jsp");
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
        String m_id = "unknown";
        String edit = null;

        try {
            String last_name = request.getParameter("last-name");
            String first_name = request.getParameter("first-name");
            String father_name = request.getParameter("father-name");
            String job = request.getParameter("job"); 
            String address = request.getParameter("address");
            String zip_code = request.getParameter("zip-code");
            String address_area = request.getParameter("area");
            String municipality = request.getParameter("municipality");
            String county = request.getParameter("county");
            String phone = request.getParameter("phone");
            String cell = request.getParameter("cell");
            String email = request.getParameter("email");
            String fax = request.getParameter("fax");
            String amka = request.getParameter("amka");
            String afm = request.getParameter("afm");
            String doy = request.getParameter("doy");
            String adt = request.getParameter("adt");
            String state = request.getParameter("state");
            String edit_mode = request.getParameter("edit_mode");
            
            String department = "";
            String department_temp = request.getParameter("department");
            
            String member_id= request.getParameter("member_id");
            
            String max_id = dao_factory.getMemberDAO().getHighestMemberCode();
            int int_max_id = 0;
            
            try{
                int_max_id = Integer.parseInt(max_id)+1;
            }catch(Exception e){}
            
            max_id  = int_max_id+"";
            
            try{
                if(department_temp!=null){
                    String[] department_parts = department_temp.split("\\.");

                    if(department_parts.length >= 2){
                        department = department_parts[1];
                        
                        int zeros_for_padding = 9 - max_id.length();
                        String zeros="";
                        
                        for(int i=0; i<zeros_for_padding; i++){
                            zeros += "0";
                        }
                        
                        if(member_id.equals("na")){
                            member_id = department_parts[0]+department_parts[1]+zeros+max_id;
                        }
                        state = department_parts[0];
                    }
                }
            }catch(Exception e){}
            
            String ttcomments = request.getParameter("ttcomments");
            String ptcomments = request.getParameter("ptcomments");
            String kdcomments = request.getParameter("kdcomments");
            String ptstatus = request.getParameter("ptstatus");
            String kdstatus = request.getParameter("kdstatus");
            
            String decisiondatekd = request.getParameter("decisiondatekd");
            String applicationdate = request.getParameter("applicationdate");
            String registrationdate = request.getParameter("applicationdate");//registrationdate
            
            String cost = request.getParameter("cost");
            String annualcost = request.getParameter("annualcost");
            String remainingcost = request.getParameter("remainingcost");
            
            if(ttcomments==null){ttcomments="";}
            if(ptcomments==null){ptcomments="";}
            if(kdcomments==null){kdcomments="";}
            if(ptstatus==null){ptstatus="0";}
            if(kdstatus==null){kdstatus="0";}
            if(decisiondatekd==null){decisiondatekd="";}
            if(applicationdate==null){applicationdate="";}
            if(registrationdate==null){registrationdate="";}
            if(cost==null){cost="";}
            if(annualcost==null){annualcost="";}
            if(remainingcost==null){remainingcost="0";}
            
            if(kdstatus.equals("1")){ptstatus="1";}
            
            ArrayList<Subscription_Type> subscription_types = dao_factory.getSubscriptionTypeDAO().getAllSubscriptionTypes();
            String registration_cost="5", annual_cost="10";
            for (Subscription_Type subscription_type : subscription_types) {
                if(subscription_type.getType().equals("Εγγραφή")){
                    registration_cost = subscription_type.getCost();
                }else if(subscription_type.getType().equals("Συνδρομή ανά έτος")){
                    annual_cost = subscription_type.getCost();
                }
            }
            if(cost.equals("")){cost=registration_cost;}
            if(annualcost.equals("")){annualcost=annual_cost;}
            
            String friend="0";
            
            String result = "";

            Date date=new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            year--;
            String last_paid_year=year+"";
            
            if(edit_mode.equals("yes")){
                result = dao_factory.getMemberDAO().UpdateMember(user.getRole(),member_id, last_name, first_name, father_name, job, address, zip_code, 
                    address_area, municipality, county, phone, cell, email, fax, amka, afm, doy, adt, state, department, ttcomments, 
                    ptcomments, kdcomments, ptstatus, kdstatus, decisiondatekd, applicationdate, registrationdate, cost, annualcost, remainingcost, friend);
                
                InsertLogRecord.insert(user.getUsername(), "Επεξεργασία στοιχείων μέλους (Κωδικός μέλους: "+member_id+")");
                edit = member_id;
            }else{
                result = dao_factory.getMemberDAO().InsertMember(member_id, last_name, first_name, father_name, job, address, zip_code, 
                    address_area, municipality, county, phone, cell, email, fax, amka, afm, doy, adt, state, department, ttcomments, 
                    ptcomments, kdcomments, ptstatus, kdstatus, decisiondatekd, applicationdate, registrationdate, cost, annualcost, remainingcost, friend, last_paid_year);
                
                InsertLogRecord.insert(user.getUsername(), "Καταχώρηση στοιχείων μέλους (Κωδικός μέλους: "+member_id+")");
            }
            
            if (result.equals("insert ok")) {
                successInsert = "yes";
                m_id = member_id;
            } else {
                successInsert = "no"+result;
            }
        } catch (Exception ex) {
            successInsert+=ex.toString();
        } finally {
            dao_factory.release();
        }
        
        request.setAttribute("m_id", m_id);
        request.setAttribute("successInsert", successInsert);
        request.setAttribute("edit", edit);

        doGet(request, response);
    }
}
