package controllers.settings;

import controllers.main.*;
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

public class DeleteMunicipality extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String municipalityid = request.getParameter("municipalityid").replaceAll("_", " ");

        JsonWriter json_writer = Json.createWriter(response.getOutputStream());
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

        try {
            String result =  "unknown";
            
            int members_count = dao_factory.getMemberDAO().getMembersCountWithMunicipality(municipalityid);
                    
            if(members_count <= 0){
                result = dao_factory.getMunicipalityDAO().deleteMunicipality(municipalityid);
                InsertLogRecord.insert(user.getUsername(), "Διαγραφή δήμου: "+municipalityid);
            }else{
                result = "member_exists";
            }

            json_writer.write(Json.createObjectBuilder()
                    .add("status", result)
                    .build());

            json_writer.close();
        } finally {
            dao_factory.release();
        }
    }
}
