package controllers.settings;

import controllers.log.InsertLogRecord;
import controllers.main.*;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DAOFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import models.Department;
import models.Member;
import models.User;
import util.exceptions.DataSourceException;

public class SetDepartmentPassword extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String successInsert = "unknown";
        try{
            successInsert = (String)request.getAttribute("successInsert");

            if(successInsert==null){
                    successInsert = "unknown";
            }
        }catch(Exception e){}
        request.setAttribute("successInsert", successInsert);
        
        request.setAttribute("view_relative_path", "/settings/SetDepartmentPassword.jsp");
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
            dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        } catch (DataSourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        try {
            String username = request.getParameter("username");
            String newpassword = request.getParameter("newpassword");
            String newpasswordagain = request.getParameter("newpasswordagain");
            
            if(username==null){username="";}
            if(newpassword==null){newpassword="";}
            if(newpasswordagain==null){newpasswordagain="";}
            
            String testusername = dao_factory.getUserDAO().getUsername(username);
            
            if(!testusername.equals("not_available")){
                if(newpassword.equals(newpasswordagain)){
                    boolean result = dao_factory.getUserDAO().updateUser(username, newpassword);

                    if (result==true) {
                        successInsert = "yes";
                        InsertLogRecord.insert(user.getUsername(), "Καθορισμός κωδικού πρόσβασης χρήστη ("+username+")");
                    } else {
                        successInsert = "no";
                    }
                }else{
                    successInsert="two_different_new_passwords";
                }
            }else{
                successInsert="wrong_username";
            }
        } catch (Exception ex) {
            successInsert+=ex.toString();
        } finally {
            dao_factory.release();
        }
        
        request.setAttribute("successInsert", successInsert);
        
        doGet(request, response);
    }
}
