package controllers;

import dao.DAOFactory;
import controllers.log.InsertLogRecord;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Annual_Scheduler;
import models.County;
import models.Department;
import models.Doy;
import models.Job;
import models.Municipality;
import models.State;
import util.EnvironmentVariables;
import models.User;
import models.Zipcode;
import util.GoogleRecaptcha;
import util.exceptions.InvalidRequestException;

public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher request_dispatcher = null;
        DAOFactory dao_factory = null;
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user != null) {//already logged in
                request_dispatcher = request.getRequestDispatcher("/admin/AdminViewWrapper.jsp");
            } else {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String user_recaptcha_response = request.getParameter("g-recaptcha-response");

                /*Check if request is not valid*/
                if (username == null || password == null) {
                    throw new InvalidRequestException("An invalid request received from :" + request.getRemoteAddr());
                }
                //
                dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
                Boolean is_recaptcha_enabled
                        = dao_factory.getSecurityOptionsDAO().isRecaptchaEnabled();
                
                Date date=new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int current_year = cal.get(Calendar.YEAR);
                int scheduler_year = current_year;

                try{
                    Annual_Scheduler annual_scheduler = dao_factory.getAnnualSchedulerDAO().getAnnualScheduler();
                    scheduler_year = Integer.parseInt(annual_scheduler.getYear());
                }catch(Exception e){}

                if ((is_recaptcha_enabled && (user_recaptcha_response == null || !GoogleRecaptcha.isRecaptchaCheckPassed(user_recaptcha_response)))
                        || (user = dao_factory.getUserDAO().getUser(username, password)) == null) {

                    if(current_year-scheduler_year>0){
                        request.setAttribute("newyear", Boolean.TRUE);
                    }else{
                        request.setAttribute("newyear", Boolean.FALSE);
                    }
                    request.setAttribute("login_retry", Boolean.TRUE);
                    request.setAttribute("is_recaptcha_enabled", is_recaptcha_enabled);
                    if (is_recaptcha_enabled) {
                        request.setAttribute("recaptcha_site_key", EnvironmentVariables.RECAPTCHA_SITE_KEY);
                    }

                    request_dispatcher = request.getRequestDispatcher("login.jsp");
                } else {
                    session.setAttribute("user", user);
                    InsertLogRecord.insert(user.getUsername(), "Σύνδεση");
                    
                    if(user.getUsername().equals("admin") && (current_year-scheduler_year>0)){
                        String result = dao_factory.getMemberDAO().newYearRemainingUpdate();
                        String result2 = dao_factory.getAnnualSchedulerDAO().updateAnnualScheduler(current_year, scheduler_year);
                    }
                    
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
                    
                    request.setAttribute("view_relative_path", "/main/SearchMember.jsp");
                    request_dispatcher = request.getRequestDispatcher("/main/MainViewWrapper.jsp");
                    
                }
            }
        } catch (Exception ex) {
            request.setAttribute("exception", ex);
            request_dispatcher = request.getRequestDispatcher("ErrorHandler");
        } finally {

            if (dao_factory != null) {
                try {
                    dao_factory.release();
                } catch (Exception ex) {
                    //Do nothing
                }
            }

            try {
                request_dispatcher.forward(request, response);
            } catch (Exception ex) {
                //Do nothing
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher request_dispatcher = null;
        DAOFactory dao_factory = null;
        HttpSession session = request.getSession();
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {//already logged in
                request_dispatcher = request.getRequestDispatcher("/main/MainViewWrapper.jsp");
            } else {
                dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
                
                Date date=new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int current_year = cal.get(Calendar.YEAR);
                int scheduler_year = current_year;

                try{
                    Annual_Scheduler annual_scheduler = dao_factory.getAnnualSchedulerDAO().getAnnualScheduler();
                    scheduler_year = Integer.parseInt(annual_scheduler.getYear());
                }catch(Exception e){}
                
                if(current_year-scheduler_year>0){
                    request.setAttribute("newyear", Boolean.TRUE);
                }else{
                    request.setAttribute("newyear", Boolean.FALSE);
                }
                
                Boolean is_recaptcha_enabled
                        = dao_factory.getSecurityOptionsDAO().isRecaptchaEnabled();
                request.setAttribute("login_retry", Boolean.FALSE);
                request.setAttribute("is_recaptcha_enabled", is_recaptcha_enabled);
                if (is_recaptcha_enabled) {
                    request.setAttribute("recaptcha_site_key", EnvironmentVariables.RECAPTCHA_SITE_KEY);
                }

                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
                request_dispatcher = request.getRequestDispatcher("login.jsp");
            }
        } catch (Exception ex) {

            request.setAttribute("exception", ex);
            request_dispatcher = request.getRequestDispatcher("ErrorHandler");

        } finally {
            if (dao_factory != null) {
                try {
                    dao_factory.release();
                } catch (Exception ex) {
                    //Do nothing
                }
            }

            try {
                request_dispatcher.forward(request, response);
            } catch (Exception ex) {
                //Do nothing
            }
        }
    }
}
