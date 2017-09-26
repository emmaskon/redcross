<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@ page import="java.util.*" %>
<%@ page import="models.User" %>
<%@ page import="models.Department" %>
<%@ page import="models.State" %>
<%@ page import="models.County" %>
<%@ page import="models.Doy" %>
<%@ page import="models.Municipality" %>
<%@ page import="models.Zipcode" %>
<%@ page import="models.Job" %>
<%@ page import="models.Member" %>
<%@ page import="models.Subscription_Type"%>

<%
    ArrayList<Department> departments = (ArrayList<Department>) request.getAttribute("departments");
    ArrayList<State> states = (ArrayList<State>) request.getAttribute("states");
    ArrayList<County> counties = (ArrayList<County>) request.getAttribute("counties");
    ArrayList<Doy> doys = (ArrayList<Doy>) request.getAttribute("doys");
    ArrayList<Municipality> municipalities = (ArrayList<Municipality>) request.getAttribute("municipalities");
    ArrayList<Zipcode> zipcodes = (ArrayList<Zipcode>) request.getAttribute("zipcodes");
    ArrayList<Job> jobs = (ArrayList<Job>) request.getAttribute("jobs");
    //Department current_department = (Department) request.getAttribute("department");
    String successInsert=(String)request.getAttribute("successInsert");
    String m_id=(String)request.getAttribute("m_id");
    String edit_mode=(String)request.getAttribute("edit_mode");
    String pending_status=(String)request.getAttribute("pending_status");
    String current_year=(String)request.getAttribute("current_year");
    Member member=(Member)request.getAttribute("member");
    
    ArrayList<Subscription_Type> subscription_types = (ArrayList<Subscription_Type>) request.getAttribute("subscription_types");
    String registration_cost="", annual_cost="";
    for (Subscription_Type subscription_type : subscription_types) {
        if(subscription_type.getType().equals("Εγγραφή")){
            registration_cost = subscription_type.getCost();
        }else if(subscription_type.getType().equals("Συνδρομή ανά έτος")){
            annual_cost = subscription_type.getCost();
        }
    }
    
    User user = (User) session.getAttribute("user");
%>
<% if(successInsert.equals("yes")){ %>
    <div class="ui success message">
        <i class="close icon"></i>
        <div class="header">
        Επιτυχής εισαγωγή στοιχείων!
        <% if(edit_mode.equals("no")){ %> 
           &nbsp;&nbsp;<button id="submitmoney" onclick="submitMoney('<%=m_id %>')" class="ui green button">Καταχώρηση πληρωμής αίτησης</button> 
        <% }%>
        </div>
    </div>
<% } else if (!successInsert.equals("yes") && !successInsert.equals("unknown")) { %> 
    <div class="ui negative message">
        <i class="close icon"></i>
        <div class="header">
        Μη επιτυχής εισαγωγή στοιχείων. Παρακαλούμε εισάγετε σωστά τα δεδομένα.
        </div>
    </div>
<% } %> 
<div class="ui form segment">
    <h4 class="ui dividing header">
    <% 
       if(edit_mode.equals("yes") && member.getMember_id()!=null && member.getPt_forward()!=null && member.getKd_approval()!=null
                                  && (!member.getMember_id().equals("")) && (!member.getPt_forward().equals("")) && (!member.getKd_approval().equals(""))){ 
            if(member.getPt_forward().equals("1") && member.getKd_approval().equals("1")){
    %>
                Επεξεργασία Μέλους (<%=member.getMember_id() %>)
    <%      
            }else{
    %>
                Επεξεργασία Αίτησης (<%=member.getMember_id() %>)
    <%
            }
       }else { %>
        Προσθήκη Μέλους
    <% } %>
    </h4>
    <form action="InsertMember" method="post" id="insert-form">
        <input type="hidden" id="edit_mode" name="edit_mode" value="<%=edit_mode %>" >
        <input type="hidden" id="member_id" name="member_id"
            <% if(edit_mode.equals("yes") && (member.getMember_id()!=null)){ %>   
                value="<%=member.getMember_id() %>"
            <% } else { %>
                value="na"
            <% } %>
        >
        <input type="hidden" id="member_submitted_afm" name="member_submitted_afm"
            <% if(edit_mode.equals("yes") && (member.getAfm()!=null)){ %>   
                value="<%=member.getAfm() %>"
            <% } else { %>
                value="na"
            <% } %>
        >
        <div class="fields">
            <div class="four wide field">
                <label>Επίθετο</label>
                <input name="last-name" id="lastname" placeholder="Επίθετο" type="text" style="text-transform:uppercase;"
                    <% if(edit_mode.equals("yes") && (member.getSurname()!=null)){ %>   
                       value="<%=member.getSurname() %>"
                    <% } %>
                >
            </div>
            <div class="four wide field">
                <label>Όνομα</label>
                <input name="first-name" id="firstname" placeholder="Όνομα" type="text" style="text-transform:uppercase;"
                    <% if(edit_mode.equals("yes") && (member.getName()!=null)){ %>   
                       value="<%=member.getName() %>"
                    <% } %>
                >
            </div>
            <div class="four wide field">
                <label>Όνομα Πατρός</label>
                <input name="father-name" placeholder="Όνομα Πατρός" type="text" style="text-transform:uppercase;"
                    <% if(edit_mode.equals("yes") && (member.getFathersname()!=null)){ %>   
                       value="<%=member.getFathersname() %>"
                    <% } %>       
                >
            </div>
                <div class="four wide field">
                <label>Επάγγελμα</label>
                    <div class="ui action input" >
                        <input name="job" id="job" placeholder="Επάγγελμα" type="text" style="text-transform:uppercase;"
                            <% if(edit_mode.equals("yes") && (member.getJob()!=null)){ %>  
                                value="<%=member.getJob() %>"
                            <% } %>
                        >
                        <div class="ui right pointing dropdown icon button">
                            <input type="hidden" name="jobselect" id="jobselect">
                            <!--<div class="default text"></div>-->
                            <i class="dropdown icon"></i>
                            <div class="menu" >
                                <div class="ui icon search input" style="width:90%;">
                                    <i class="search icon"></i>
                                    <input style="width:100%;" placeholder="Πληκτρολογήστε το επάγγελμα..." type="text">
                                </div>
                                <%
                                    for (Job job : jobs) {
                                %>
                                <div class="item" data-value="<%=job.getName() %>"><%=job.getName() %></div>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
        <div class="fields">
            <div class="six wide field">
                <label>Διεύθυνση</label>
                <input name="address" placeholder="Διεύθυνση" type="text" style="text-transform:uppercase;"
                    <% if(edit_mode.equals("yes") && (member.getAddress()!=null)){ %>   
                       value="<%=member.getAddress() %>"
                    <% } %>         
                >
            </div>
            <div class="three wide field">
                <label>ΤΚ</label>
                <div class="ui action input">
                    <input name="zip-code" id="zipcode" placeholder="ΤΚ" type="text"
                        <% if(edit_mode.equals("yes") && (member.getAddress_zipcode()!=null)){ %>  
                            value="<%=member.getAddress_zipcode() %>"
                        <% } %>  
                    >
                    <div class="ui right pointing dropdown icon button">
                        <input type="hidden" name="zip-code-select" id="zipcodeselect">
                        <!--<div class="default text"></div>-->
                        <i class="dropdown icon"></i>
                        <div class="menu" style="overflow:auto;max-height:300px;">
                            <div class="ui icon search input" style="width:90%;">
                                <i class="search icon"></i>
                                <input style="width:100%;" placeholder="Πληκτρολογήστε τον ΤΚ..." type="text">
                            </div>
                            <%
                                for (Zipcode zipcode : zipcodes) {
                            %>
                            <div class="item" data-value="<%=zipcode.getZipcode() %>"><%=zipcode.getZipcode() %> <i style="color:gray;">(<%=zipcode.getArea() %>)</i></div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
            <div class="four wide field">
                <label>Περιοχή</label>
                <input name="area" placeholder="Περιοχή" type="text" style="text-transform:uppercase;"
                    <% if(edit_mode.equals("yes") && (member.getAddress_area()!=null)){ %>   
                       value="<%=member.getAddress_area() %>"
                    <% } %>          
                >
            </div>
            <div class="three wide field">
                <label>Δήμος</label>
                <div class="ui selection dropdown">
                    <input type="hidden" name="municipality" id="municipality" 
                        <% if(edit_mode.equals("yes") && (member.getAddress_municipality()!=null)){ %>  
                            value="<%=member.getAddress_municipality() %>"
                        <% } %>    
                    >
                    <div class="default text">ΔΉΜΟΣ</div>
                    <i class="dropdown icon"></i>
                    <div class="menu">
                        <div class="ui icon search input" style="width:90%;">
                            <i class="search icon"></i>
                            <input style="width:100%;" placeholder="Πληκτρολογήστε το δήμο..." type="text">
                        </div>
                        <%
                            for (Municipality municipality : municipalities) {
                        %>
                        <div class="item" data-value="<%=municipality.getName() %>"><%=municipality.getName() %></div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
        <div class="fields">
            <div class="four wide field">
                <label>Νομός</label>
                <div class="ui selection dropdown">
                    <input type="hidden" name="county" id="county" 
                        <% if(edit_mode.equals("yes") && (member.getAddress_county()!=null)){ %>  
                            value="<%=member.getAddress_county() %>"
                        <% } %>  
                    >
                    <div class="default text">ΝΟΜΌΣ</div>
                    <i class="dropdown icon"></i>
                    <div class="menu">
                        <div class="ui icon search input" style="width:90%;">
                            <i class="search icon"></i>
                            <input style="width:100%;" placeholder="Πληκτρολογήστε το νομό..." type="text">
                        </div>
                        <%
                            for (County county : counties) {
                        %>
                        <div class="item" data-value="<%=county.getName() %>"><%=county.getName() %></div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
            <div class="three wide field">
                <label>Τηλέφωνο</label>
                <input name="phone" placeholder="Τηλέφωνο" type="text"  style="text-transform:uppercase;"
                    <% if(edit_mode.equals("yes") && (member.getPhone()!=null)){ %>  
                       value="<%=member.getPhone() %>"
                    <% } %>         
                >
            </div>
            <div class="three wide field">
                <label>Κινητό</label>
                <input name="cell" placeholder="Κινητό" type="text" style="text-transform:uppercase;"
                    <% if(edit_mode.equals("yes") && (member.getMobile_phone()!=null)){ %>  
                       value="<%=member.getMobile_phone() %>"
                    <% } %>        
                >
            </div>
            <div class="three wide field">
                <label>Email</label>
                <input name="email" placeholder="EMAIL" type="text"
                    <% if(edit_mode.equals("yes") && (member.getEmail()!=null)){ %>  
                       value="<%=member.getEmail() %>"
                    <% } %>          
                >
            </div>
            <div class="three wide field">
                <label>Fax</label>
                <input name="fax" placeholder="Fax" type="text" style="text-transform:uppercase;"
                    <% if(edit_mode.equals("yes") && (member.getFax()!=null)){ %>  
                       value="<%=member.getFax() %>"
                    <% } %>        
                >
            </div>
        </div>
        <div class="fields">
            <div class="four wide field">
                <label>ΑΜΚΑ</label>
                <input name="amka" id="amka" placeholder="ΑΜΚΑ" type="text"
                    <% if(edit_mode.equals("yes") && (member.getAmka()!=null)){ %>  
                       value="<%=member.getAmka() %>"
                    <% } %>         
                >
            </div>
            <div class="four wide field" id="afm_div">
                <label>ΑΦΜ</label>
                <input class="afm_exists" name="afm" id="afm" placeholder="ΑΦΜ" type="text"
                    <% if(edit_mode.equals("yes") && (member.getAfm()!=null)){ %>  
                       value="<%=member.getAfm() %>"
                    <% } %>        
                >
                <div class="ui flowing popup top left transition hidden yellow" id="afm_exists_warning" style="display:none"></div>
            </div>
            <div class="four wide field">
                <label>ΔΟΥ</label>
                <div class="ui selection dropdown">
                    <input type="hidden" name="doy" id="doy" 
                        <% if(edit_mode.equals("yes") && (member.getDoy()!=null)){ %>  
                            value="<%=member.getDoy() %>"
                        <% } %>   
                    >
                    <div class="default text">ΔΟΥ</div>
                    <i class="dropdown icon"></i>
                    <div class="menu">
                        <div class="ui icon search input" style="width:90%;">
                            <i class="search icon"></i>
                            <input style="width:100%;" placeholder="Πληκτρολογήστε το όνομα της ΔΟΥ..." type="text">
                        </div>
                        <%
                            for (Doy doy : doys) {
                        %>
                        <div class="item" data-value="<%=doy.getName() %>"><%=doy.getName() %></div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
            <div class="four wide field">
                <label>ΑΔΤ</label>
                <input name="adt" placeholder="ΑΔΤ" type="text"  style="text-transform:uppercase;"
                    <% if(edit_mode.equals("yes") && (member.getIdentity_number()!=null)){ %>  
                       value="<%=member.getIdentity_number() %>"
                    <% } %>        
                >
            </div>
        </div>
        <div class="fields">
            <div class="eight wide field">
                <label>Περιφέρεια</label>
                <div class="ui selection dropdown">
                    <input type="hidden" name="state" id="state" 
                        <% if(edit_mode.equals("yes") && (member.getAddress_state()!=null)){ %>  
                            value="<%=member.getAddress_state() %>"
                        <% } %>    
                    >
                    <div class="default text">ΠΕΡΙΦΈΡΕΙΑ</div>
                    <i class="dropdown icon"></i>
                    <div class="menu">
                        <div class="ui icon search input" style="width:90%;">
                            <i class="search icon"></i>
                            <input style="width:100%;" placeholder="Πληκτρολογήστε την περιφέρεια..." type="text">
                        </div>
                        <%
                            for (State state : states) {
                        %>
                        <div class="item" data-value="<%=state.getId() %>"><%=state.getState_name() %></div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
            <div class="eight wide field">
                <label>Τμήμα</label>
                <div class="ui selection dropdown">
                    <input type="hidden" name="department" id="department" 
                        <% if(edit_mode.equals("yes") && (member.getAddress_state()!=null) && (member.getCity_id()!=null)){ %>  
                            value="<%=member.getAddress_state() %>.<%=member.getCity_id() %>"
                        <% } %>        
                    >
                    <div class="default text">ΤΜΉΜΑ</div>
                    <i class="dropdown icon"></i>
                    <div class="menu">
                        <div class="ui icon search input" style="width:90%;">
                            <i class="search icon"></i>
                            <input style="width:100%;" placeholder="Πληκτρολογήστε το τμήμα..." type="text">
                        </div>
                        <%
                            for (Department department : departments) {
                                if(department.getType().equals("ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ")){
                        %>
                        <div class="item" data-value="<%=department.getStateId() %>.<%=department.getCityId() %>"><b><%=department.getType() %> <%=department.getName() %> (<%=department.getStateId() %><%=department.getCityId() %>)</b></div>
                        <% 
                                }else{
                        %>
                        <div class="item" data-value="<%=department.getStateId() %>.<%=department.getCityId() %>">&nbsp;&nbsp;&nbsp;&nbsp;<%=department.getType() %> <%=department.getName() %> (<%=department.getStateId() %><%=department.getCityId() %>)</div>
                        <%
                                }
                            } //for
                        %>
                    </div>
                </div>
            </div>
        </div>
        <div class="fields">
            <div class="eight wide field">
                <label>Παρατηρήσεις Τοπικού Τμήματος</label>
                <input id="ttcomments" name="ttcomments" placeholder="Παρατηρήσεις Τοπικού Τμήματος" type="text"
                <% if(!user.getRole().equals("tt") && !user.getRole().equals("su")) { %>          
                    readonly   
                <% } %>
                <% if(edit_mode.equals("yes") && (member.getTt_notes()!=null)){ %> 
                    value="<%=member.getTt_notes() %>"
                <% } %>
                >
            </div>
            <div class="eight wide field">
                <div class="date field">
                 <label>Ημερομηνία Αίτησης/Εγγραφής</label>
                 <% 
                    if(edit_mode.equals("yes") && (member.getApplicationdate()!=null)){
                        if(member.getApplicationdate().equals("0000-00-00")){
                 %>
                 <div class="ui left icon input" >
                        <i class="red warning sign icon large application_date_not_exists" id="apdatemsgicon" style="display:block;" ></i>&nbsp;
                        <div class="ui flowing popup top left transition hidden yellow" id="noappdatemsg">
                            H <b>Ημερομηνία Αίτησης/Εγγραφής</b> δεν έχει καθοριστεί.<br/>Λόγω ότι το πεδίο είναι απολύτως απαραίτητο για την εύρυθμη λειτουργία του συστήματος, το σύστημα εμφανίζει την <b>εκτιμώμενη ημερομηνία Αίτησης/Εγγραφής</b> σύμφωνα με τα δεδομένα που διαθέτει.
                        </div>
                 <%     
                        }
                    }else{ 
                 %> 
                    <i id="apdatemsgicon" style="display:none;" ></i> <!--to prevent javascript error -->
                 <%     
                    }
                 %> 
                 <input id="datepicker2" name="applicationdate" placeholder="xx/xx/xxxx" type="text" readonly style="cursor:pointer"
                 <% if(edit_mode.equals("yes") && (member.getApplicationdate()!=null)){
                        if(!member.getApplicationdate().equals("0000-00-00")){
                 %>
                    
                            value="<%=member.getApplicationdate() %>"
                 <% 
                        }else{
                            int last_paid_year=0;
                            try{
                                last_paid_year = Integer.parseInt(member.getLast_paid_year());
                            }catch(Exception e){}
                 %>
                            value="01/01/<%=last_paid_year %>" class="application_date_not_exists"
                 <%
                        } 
                    }
                 %>          
                 >
                 <div class="ui flowing popup top left transition hidden yellow" id="noappdatemsg2">
                    H <b>Ημερομηνία Αίτησης/Εγγραφής</b> δεν έχει καθοριστεί.<br/>Λόγω ότι το πεδίο είναι απολύτως απαραίτητο για την εύρυθμη λειτουργία του συστήματος, το σύστημα εμφανίζει την <b>εκτιμώμενη ημερομηνία Αίτησης/Εγγραφής</b> σύμφωνα με τα δεδομένα που διαθέτει.
                 </div>
                 <% 
                    if(edit_mode.equals("yes") && (member.getApplicationdate()!=null)){
                        if(member.getApplicationdate().equals("0000-00-00")){
                 %>
                </div>
                <%     
                        }
                    } 
                 %> 
                 <input id="datepicker2replica" name="applicationdatereplica" type="hidden"
                 <% if(edit_mode.equals("yes") && (member.getApplicationdate()!=null)){
                        if(!member.getApplicationdate().equals("0000-00-00")){
                 %>
                    
                            value="<%=member.getApplicationdate() %>"
                 <% 
                        }else{
                            int last_paid_year=0;
                            try{
                                last_paid_year = Integer.parseInt(member.getLast_paid_year());
                            }catch(Exception e){}
                 %>
                            value="01/01/<%=last_paid_year %>"
                 <%
                        } 
                    }
                 %>          
                 >
                </div>
            </div>
        </div>
        <%  if(user.getRole().equals("kd") || user.getRole().equals("su") || user.getRole().equals("pt")) { %>
        <div class="fields">
            <div class="eight wide field">
                <label>Παρατηρήσεις Περιφερειακού Τμήματος</label>
                <input id="ptcomments" name="ptcomments" placeholder="Παρατηρήσεις Περιφερειακού Τμήματος" type="text"
                <% if(!user.getRole().equals("pt") && !user.getRole().equals("su")) { %>          
                    readonly   
                <% } %>
                <% if(edit_mode.equals("yes") && (member.getPt_notes()!=null)){ %> 
                    value="<%=member.getPt_notes() %>"
                <% } %>
                >
            </div>
            <div class="eight wide field">
                <label>Κατάσταση προώθησης εγγραφής από το Π.Τ.</label>
                <input type="checkbox" id="ptstatuscheckbox" name="ptstatuscheckbox"
                    <% if(edit_mode.equals("yes") && (member.getPt_forward()!=null)){ 
                            if(member.getPt_forward().equals("1")) {
                    %>
                            checked="true"
                    <%      
                            }
                       } 
                    %>       
                >
                <input id="ptstatus" name="ptstatus" type="hidden"
                    <% if(edit_mode.equals("yes") && (member.getPt_forward()!=null)){ 
                            if(member.getPt_forward().equals("1")) {
                    %>
                            value="1"
                    <%      
                            }else{
                    %>
                            value="0"
                    <%
                            }
                       }else{ 
                    %>
                        value="0"
                    <% } %>
                >
            </div>
        </div>
        <% } %>
        <%  if(user.getRole().equals("kd") || user.getRole().equals("su")) { %>
        <div class="fields">
            <div class="eight wide field">
                <label>Παρατηρήσεις Κεντρικής Διοίκησης</label>
                <input id="kdcomments" name="kdcomments" placeholder="Παρατηρήσεις Κεντρικής Διοίκησης" type="text"
                <% if(!user.getRole().equals("kd") && !user.getRole().equals("su")) { %>          
                    readonly   
                <% } %>
                <% if(edit_mode.equals("yes") && (member.getKd_notes()!=null)){ %> 
                    value="<%=member.getKd_notes() %>"
                <% } %>
                >
            </div>
            <div class="four wide field">
            <label>Κατάσταση αποδοχής εγγραφής από την Κ.Δ.</label>
            <input type="checkbox" id="kdstatuscheckbox" name="kdstatuscheckbox"
                <% if(edit_mode.equals("yes") && (member.getKd_approval()!=null)){ 
                        if(member.getKd_approval().equals("1")) {
                %>
                        checked="true"
                <%      
                        }
                   } 
                %>        
            >
            <input id="kdstatus" name="kdstatus" type="hidden"
                <% if(edit_mode.equals("yes") && (member.getKd_approval()!=null)){ 
                        if(member.getKd_approval().equals("1")) {
                %>
                        value="1"
                <%      
                        }else{
                %>
                        value="0"
                <%
                        }
                   }else{ 
                %>
                    value="0"
                <% } %>
            >
            </div>
            <div class="four wide field">
                <div class="date field">
                 <label>Ημερομηνία Απόφασης Κ.Δ.</label>
                 <input id="datepicker1" name="decisiondatekd" placeholder="xx/xx/xxxx" type="text" readonly style="cursor:pointer"
                 <% if(edit_mode.equals("yes") && (member.getDecisiondatekd()!=null)){ %>  
                    value="<%=member.getDecisiondatekd() %>"
                 <% } %>        
                 >
                </div>
            </div>
        </div>
        <% } %>
        <div class="fields">
            <div class="five wide field">
                <label>Κόστος Εγγραφής</label>
                <input name="cost" id="cost" placeholder="Κόστος Εγγραφής" type="text" readonly
                    <% if(edit_mode.equals("yes") && (member.getRegistration_fee()!=null) && (member.getAnnual_membership()!=null) && (member.getRemaining()!=null)){
                            double remaining = 0;
                            double reg_fee = 0;
                            double annual = 0;
                            double rem_ann_modulo = 0;
                            try{
                                remaining = Double.parseDouble(member.getRemaining());
                                reg_fee = Double.parseDouble(member.getRegistration_fee());
                                annual = Double.parseDouble(member.getAnnual_membership());
                                rem_ann_modulo = remaining%annual;
                            }catch(Exception e){}
                        
                            if(remaining>=reg_fee && rem_ann_modulo>0){
                    %>  
                        value="<%=member.getRegistration_fee() %>"
                    <%      
                            } else {
                    %> 
                        value=""
                    <%      }
                       } else if(edit_mode.equals("no")){ %>
                       value="<%=registration_cost %>"
                    <% }%>
                >
                <input name="cost_replica" id="cost_replica" placeholder="Κόστος Εγγραφής" type="hidden" readonly
                    <% if(edit_mode.equals("yes") && (member.getRegistration_fee()!=null) && (member.getAnnual_membership()!=null) && (member.getRemaining()!=null)){
                            double remaining = 0;
                            double reg_fee = 0;
                            double annual = 0;
                            double rem_ann_modulo = 0;
                            try{
                                remaining = Double.parseDouble(member.getRemaining());
                                reg_fee = Double.parseDouble(member.getRegistration_fee());
                                annual = Double.parseDouble(member.getAnnual_membership());
                                rem_ann_modulo = remaining%annual;
                            }catch(Exception e){}
                        
                            if(remaining>=reg_fee && rem_ann_modulo>0){
                    %>  
                        value="<%=member.getRegistration_fee() %>"
                    <%      
                            } else {
                    %> 
                        value=""
                    <%      }
                       } else if(edit_mode.equals("no")){ %>
                       value="<%=registration_cost %>"
                    <% }%>
                >
            </div>
            <div class="five wide field">
                <%
                boolean remaining_exists=false;
                double remaining = 0;
                double annual = 0;
                try{
                    String[] remaining_parts1 = member.getRemaining().split("\\.");
                    String[] remaining_parts2 = remaining_parts1[0].split(",");
                    if(Double.parseDouble(remaining_parts2[0])>0){
                        remaining_exists=true;
                        remaining = Double.parseDouble(remaining_parts2[0]);
                    }
                    annual = Double.parseDouble(member.getAnnual_membership());
                }catch(Exception e){}
                
                int remaining_years = 0, last_paid_year=0, year=0;
                
                if(remaining_exists==true){
                    Date date=new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    year = cal.get(Calendar.YEAR);
                    
                    try{
                        remaining_years = year - Integer.parseInt(member.getLast_paid_year());
                        last_paid_year = Integer.parseInt(member.getLast_paid_year());
                    }catch(Exception e){}
                }
                %>
                <label>Κόστος ετήσιας συνδρομής</label>
                <input name="annualcost" id="annualcost" placeholder="Κόστος ετήσιας συνδρομής" type="text" readonly
                    <% if(edit_mode.equals("yes") && member.getAnnual_membership()!=null && member.getRemaining()!=null){ 
                            
                            if(remaining>=annual){
                    %>  
                       value="<%=member.getAnnual_membership() %>"
                    <%      
                            } else {
                    %> 
                        value=""
                    <%      }
                        }else if(edit_mode.equals("no")){ %> 
                        value=""
                    <% }%>
                >
                <input name="annualcost_replica" id="annualcost_replica" placeholder="Κόστος ετήσιας συνδρομής" type="hidden" readonly
                    <% if(edit_mode.equals("yes") && member.getAnnual_membership()!=null && member.getRemaining()!=null){ 
                            
                            if(remaining>=annual){
                    %>  
                       value="<%=member.getAnnual_membership() %>"
                    <%      
                            } else {
                    %> 
                        value=""
                    <%      }
                        }else if(edit_mode.equals("no")){ %> 
                        value=""
                    <% }%>
                >
            </div>
            <div class="six wide field">
                <%  if(user.getRole().equals("kd") || user.getRole().equals("su")) { %>
                <div class="ui toggle checkbox remaining">
                    <label><b>Υπόλοιπο Οφειλής</b>&nbsp;&nbsp;&nbsp;<i>(Αποδεκτή μορφή τιμών: 00 ή 00.00)</i></label>
                    <input name="enable_reremainingcost" type="checkbox">
                </div>
                <% } else { %>
                    <label>Υπόλοιπο Οφειλής</label>
                <% } %>
                <input name="remainingcost" id="remainingcost" placeholder="Υπόλοιπο Οφειλής" type="text" pattern="^(\d+\.)?\d+$" readonly
                    <% if(edit_mode.equals("yes") && (member.getRemaining()!=null)){ %>  
                       value="<%=member.getRemaining() %>"
                    <% }else if(edit_mode.equals("no")){ %> 
                        value="<%=registration_cost %>"
                    <% }%>      
                >
                <input name="remainingcost_replica" id="remainingcost_replica" placeholder="Υπόλοιπο Οφειλής" type="hidden" readonly
                    <% if(edit_mode.equals("yes") && (member.getRemaining()!=null)){ %>  
                       value="<%=member.getRemaining() %>"
                    <% }else if(edit_mode.equals("no")){ %> 
                        value="<%=registration_cost %>"
                    <% }%>      
                >
                <% 
                if(remaining_years > 0){
                %>
                    <i>Έτη: 
                        <% for(int i=last_paid_year+1; i<=year; i++){ %>
                            <%=i %>
                            <% if(i<year){ %>
                            ,&nbsp;
                            <% } %>
                        <% } %>
                        &nbsp;- Πλήθος ετών: <%=remaining_years %>
                    </i>
                <% 
                    }
                %> 
            </div>
        </div>
<!--        <div class="four wide field">
            <label>Φίλος</label>
            <input name="status" type="checkbox">
        </div>-->
        <button id="submitbutton" class="ui green submit button">Υποβολή</button>
        <div id="resetbutton" class="ui red reset button">
        <% if(edit_mode.equals("yes")){ %>      
            Επαναφορά
        <% } else { %>
            Εκκαθάριση
        <% } %>
        </div>
    </form>
</div>
                    
<script>
    var insertform = $("#insert-form");
    
    $('.ui.dropdown').dropdown();
    
    $('#resetbutton').on("click", function () {
        $('#insert-form').get(0).reset();
    });
    
    $('.message .close').on('click', function() {
        $(this).closest('.message').fadeOut();
    });
    
    $('#ptstatuscheckbox').on("change", function () {
        if ($('#ptstatuscheckbox').is(':checked')) {
            $('#ptstatus').val("1"); 
        }else{
            $('#ptstatus').val("0");
        }
    });
    
    $('#kdstatuscheckbox').on("change", function () {
        if ($('#kdstatuscheckbox').is(':checked')) {
            $('#kdstatus').val("1"); 
        }else{
            $('#kdstatus').val("0");
        }
    });
    
    function submitMoney(thememberid){
        var newlocation="InsertEconomics?memberid=" + thememberid;
        window.location.href = newlocation;
    }
    
    $('#submitbutton').on("click", function () {
        insertform.form({
            lastname: {
                identifier: "lastname",
                rules: [
                    {type: "empty", prompt: "Συμπληρώστε το επίθετο"}
                ]
            },
            firstname: {
                identifier: "firstname",
                rules: [
                    {type: "empty", prompt: "Συμπληρώστε το όνομα"}
                ]
            },
            zipcode: {
                identifier: "zipcode",
                optional   : true,
                rules: [
                    { type: "length[5]", prompt: "Μη έγκυρος TK" },
                    { type: "maxLength[5]", prompt: "Μη έγκυρος TK" }
                ]
            },
            county: {
                identifier: "county",
                rules: [
                    {type: "empty", prompt: "Επιλέξτε νομό"}
                ]
            },
            state: {
                identifier: "state",
                rules: [
                    {type: "empty", prompt: "Επιλέξτε την περιφέρεια"}
                ]
            },
            department: {
                identifier: "department",
                rules: [
                    {type: "empty", prompt: "Επιλέξτε τμήμα"}
                ]
            },
            amka: {
                identifier: "amka",
                optional   : true,
                rules: [
                    { type: "length[11]", prompt: "Μη έγκυρο ΑΜΚΑ" },
                    { type: "maxLength[11]", prompt: "Μη έγκυρο ΑΜΚΑ" }
                ]
            },
            afm: {
	            identifier: "afm",
	            optional   : true,
	            rules: [
	                { type: "length[9]",  prompt: "Μη έγκυρο ΑΦΜ" },
			{ type: "maxLength[9]", prompt: "Μη έγκυρο ΑΦΜ" }
	            ]
	        }
			}, {
            inline: true,
            onSuccess: addMember
        });
    });
    
    function addMember(){
        insertform.submit();
    }
    
    $(function () {
        $("#datepicker1").datepicker();
    });
    
    $(function () {
        $("#datepicker2").datepicker();
    });
    
    $(function () {
        $("#datepicker3").datepicker();
    });
    
    
    $("#datepicker2").on("change", function () {
        
        if(document.getElementById("apdatemsgicon").style.display == "block"){
            document.getElementById("apdatemsgicon").style.display = "none";
            document.getElementById("noappdatemsg").parentNode.removeChild(document.getElementById("noappdatemsg"));
            document.getElementById("noappdatemsg2").parentNode.removeChild(document.getElementById("noappdatemsg2"));
        }
        
        var current_year=<%=current_year %>;
        var selected_date=document.getElementById("datepicker2").value;
        var selected_date_replica=document.getElementById("datepicker2replica").value;
        var selected_year= selected_date.split("/");
        var selected_year_replica= selected_date_replica.split("/"); 
        var years_dif = current_year-selected_year[2];
        
        var member_remaing=0;
        var edit_mode="no";
        
        <% if(!member.getRemaining().equals("")){ %>  
            member_remaing=<%=member.getRemaining() %>;
        <% } %>
            
        <% if(edit_mode.equals("yes")){ %>  
            edit_mode="yes";
        <% } %>
            
        var registration_cost=<%=registration_cost %>;
        var annual_cost=<%=annual_cost %>;
        
        if(edit_mode=="no"){
            if(years_dif==0){
                document.getElementById("cost").value=registration_cost;
                document.getElementById("annualcost").value="";
                document.getElementById("remainingcost").value=registration_cost;
            }else if(years_dif>0){
                document.getElementById("cost").value=registration_cost;
                document.getElementById("annualcost").value=annual_cost;
                document.getElementById("remainingcost").value=registration_cost+(years_dif*annual_cost);
            }else{
                document.getElementById("cost").value="";
                document.getElementById("annualcost").value="";
                document.getElementById("remainingcost").value="";
            }
        }else{
            years_dif=selected_year_replica[2]-selected_year[2];
            
            if(years_dif==0){
                document.getElementById("cost").value=document.getElementById("cost_replica").value;
                document.getElementById("annualcost").value=document.getElementById("annualcost_replica").value;
                document.getElementById("remainingcost").value=document.getElementById("remainingcost_replica").value;
            }else if(years_dif>0){
                var new_remaining = member_remaing+(years_dif*annual_cost);

                var rem_ann_modulo = new_remaining%annual_cost;
            
                if(new_remaining>=registration_cost && rem_ann_modulo>0){
                    document.getElementById("cost").value=registration_cost;
                }else{
                    document.getElementById("cost").value="";
                }
                
                if(new_remaining>=annual_cost){
                    document.getElementById("annualcost").value=annual_cost;
                }else{
                    document.getElementById("annualcost").value="";
                }
                
                document.getElementById("remainingcost").value=new_remaining;
            }else{                
                var new_remaining = member_remaing+(years_dif*annual_cost);

                if(new_remaining < 0){
                    new_remaining = 0;
                }

                var rem_ann_modulo = new_remaining%annual_cost;
            
                if(new_remaining>=registration_cost && rem_ann_modulo>0){
                    document.getElementById("cost").value=registration_cost;
                }else{
                    document.getElementById("cost").value="";
                }
                
                if(new_remaining>=annual_cost){
                    document.getElementById("annualcost").value=annual_cost;
                }else{
                    document.getElementById("annualcost").value="";
                }
                
                document.getElementById("remainingcost").value=new_remaining;
            }
        }
    });
    
    $('.application_date_not_exists').popup({
        inline: true
    });
    
    $('.afm_exists').popup({
        inline: true
    });
    
    $('.ui.checkbox.remaining').checkbox();
    
    $(".ui.checkbox.remaining").on("change", function (evt) {
	if($(".ui.checkbox.remaining").hasClass('checked')){
            $("#remainingcost").attr("readonly", true); 
        }else{
            $("#remainingcost").attr("readonly", false); 
        }
    });
    
    function searchAfm(){
        
        if(($("#edit_mode").val()=="no") || ($("#edit_mode").val()=="yes" && $("#afm").val()!=$("#member_submitted_afm").val())){
            $.get("<jstl_c:url value='/main/AfmLiveSearch?afm="+$("#afm").val()+"' />", {offset: 0, limit: 100}).done(function (data) {
                var member = data.member;

                if(member[0].afm==""){
                    document.getElementById("afm_exists_warning").innerHTML="";
                    $("#afm_exists_warning").removeClass("popup");
                    $("#afm_exists_warning").removeClass("flowing");
                    $("#afm_exists_warning").removeClass("transition");
                    $("#afm_exists_warning").removeClass("top");
                    $("#afm_exists_warning").removeClass("left");
                    $("#afm_exists_warning").removeClass("hidden");
                    $("#afm_div").removeClass("error");
                    //document.getElementById("afm").style.color="white";
                }else{
                    document.getElementById("afm_exists_warning").innerHTML="<b><u>Υπάρχει ήδη εγγεγραμμένο μέλος με αυτό το ΑΦΜ.</u></b><br/><br/><b><i>Στοιχεία μέλους</i></b><br/>Ονοματεπώνυμο: "+member[0].fullname+"<br/>Κωδικός: "+member[0].member_id+"<br/>Τμήμα: "+member[0].department;
                    $("#afm_exists_warning").addClass("popup");
                    $("#afm_exists_warning").addClass("flowing");
                    $("#afm_exists_warning").addClass("transition");
                    $("#afm_exists_warning").addClass("top");
                    $("#afm_exists_warning").addClass("left");
                    $("#afm_exists_warning").addClass("hidden");
                    $("#afm_div").addClass("error");
                }

            }
            ).always(function () {

            });
        }else{
            document.getElementById("afm_exists_warning").innerHTML="";
            $("#afm_exists_warning").removeClass("popup");
            $("#afm_exists_warning").removeClass("flowing");
            $("#afm_exists_warning").removeClass("transition");
            $("#afm_exists_warning").removeClass("top");
            $("#afm_exists_warning").removeClass("left");
            $("#afm_exists_warning").removeClass("hidden");
            $("#afm_div").removeClass("error");
        }
    }
    
    $("#afm").on("change", function (evt) {
	searchAfm();
        evt.stopPropagation();
    });
    
    $("#afm").on("blur", function (evt) {
	searchAfm();
        evt.stopPropagation();
    });
    
    $("#jobselect").on("change", function (evt) {
	$("#job").val($("#jobselect").val());
    });
    
    $("#zipcodeselect").on("change", function (evt) {
	$("#zipcode").val($("#zipcodeselect").val());
    });
    
    $(document).ready(function() {
        document.getElementById("afm_exists_warning").innerHTML="";
        $("#afm_exists_warning").removeClass("popup");
        $("#afm_exists_warning").removeClass("flowing");
        $("#afm_exists_warning").removeClass("transition");
        $("#afm_exists_warning").removeClass("top");
        $("#afm_exists_warning").removeClass("left");
        $("#afm_exists_warning").removeClass("hidden");
    });
</script>

<style>
    .ui-datepicker { font-size: 9pt !important; }
</style>
