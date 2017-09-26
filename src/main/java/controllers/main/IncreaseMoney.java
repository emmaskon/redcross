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

//This claas is used only by the developer
public class IncreaseMoney extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
     
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
         
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if(user.getRole().equals("su")){
            try{
                DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
                String result = dao_factory.getMemberDAO().increaseRemaining();
                
                if (dao_factory != null) {
                    try {
                        dao_factory.release();
                    } catch (Exception ex) {
                        //Do nothing
                    }
                }
            }catch(Exception e){}
        }
        
        doGet(request, response);
    }
}
