package controllers.main;

import controllers.log.InsertLogRecord;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.DAOFactory;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import models.User;

public class DeleteMember extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String memberid = request.getParameter("memberid");

        JsonWriter json_writer = Json.createWriter(response.getOutputStream());
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

        try {
            String result = dao_factory.getMemberDAO().deleteMember(memberid);

            json_writer.write(Json.createObjectBuilder()
                    .add("status", result)
                    .build());

            json_writer.close();
            
            InsertLogRecord.insert(user.getUsername(), "Διαγραφή μέλους: "+memberid);
        } finally {
            dao_factory.release();
        }
    }
}
