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
public class MemberLiveSearch extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        JsonWriter json_writer = null;
        DAOFactory dao_factory = null;

        try {
            response.setContentType("application/json; charset=utf-8");

            dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            Member member = dao_factory.getMemberDAO().getMember(request.getParameter("member_id"));
            int max_paid_year = dao_factory.getMemberEconomicDAO().getMaxPaidSubscriptionYear(request.getParameter("member_id"));
            
            JsonArrayBuilder json_array = Json.createArrayBuilder();
        
            String name="", surname="";
            
            try{
            	name=member.getName();
            	surname=member.getSurname();
            }catch(Exception e){}
            
            
            if(name==null){
            	name="";
            }

            if(surname==null){
                surname="";
            }
            
            String year_to_be_paid="";
            
            if(max_paid_year!=-1){
                max_paid_year++;
                year_to_be_paid=max_paid_year+"";
            }

            json_array.add(
                Json.createObjectBuilder()
                    .add("name", name)
                    .add("surname", surname)
                    .add("year_to_be_paid", year_to_be_paid)
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
