/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.main;

import dao.DAOFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Department;
import models.Member;
import models.Member_Economic;
import models.Subscription_Type;
import models.User;

/**
 *
 * @author mary
 */
public class ViewEconomics extends HttpServlet {
    
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
        
        String m_id = request.getParameter("memberid");
        if(m_id == null){
            m_id="";
        }
        
        DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        ArrayList<Member_Economic> member_economics = new ArrayList<Member_Economic>();
        ArrayList<Department> departments = null;
        ArrayList<Subscription_Type> subscription_types = null;
        Department department = new Department();
        String department_name="all";
        
        String specific_search= request.getParameter("search");
        
        if(specific_search==null){
            specific_search="select";
        }
        
        request.setAttribute("member_id", m_id);
        request.setAttribute("specific_search", specific_search);
        
        try {
            if(user.getRole().equals("pt")){
                departments = dao_factory.getDepartmentDAO().getPtAndTtsDepartments(user.getState_id());
                
                if(m_id.equals("")){
                    if(specific_search.equals("select")){
                        specific_search = "all";
                    }
                    
                    if(specific_search.equals("all")){
                        member_economics = dao_factory.getMemberEconomicDAO().getAllStateMemberEconomics(user.getState_id(), fromdate, todate);
                    }else{
                        String[] specific_search_parts = specific_search.split("\\.");
                        String state_id="";
                        String city_id="";

                        try{
                            state_id=specific_search_parts[0];
                            city_id=specific_search_parts[1];
                        }catch(Exception e){}

                        member_economics = dao_factory.getMemberEconomicDAO().getDepartmentMemberEconomics(state_id, city_id, fromdate, todate);
                        department = dao_factory.getDepartmentDAO().getDepartment(state_id, city_id);
                        department_name = department.getName();
                    }
                }else{
                    Member member = dao_factory.getMemberDAO().getStateMember(m_id, user.getState_id());
                    
                    if(!member.getMember_id().equals("")){
                        member_economics = dao_factory.getMemberEconomicDAO().getMemberEconomics(member.getMember_id());
                    }
                }
            }else if(user.getRole().equals("tt")){
                departments = dao_factory.getDepartmentDAO().getTtDepartment(user.getState_id(), user.getCity_id());
                if(m_id.equals("")){
                    if(departments.size()>0){
                        department = departments.get(0);
                        department_name = department.getName();
                    }
                    member_economics = dao_factory.getMemberEconomicDAO().getDepartmentMemberEconomics(user.getState_id(), user.getCity_id(), fromdate, todate);
                }else{
                    Member member = dao_factory.getMemberDAO().getDepartmentMember(m_id, user.getState_id(), user.getCity_id());
                    
                    if(!member.getMember_id().equals("")){
                        member_economics = dao_factory.getMemberEconomicDAO().getMemberEconomics(member.getMember_id());
                    }
                }
            }else if(user.getRole().equals("kd") || user.getRole().equals("su")){
                departments = dao_factory.getDepartmentDAO().getAllDepartments();
                
                if(m_id.equals("")){
                    if(specific_search.equals("all")){
                        member_economics = dao_factory.getMemberEconomicDAO().getAllMemberEconomics(fromdate, todate);
                    }else if(specific_search.equals("select")){
                        member_economics = new ArrayList<Member_Economic>();
                        department_name="select";
                    }else{
                        String[] specific_search_parts = specific_search.split("\\.");
                        String state_id="";
                        String city_id="";

                        try{
                            state_id=specific_search_parts[0];
                            city_id=specific_search_parts[1];
                        }catch(Exception e){}
                        member_economics = dao_factory.getMemberEconomicDAO().getDepartmentMemberEconomics(state_id, city_id, fromdate, todate);
                        department = dao_factory.getDepartmentDAO().getDepartment(state_id, city_id);
                        department_name = department.getName();
                    }
                }else{
                    Member member = dao_factory.getMemberDAO().getMember(m_id);
                    
                    if(!member.getMember_id().equals("")){
                        member_economics = dao_factory.getMemberEconomicDAO().getMemberEconomics(member.getMember_id());
                    }
                }
            }
            subscription_types = dao_factory.getSubscriptionTypeDAO().getAllSubscriptionTypes();
        } catch(Exception e){
        } finally {
            dao_factory.release();
        }
        
        double total_paid = 0;
        for (Member_Economic member_economic : member_economics) {
            for (Subscription_Type subscription_type : subscription_types) {
                if(member_economic.getSubscription_type().equals(subscription_type.getType())){
                    try{
                        total_paid+=Integer.parseInt(subscription_type.getCost());
                        member_economic.setPaid(Integer.parseInt(subscription_type.getCost()));
                    }catch(Exception e){}
                }
            }
        }
        
        request.setAttribute("fromdate", fromdate);
        request.setAttribute("todate", todate);
        request.setAttribute("total_paid", total_paid+"");
        request.setAttribute("member_economics", member_economics);
        request.setAttribute("departments", departments);
        request.setAttribute("department", department);
        request.setAttribute("department_name", department_name);
        request.setAttribute("pending", "no");

        request.setAttribute("view_relative_path", "ViewEconomics.jsp");
        RequestDispatcher request_dispatcher = request.getRequestDispatcher("/main/MainViewWrapper.jsp");
        request_dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
    }
}
