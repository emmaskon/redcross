/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.main;

import dao.DAOFactory;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Member;
import util.exceptions.DataSourceException;

/**
 *
 * @author mary
 */
public class AfmLiveSearch extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        JsonWriter json_writer = null;
        DAOFactory dao_factory = null;

        try {
            response.setContentType("application/json; charset=utf-8");

            dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            Member member = dao_factory.getMemberDAO().getMemberUsingAfm(request.getParameter("afm"));
           
            JsonArrayBuilder json_array = Json.createArrayBuilder();
        
            String afm="", fullname="", department="", member_id="";
            
            try{
            	afm=member.getAfm().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "");
                fullname=member.getSurname().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "")+" "+member.getName().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "");
                department=member.getDepartment_type() +" "+member.getDepartment_name().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "");
                member_id=member.getMember_id().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "");;
            }catch(Exception e){}
            
            
            if(afm==null){
            	afm="";
            }
            
            json_array.add(
                Json.createObjectBuilder()
                    .add("afm", afm)
                    .add("fullname", fullname)
                    .add("department", department)
                    .add("member_id", member_id)
            );
            

            json_writer = Json.createWriter(response.getOutputStream());
            json_writer.writeObject(Json.createObjectBuilder().add("member", json_array).build());

        } catch (Exception ex) {
            //
        } finally {
            try {
                if (dao_factory != null) {
                    dao_factory.release();
                }

                if (json_writer != null) {
                    json_writer.close();
                }

            } catch (Exception ex) {
//Do nothing
            }
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) {
    	processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws DataSourceException {
    	processRequest(request, response);
    }
}
