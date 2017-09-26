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

public class SearchMember extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        
        ArrayList<Department> departments = null;
        ArrayList<State> states = null;
        ArrayList<County> counties = null;
        ArrayList<Doy> doys = null;
        ArrayList<Municipality> municipalities = null;
        ArrayList<Zipcode> zipcodes = null;
        ArrayList<Job> jobs = null;

        try {
            if(user.getRole().equals("pt")){
                departments = dao_factory.getDepartmentDAO().getPtAndTtsDepartments(user.getState_id());
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

            doys = dao_factory.getDoyDAO().getAllDoys();
            municipalities = dao_factory.getMunicipalityDAO().getAllMunicipalities();
            zipcodes = dao_factory.getZipcodeDAO().getAllZipcodes();
            jobs = dao_factory.getJobDAO().getAllJobs();
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
                
        request.setAttribute("view_relative_path", "SearchMember.jsp");
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

        try {
            String member_id = request.getParameter("member_id");
            String last_name = request.getParameter("last-name");
            String first_name = request.getParameter("first-name");
            String father_name = request.getParameter("father-name");
            String amka = request.getParameter("amka");
            String afm = request.getParameter("afm");
            String doy = request.getParameter("doy");
            String adt = request.getParameter("adt");
            String job = request.getParameter("job");
            String address = request.getParameter("address");
            String zip_code = request.getParameter("zip-code");
            String address_area = request.getParameter("area");
            String municipality = request.getParameter("municipality");
            String county = request.getParameter("county");
            String state = request.getParameter("state");
            String phone = request.getParameter("phone");
            String cell = request.getParameter("cell");
            String email = request.getParameter("email");
            String fax = request.getParameter("fax");
            String department_code = request.getParameter("department");
            String applicationdate = request.getParameter("applicationdate");
            String ptstatus = request.getParameter("ptstatus");
            String kdstatus = request.getParameter("kdstatus");
            String decisiondatekd = request.getParameter("decisiondatekd");
            String remainingcost = request.getParameter("remainingcost");

            if(member_id==null){member_id="";}
            if(last_name==null){last_name="";}
            if(first_name==null){first_name="";}
            if(father_name==null){father_name="";}
            if(amka==null){amka="";}
            if(afm==null){afm="";}
            if(doy==null){doy="";}
            if(adt==null){adt="";}
            if(job==null){job="";}
            if(address==null){address="";}
            if(zip_code==null){zip_code="";}
            if(address_area==null){address_area="";}
            if(municipality==null){municipality="";}
            if(county==null){county="";}
            if(state==null){state="";}
            if(phone==null){phone="";}
            if(cell==null){cell="";}
            if(email==null){email="";}
            if(fax==null){fax="";}
            if(department_code==null){department_code="";}
            if(applicationdate==null){applicationdate="";}
            if(ptstatus==null){ptstatus="";}
            if(kdstatus==null){kdstatus="";}
            if(decisiondatekd==null){decisiondatekd="";}
            if(remainingcost==null){remainingcost="";}
            
            ArrayList<Member> members = null;
            ArrayList<Member> allmembers = null;
            ArrayList<Department> departments = null;
            Department department = new Department();
            String department_name="all";

            String dep_state="", dep_city="";
            if(!department_code.equals("")){
                String[] depcode_parts = department_code.split("\\.");
                try{
                    dep_state = depcode_parts[0];
                    dep_city = depcode_parts[1];
                    department = dao_factory.getDepartmentDAO().getDepartment(dep_state, dep_city);
                    department_name = department.getName();
                }catch(Exception e){}
            }
            
            String specific_search= request.getParameter("search");
            String specific_member_id= request.getParameter("member_id");
            String overtwoyears= request.getParameter("overtwoyears");

            if(specific_search==null){
                specific_search="select";
            }

            if(specific_member_id==null){
                specific_member_id="all";
            }

            if(overtwoyears==null){
                overtwoyears="no";
            }

            request.setAttribute("specific_search", specific_search);
            request.setAttribute("specific_member_id", specific_member_id);
            request.setAttribute("overtwoyears", overtwoyears);

            try {
                if(user.getRole().equals("pt")){
                    dep_state = user.getState_id();
                    departments = dao_factory.getDepartmentDAO().getPtAndTtsDepartments(user.getState_id());
                }else if(user.getRole().equals("tt")){
                    dep_state = user.getState_id();
                    dep_city = user.getCity_id();
                    departments = dao_factory.getDepartmentDAO().getTtDepartment(dep_state, dep_city);
                }else if(user.getRole().equals("kd") || user.getRole().equals("su")){
                    departments = dao_factory.getDepartmentDAO().getAllDepartments();
                }
                
                members = dao_factory.getMemberDAO().getMembers(member_id, last_name, first_name, father_name, 
                            amka, afm, doy, adt, job, address, zip_code, address_area, municipality, county, state, 
                            phone, cell, email, fax, dep_state, dep_city, applicationdate, ptstatus, kdstatus, 
                            decisiondatekd, remainingcost);
                allmembers = new ArrayList<Member>(members);
            } catch(Exception e){
            } finally {
                dao_factory.release();
            }

            double tobepaid=0;
            for (Member member : members) {
                try{
                    tobepaid += Double.parseDouble(member.getRemaining());
                }catch(Exception e){}
            }

            request.setAttribute("tobepaid", tobepaid+"");
            request.setAttribute("members", members);
            request.setAttribute("allmembers", allmembers);
            request.setAttribute("departments", departments);
            request.setAttribute("department", department);
            request.setAttribute("department_name", department_name);
            request.setAttribute("pending", "no");
            request.setAttribute("searchresult", "yes");

            request.setAttribute("view_relative_path", "ViewAllMembers.jsp");
            RequestDispatcher request_dispatcher = request.getRequestDispatcher("/main/MainViewWrapper.jsp");
            request_dispatcher.forward(request, response);
        } catch (Exception ex) {
        } finally {
            dao_factory.release();
        }
    }
}
