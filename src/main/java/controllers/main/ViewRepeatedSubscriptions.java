package controllers.main;

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

public class ViewRepeatedSubscriptions extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        ArrayList<Member> members = null;
        ArrayList<Department> departments = null;
        Department department = new Department();
        String department_name="all";
        
        String specific_search= request.getParameter("search");
        String specific_member_id= request.getParameter("member_id");
        String overtwoyears= request.getParameter("overtwoyears");
        
        if(specific_search==null){
            specific_search="all";
        }
        
        if(specific_member_id==null){
            specific_member_id="all";
        }
        
        if(overtwoyears==null){
            overtwoyears="no";
        }
        
        request.setAttribute("specific_search", specific_search);
        request.setAttribute("specific_member_id", specific_member_id);
        request.setAttribute("overtwoyears", overtwoyears);
        
        try {
            if(user.getRole().equals("pt")){
                departments = dao_factory.getDepartmentDAO().getPtAndTtsDepartments(user.getState_id());
                
                if(specific_search.equals("all")){
                    members = dao_factory.getMemberDAO().getAllStateRepeatedMembersOrderedByName(user.getState_id());
                }else{
                    String[] specific_search_parts = specific_search.split("\\.");
                    String state_id="";
                    String city_id="";

                    try{
                        state_id=specific_search_parts[0];
                        city_id=specific_search_parts[1];
                    }catch(Exception e){}

                    members = dao_factory.getMemberDAO().getDepartmentRepeatedMembersOrderedByName(state_id, city_id, user.getRole());
                    department = dao_factory.getDepartmentDAO().getDepartment(state_id, city_id);
                    department_name = department.getName();
                }
            }else if(user.getRole().equals("tt")){
                departments = dao_factory.getDepartmentDAO().getTtDepartment(user.getState_id(), user.getCity_id());
                if(departments.size()>0){
                    department = departments.get(0);
                    department_name = department.getName();
                }
                members = dao_factory.getMemberDAO().getDepartmentRepeatedMembersOrderedByName(user.getState_id(), user.getCity_id(), user.getRole());
            }else if(user.getRole().equals("kd") || user.getRole().equals("su")){
                departments = dao_factory.getDepartmentDAO().getAllDepartments();
                
                if(specific_search.equals("all")){
                    members = dao_factory.getMemberDAO().getAllRepeatedMembersOrderedByName();
                }else if(specific_search.equals("select")){
                    members = new ArrayList<Member>();
                    department_name="select";
                }else{
                    String[] specific_search_parts = specific_search.split("\\.");
                    String state_id="";
                    String city_id="";

                    try{
                        state_id=specific_search_parts[0];
                        city_id=specific_search_parts[1];
                    }catch(Exception e){}

                    members = dao_factory.getMemberDAO().getDepartmentRepeatedMembersOrderedByName(state_id, city_id, user.getRole());
                    department = dao_factory.getDepartmentDAO().getDepartment(state_id, city_id);
                    department_name = department.getName();
                }
            }
        } catch(Exception e){
        } finally {
            dao_factory.release();
        }

        request.setAttribute("tobepaid", "");
        request.setAttribute("allmembers", members);
        request.setAttribute("members", members);
        request.setAttribute("departments", departments);
        request.setAttribute("department", department);
        request.setAttribute("department_name", department_name);
        request.setAttribute("pending", "repeated");
        request.setAttribute("searchresult", "no");

        request.setAttribute("view_relative_path", "ViewAllMembers.jsp");
        RequestDispatcher request_dispatcher = request.getRequestDispatcher("/main/MainViewWrapper.jsp");
        request_dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
    }
}
