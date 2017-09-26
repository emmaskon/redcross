package controllers.log;

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
import models.Log;
import models.User;

public class ViewLog extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        Date date=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH)+1;
        String monthStr = month+"";
        int year = cal.get(Calendar.YEAR);

        if(month<10){
            monthStr = "0"+monthStr;
        }

        int dayscount = cal.getActualMaximum(Calendar.DAY_OF_MONTH); 
        
        String fromdate = request.getParameter("fromdate");
        if(fromdate==null){
            fromdate = "01/"+monthStr+"/"+year;
        }
        
        String todate = request.getParameter("todate");
        if(todate==null){
            todate = dayscount+"/"+monthStr+"/"+year;
        }
        
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        ArrayList<Log> logs = null;
        
        try {
            if(user.getRole().equals("su")){
                logs = dao_factory.getLogDAO().getLog(fromdate, todate);
            }else{
                logs = new ArrayList<Log>();
            }
        } catch(Exception e){
        } finally {
            dao_factory.release();
        }

        request.setAttribute("fromdate", fromdate);
        request.setAttribute("todate", todate);
        request.setAttribute("logs", logs);

        request.setAttribute("view_relative_path", "/log/ViewLog.jsp");
        RequestDispatcher request_dispatcher = request.getRequestDispatcher("/main/MainViewWrapper.jsp");
        request_dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
    }
}
