package controllers;

import controllers.log.InsertLogRecord;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;

public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = (User) request.getSession().getAttribute("user");
        InsertLogRecord.insert(user.getUsername(), "Αποσύνδεση");
        
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath());
    }
}
