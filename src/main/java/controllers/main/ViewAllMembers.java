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

public class ViewAllMembers extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        ArrayList<Member> members = null;
        ArrayList<Member> allmembers = null;
        ArrayList<Department> departments = null;
        Department department = new Department();
        String department_name="all";
        
        String specific_search= request.getParameter("search");
        String specific_member_id= request.getParameter("member_id");
        String overtwoyears= request.getParameter("overtwoyears");
        
        if(specific_search==null){
            specific_search="select";
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
                if(specific_search.equals("select")){
                    specific_search = "all";
                }
                
                departments = dao_factory.getDepartmentDAO().getPtAndTtsDepartments(user.getState_id());
                
                if(specific_search.equals("all")){
                    if(overtwoyears.equals("yes")){
                        members = dao_factory.getMemberDAO().getAllStateMembersNoPaidOrderedByName(user.getState_id(), 2);
                        allmembers = new ArrayList<Member>(members);
                    }else{
                        if(specific_member_id.equals("all")){
                            members = dao_factory.getMemberDAO().getAllStateMembersOrderedByName(user.getState_id());
                            allmembers = new ArrayList<Member>(members);
                        }else{
                            members = new ArrayList<Member>();
                            members.add(dao_factory.getMemberDAO().getStateMember(specific_member_id, user.getState_id()));
                            allmembers = dao_factory.getMemberDAO().getAllStateMembersOrderedByName(user.getState_id());
                        }
                    }
                }else{
                    String[] specific_search_parts = specific_search.split("\\.");
                    String state_id="";
                    String city_id="";

                    try{
                        state_id=specific_search_parts[0];
                        city_id=specific_search_parts[1];
                    }catch(Exception e){}

                    if(overtwoyears.equals("yes")){
                        members = dao_factory.getMemberDAO().getDepartmentMembersNoPaidOrderedByName(state_id, city_id, 2);
                        allmembers = new ArrayList<Member>(members);
                    }else{
                        if(specific_member_id.equals("all")){
                            members = dao_factory.getMemberDAO().getDepartmentMembersOrderedByName(state_id, city_id);
                            allmembers = new ArrayList<Member>(members);
                        }else{
                            members = new ArrayList<Member>();
                            members.add(dao_factory.getMemberDAO().getDepartmentMember(specific_member_id, state_id, city_id));
                            allmembers = dao_factory.getMemberDAO().getDepartmentMembersOrderedByName(state_id, city_id);
                        }
                    }
                    department = dao_factory.getDepartmentDAO().getDepartment(state_id, city_id);
                    department_name = department.getName();
                }
            }else if(user.getRole().equals("tt")){
                departments = dao_factory.getDepartmentDAO().getTtDepartment(user.getState_id(), user.getCity_id());
                if(departments.size()>0){
                    department = departments.get(0);
                    department_name = department.getName();
                }
                if(overtwoyears.equals("yes")){
                    members = dao_factory.getMemberDAO().getDepartmentMembersNoPaidOrderedByName(user.getState_id(), user.getCity_id(), 2);
                    allmembers = new ArrayList<Member>(members);
                }else{
                    if(specific_member_id.equals("all")){
                        members = dao_factory.getMemberDAO().getDepartmentMembersOrderedByName(user.getState_id(), user.getCity_id());
                        allmembers = new ArrayList<Member>(members);
                    }else{
                        members = new ArrayList<Member>();
                        members.add(dao_factory.getMemberDAO().getDepartmentMember(specific_member_id, user.getState_id(), user.getCity_id()));
                        allmembers = dao_factory.getMemberDAO().getDepartmentMembersOrderedByName(user.getState_id(), user.getCity_id());
                    }
                }
            }else if(user.getRole().equals("kd") || user.getRole().equals("su")){
                departments = dao_factory.getDepartmentDAO().getAllDepartments();
                
                if(specific_search.equals("all")){
                    if(overtwoyears.equals("yes")){
                        members = dao_factory.getMemberDAO().getAllMembersNoPaidOrderedByName(2);
                        allmembers = new ArrayList<Member>(members);
                    }else{
                        if(specific_member_id.equals("all")){
                            members = dao_factory.getMemberDAO().getAllMembersOrderedByName();
                            allmembers = new ArrayList<Member>(members);
                        }else{
                            members = new ArrayList<Member>();
                            members.add(dao_factory.getMemberDAO().getMember(specific_member_id));
                            allmembers = dao_factory.getMemberDAO().getAllMembersOrderedByName();
                        }
                    }
                }else if(specific_search.equals("select")){
                    members = new ArrayList<Member>();
                    allmembers = new ArrayList<Member>();
                    department_name="select";
                }else{
                    String[] specific_search_parts = specific_search.split("\\.");
                    String state_id="";
                    String city_id="";

                    try{
                        state_id=specific_search_parts[0];
                        city_id=specific_search_parts[1];
                    }catch(Exception e){}

                    if(overtwoyears.equals("yes")){
                        members = dao_factory.getMemberDAO().getDepartmentMembersNoPaidOrderedByName(state_id, city_id, 2);
                        allmembers = new ArrayList<Member>(members);
                    }else{
                        if(specific_member_id.equals("all")){
                            members = dao_factory.getMemberDAO().getDepartmentMembersOrderedByName(state_id, city_id);
                            allmembers = new ArrayList<Member>(members);
                        }else{
                            members = new ArrayList<Member>();
                            members.add(dao_factory.getMemberDAO().getDepartmentMember(specific_member_id, state_id, city_id));
                            allmembers = dao_factory.getMemberDAO().getDepartmentMembersOrderedByName(state_id, city_id);
                        }
                    }
                    department = dao_factory.getDepartmentDAO().getDepartment(state_id, city_id);
                    department_name = department.getName();
                }
            }
        } catch(Exception e){
        } finally {
            dao_factory.release();
        }
        
        double tobepaid=0;
        for (Member member : members) {
            try{
                tobepaid += Double.parseDouble(member.getRemaining());
            }catch(Exception e){}
        }
        
        request.setAttribute("tobepaid", tobepaid+"");
        request.setAttribute("members", members);
        request.setAttribute("allmembers", allmembers);
        request.setAttribute("departments", departments);
        request.setAttribute("department", department);
        request.setAttribute("department_name", department_name);
        request.setAttribute("pending", "no");
        request.setAttribute("searchresult", "no");

        request.setAttribute("view_relative_path", "ViewAllMembers.jsp");
        RequestDispatcher request_dispatcher = request.getRequestDispatcher("/main/MainViewWrapper.jsp");
        request_dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
    }
}
