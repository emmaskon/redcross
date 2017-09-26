package controllers.settings;

import controllers.log.InsertLogRecord;
import java.io.UnsupportedEncodingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DAOFactory;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import models.Job;
import models.User;
import util.exceptions.DataSourceException;

public class EditJobs extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String successInsert = "unknown";
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        ArrayList<Job> jobs = null;
        
        try{
            successInsert = (String)request.getAttribute("successInsert");

            if(successInsert==null){
                    successInsert = "unknown";
            }
            
            jobs = dao_factory.getJobDAO().getAllJobs();
            
        }catch(Exception e){
        
        }finally{
            if (dao_factory != null) {
                try {
                    dao_factory.release();
                } catch (Exception ex) {
                    //Do nothing
                }
            }
        }
        
        request.setAttribute("successInsert", successInsert);
        request.setAttribute("jobs", jobs);
        
        request.setAttribute("view_relative_path", "/settings/EditJobs.jsp");
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
        
        try {
            String newjob = request.getParameter("newjob");
            String result = dao_factory.getJobDAO().addJob(newjob);
            
            if (result.equals("insert ok")) {
                successInsert = "yes";
            } else {
                successInsert = "no";
            }
        } catch (Exception ex) {
           
        } finally {
            dao_factory.release();
        }
        
        request.setAttribute("successInsert", successInsert);
        
        doGet(request, response);
    }
}
