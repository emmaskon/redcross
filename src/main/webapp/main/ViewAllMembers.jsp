<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@ page import="java.util.*" %>
<%@ page import="models.User" %>
<%@ page import="models.Member" %>
<%@ page import="models.Department" %>

<%
    ArrayList<Member> members = (ArrayList<Member>) request.getAttribute("members");
    ArrayList<Member> allmembers = (ArrayList<Member>) request.getAttribute("allmembers");
    ArrayList<Department> departments = (ArrayList<Department>) request.getAttribute("departments");
    Department current_department = (Department) request.getAttribute("department");
    String specific_search = (String) request.getAttribute("specific_search");
    String specific_member_id = (String) request.getAttribute("specific_member_id");
    String overtwoyears = (String) request.getAttribute("overtwoyears");
    String department_name = (String) request.getAttribute("department_name");
    String pending = (String) request.getAttribute("pending");
    String tobepaid = (String) request.getAttribute("tobepaid");
    String searchresult = (String) request.getAttribute("searchresult");
    User user = (User) session.getAttribute("user");
    
    String mails="";
    
    if(user==null){ 
%>
<script>
    location.replace("<jstl_c:url value='/Logout' />");
</script>    
<%
    }
%>
<% if(department_name.equals("select")){ %>
<div class="ui blue icon message">
  <i class="info circle icon"></i>
  <div class="content">
    <div class="header">
        <% if(pending.equals("yes")){ %>
        Επιλέξτε ένα τμήμα για να εμφανίσετε τις σχετικές αιτήσεις!
        <% } else if(pending.equals("no")){ %>
        Επιλέξτε ένα τμήμα για να εμφανίσετε τα εγγεγραμμένα μέλη που διαθέτει!
        <% } else if(pending.equals("repeated")){ %>
        Επιλέξτε ένα τμήμα για να εμφανίσετε τις σχετικές επαναλαμβανόμενες αιτήσεις!
        <% } %>
    </div>
  </div>
</div>
<% } %>
<div class="ui page dimmer">
  <div class="content">
        <div class="center">
            <h2 class="ui inverted icon">
                <i class="spinner loading huge icon"></i>
                <div id="dimmer_text"></div>
            </h2>
        </div>
  </div>
</div>
<div id="members" class="ui segment">
    <% if(pending.equals("yes")){ %>
    <h4 class="ui dividing header">Αιτήσεις σε αναμονή</h4>
    <% } else if(pending.equals("no")){ 
            if(searchresult.equals("no")){ %> 
    <h4 class="ui dividing header">Πίνακας Μελών</h4>
    <%      }else{ %>
    <h4 class="ui dividing header">Αποτελέσματα Αναζήτησης</h4> 
    <%
            }
       } else if(pending.equals("repeated")){ %>
    <h4 class="ui dividing header">Επαναλαμβανόμενες αιτήσεις</h4>
    <% } %>
    <table class="ui celled structured small table" id="records_table">
        <thead>
            <tr>
                <th colspan="13">
                    <div class="nine wide field">
                        <% if(pending.equals("yes")){ %>
                        <label>ΑΙΤΗΣΕΙΣ&nbsp;&nbsp;</label>
                        <% } else if(pending.equals("no")){ 
                                if(searchresult.equals("no")){ %> 
                            <label>ΕΓΓΕΓΡΑΜΜΕΝΑ ΜΕΛΗ&nbsp;&nbsp;</label>
                        <%      }else { %> 
                            <label>ΤΜΗΜΑ&nbsp;&nbsp;</label>
                        <%      }
                           } else if(pending.equals("repeated")){ %>
                        <label>ΕΠΑΝΑΛΑΜΒΑΝΟΜΕΝΕΣ ΑΙΤΗΣΕΙΣ&nbsp;&nbsp;</label>
                        <% } %>
                        <div class="ui selection dropdown"
                        <% if(searchresult.equals("yes")){ %> 
                        style="width:400px;pointer-events: none;"
                        <% }else{ %>
                        style="width:400px;"
                        <% } %>
                        >
                            <% if((!department_name.equals("all"))&& (!department_name.equals("select")) 
                                    && current_department.getType()!=null && current_department.getStateId()!=null  && current_department.getCityId()!=null
                                    && (!current_department.getType().equals("")) && (!current_department.getStateId().equals("")) && (!current_department.getCityId().equals(""))
                                 ){ %>
                            <div class="default text"><%=current_department.getType() %> <%=department_name %> (<%=current_department.getStateId() %><%=current_department.getCityId() %>)</div>
                            <input type="hidden" name="showspecific" id="showspecific" value="<%=current_department.getStateId() %>.<%=current_department.getCityId() %>">
                            <% }else if(department_name.equals("select")){ %>
                            <div class="default text">Επιλέξτε ένα τμήμα</div>
                            <input type="hidden" name="showspecific" id="showspecific" value="">
                            <% }else{ %>
                            <div class="default text">ΌΛΑ</div>
                            <input type="hidden" name="showspecific" id="showspecific" value="<%=department_name %>">
                            <% } %>
                            <% if(searchresult.equals("no")){ %> 
                            <i class="dropdown icon"></i>
                            <% } %> 
                            <div class="menu">
                                <div class="ui icon search input" style="width:90%;">
                                    <i class="search icon"></i>
                                    <input style="width:100%;" placeholder="Πληκτρολογήστε το τμήμα..." type="text">
                                </div>
                                <%  if(user.getRole().equals("kd") || user.getRole().equals("su") || user.getRole().equals("pt")) { %>
                                <div class="item" data-value="all">ΌΛΑ</div>
                                <%
                                    }
                                
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
                        &nbsp;
                        <div style="width:40px;" class="ui circular vertical animated icon button" onclick="sendNewsletter()">
                            <div class="visible content"><i class="at icon"></i></div>
                            <div class="hidden content">&nbsp;<i class="mail green icon"></i></div>
                        </div>
                        <div class="ui flowing popup top left transition hidden">
                            <table class="ui celled structured small table">
                            <thead>
                                <tr>
                                    <td><b>Αποστολή ομαδικής ηλεκτρονικής αλληλογραφίας</b></td>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            </table>
                        </div>
                        &nbsp;
                        <div style="width:40px;" class="ui circular vertical animated icon button" onclick="printAllTickets()">
                            <div class="visible content"><i class="tags icon"></i></div>
                            <div class="hidden content">&nbsp;<i class="print green icon"></i></div>
                        </div>
                        <div class="ui flowing popup top left transition hidden">
                            <table class="ui celled structured small table">
                            <thead>
                                <tr>
                                    <td><b>Εκτύπωση όλων των ετικετών</b></td>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            </table>
                        </div>
                        <% if(pending.equals("yes")){ %>
                        &nbsp;<i>Αριθμός αιτήσεων σε αναμονή: <%=members.size() %> </i>
                        <% } else if(pending.equals("no")){ %>
                        &nbsp;<i>Αριθμός μελών: <%=members.size() %> , Συνολικό ατιμολόγητο ποσό: <%=tobepaid %> €</i>
                        <% } else if(pending.equals("repeated")){ %>
                        &nbsp;<i>Αριθμός επαναλαμβανόμενων αιτήσεων: <%=members.size() %> </i>
                        <% } %>
                    </div>
                </th>
            </tr>
            
            <tr id="titles">
                <th>ΑΑ</th>
                <th>ΚΩΔΙΚΟΣ ΜΕΛΟΥΣ
                    <% if(!pending.equals("yes") && !pending.equals("repeated") && searchresult.equals("no")){ %>
                    <div class="ui selection dropdown">
                        <% if(specific_member_id.equals("all")){ %>
                        <input type="hidden" name="search_member_id" id="search_member_id" value="ΌΛΑ">
                        <% } else if(specific_member_id.equals("select")){ %>
                        <input type="hidden" name="search_member_id" id="search_member_id" value="">
                        <% } else { %>
                        <input type="hidden" name="search_member_id" id="search_member_id" value="<%=specific_member_id %>">
                        <% } %>
                        <div class="default text"></div>
                        <i class="dropdown icon"></i>
                        <div class="menu visible">
                            <div class="ui icon search input">
                                <i class="search icon"></i>
                                <input style="width:100%;" placeholder="Αναζήτηση..." type="text">
                            </div>
                            <div class="item" data-value="all">ΌΛΑ</div>
                            <% for (Member member : allmembers) { %>
                            <div class="item" data-value="<%=member.getMember_id() %>"><%=member.getMember_id() %></div>
                            <% } %>
                        </div>
                    </div>
                    <% } %>
                </th>
                <th>ΕΠΩΝΥΜΟ</th>
                <th>ΟΝΟΜΑ</th>
                <th>ΔΙΕΥΘΥΝΣΗ</th>
                <th>ΤΚ</th>
                <th>ΠΕΡΙΟΧΗ</th>
                <th>ΔΗΜΟΣ</th>
                <th>ΝΟΜΟΣ</th>
                <th>ΠΕΡΙΦΕΡΕΙΑ</th>
                <th>ΠΕΡ.ΤΜ. Ε.Ε.Σ.</th>
                <th>ΤΟΠΙΚΟ ΤΜΗΜΑ</th>
                <th>ΑΤΙΜΟΛΟΓΗΤΟ
                    <% if(!pending.equals("yes") && !pending.equals("repeated") && searchresult.equals("no")){ %>
                    <div style="width:140px;margin-top:10px;color:#787878;font-style: italic;">
                        <input type="checkbox" id="only_two_years_nopaid" name="only_two_years_nopaid"
                        <% if(overtwoyears.equals("yes")){ %>
                        checked
                        <% } %>
                        > Μόνο οφειλές για πάνω από 2 έτη
                    </div>
                    <% } %>
                </th>
            </tr>
        </thead>
        <tbody id="data">
            <%
                int j = 0;
                for (Member member : members) {
                    j++;
                    
                    String message="";
                    
                    if(member.getAfm().length()<9){
                        if(message.length()>0){
                            message+=", ";
                        }
                        message+="ΑΦΜ";
                    }
                    
                    if(member.getIdentity_number().length()<7){
                        if(message.length()>0){
                            message+=", ";
                        }
                        message+="ΑΔΤ";
                    }
                    
                    if(member.getPhone().length()<10 && member.getMobile_phone().length()<10){
                        if(message.length()>0){
                            message+=", ";
                        }
                        
                        message+="Αριθμός τηλεφώνου";
                    }
                    
                    if(member.getEmail().length()>4){
                        mails+=member.getEmail().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "")+";";
                    }else{
                        if(message.length()>0){
                            message+=", ";
                        }
                        message+="email";
                    }
            %>
            <tr data-record="<%=member.getMember_id() %>">
                <td style="min-width:130px">
                    <i title="Αύξοντας Αριθμός"><%=j %></i>&nbsp;&nbsp;
                        <button class="ui icon button 
                        <%
                            if(!message.equals("")){
                        %>
                        yellow
                        <%
                            }
                        %>        
                        editbtn" id="edit<%=member.getMember_id() %>" onclick="editMember('<%=member.getMember_id() %>')" >
                            <i class="edit icon"></i>
                        </button>
                        <% if(!message.equals("")){ %>
                        <div class="ui flowing popup top left transition hidden">
                           Eλλιπή στοιχεία: <b><%=message %></b>.
                        </div>
                        <% } %>
                        <%  if(user.getRole().equals("kd") || user.getRole().equals("su")) { %>
                        <button class="ui icon button red delete btn circular mini" id="delete<%=member.getMember_id() %>" onclick="deleteMember('<%=member.getMember_id() %>')">
                            <i class="remove circle icon"></i>
                        </button>
                        
                        <form class="ui form" id="deletemember<%=member.getMember_id() %>">
                            <input type="hidden" name="delmemberid<%=member.getMember_id() %>" id="delmemberid<%=member.getMember_id() %>" value="<%=member.getMember_id() %>" />
                            <div class="ui small basic modal" id="confirm_delete_<%=member.getMember_id() %>">
                                <div class="ui icon header">
                                    <i class="warning icon"></i>
                                    Επιβεβαίωση
                                </div>
                                <div class="content">
                                    <p>Έιστε βέβαιοι ότι θέλετε να διαγράψετε το μέλος <%=member.getMember_id() %>;</p>
                                </div>
                                <div class="actions">
                                    <div class="ui green ok inverted button">
                                        <i class="checkmark icon"></i>
                                        Ναι
                                    </div>
                                    <div class="ui red basic cancel inverted button">
                                        <i class="remove icon"></i>
                                        Όχι
                                    </div>
                                </div>
                            </div>
                        </form>
                        <% } %>
                </td>
                <td style="min-width:260px"><%=member.getMember_id() %>&nbsp;&nbsp;
                    <div style="width:40px;" class="ui circular vertical animated icon button" id="viewpayments" onclick="viewPayments('<%=member.getMember_id() %>')">
                        <div class="visible content"><i class="browser icon"></i></div>
                        <div class="hidden content">&nbsp;<i class="ordered list green icon"></i></div>
                    </div>
                    <div class="ui flowing popup top left transition hidden">
                        <table class="ui celled structured small table">
                            <thead>
                                <tr>
                                    <td><b>Προβολή πληρωμών</b></td>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div style="width:40px;" class="ui circular vertical animated icon button" id="submitmoney" onclick="submitMoney('<%=member.getMember_id() %>.newpayment')">
                        <div class="visible content"><i class="euro icon"></i></div>
                        <div class="hidden content">&nbsp;<i class="add green icon"></i></div>
                    </div>
                    <div class="ui flowing popup top left transition hidden">
                        <table class="ui celled structured small table">
                            <thead>
                                <tr>
                                    <td><b>Καταχώρηση πληρωμής</b></td>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div style="width:40px;" class="ui circular vertical animated icon button" onclick="PrintMe('<%=member.getSurname().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>','<%=member.getName().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>','<%=member.getAddress().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>','<%=member.getAddress_zipcode().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>','<%=member.getAddress_municipality().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>')">
                        <div class="visible content"><i class="tag icon"></i></div>
                        <div class="hidden content">&nbsp;<i class="print green icon"></i></div>
                    </div>
                    <div class="ui flowing popup top left transition hidden">
                        <table class="ui celled structured small table">
                        <thead>
                            <tr>
                                <th>Εκτύπωση ετικέτας</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                <b>ΠΡΟΣ</b><br/>
                                <%=member.getSurname() %> <%=member.getName() %> <br/>
                                <%=member.getAddress() %>, <%=member.getAddress_zipcode() %><br/>
                                <%=member.getAddress_municipality() %>
                                </td>
                            </tr>
                        </tbody>
                        </table>
                    </div>
                </td>
                <td><%=member.getSurname() %></td>
                <td><%=member.getName() %></td>
                <td><%=member.getAddress() %></td>
                <td><%=member.getAddress_zipcode() %></td>
                <td><%=member.getAddress_area() %></td>
                <td><%=member.getAddress_municipality() %></td>
                <td><%=member.getAddress_county() %></td>
                <td><%=member.getAddress_state_name() %></td>
                <td><%=member.getPt() %></td>
                <td><%=member.getTt() %></td>
                <%
                boolean remaining_exists=false;
                try{
                    String[] remaining_parts1 = member.getRemaining().split("\\.");
                    String[] remaining_parts2 = remaining_parts1[0].split(",");
                    if(Integer.parseInt(remaining_parts2[0])>0){
                        remaining_exists=true;
                    }
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
                %>
                <td style="color:red;font-weight:bold;">
                <% } else {%>
                <td>    
                <% }%>
                <%=member.getRemaining() %> €
                <% 
                    if(remaining_years > 0){
                %>
                <br/><i>(Έτη: 
                        <% for(int i=last_paid_year+1; i<=year; i++){ %>
                            <%=i %>
                            <% if(i<year){ %>
                            ,&nbsp;
                            <% } %>
                        <% } %>
                    <br/>Πλήθος ετών: <%=remaining_years %>)</i>
                <% 
                    }
                %>
                </td>
            </tr>
            <% 
                } //for
            %>
        </tbody>
    </table>
</div>
<div class="ui segment">
    <button class="ui green button" id="printbtn" onclick="PrintElem('#titles', '#data')">
        <% if(pending.equals("yes")){ %>
        Εκτύπωση αιτήσεων
        <% } else { %>
        Εκτύπωση μελών
        <% } %>
    </button>
    <h4>Εκτυπώσιμα στοιχεία</h4>
    <table class="ui celled structured small table">
        <tr>
            <td>
                <div class="ui checkbox">
                  <input name="aa_checkbox" id="aa_checkbox" type="checkbox" checked="checked">
                  <label>ΑΑ</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="member_id_checkbox" id="member_id_checkbox" type="checkbox" checked="checked">
                  <label>Κωδικός μέλους</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="surname_checkbox" id="surname_checkbox" type="checkbox" checked="checked">
                  <label>Επώνυμο</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="name_checkbox" id="name_checkbox" type="checkbox" checked="checked">
                  <label>Όνομα</label>
                </div>
            </td> 
            <td>
                <div class="ui checkbox">
                  <input name="fathersname_checkbox" id="fathersname_checkbox" type="checkbox">
                  <label>Όνομα πατρός</label>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="ui checkbox">
                  <input name="job_checkbox" id="job_checkbox" type="checkbox">
                  <label>Επάγγελμα</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="address_checkbox" id="address_checkbox" type="checkbox" checked="checked">
                  <label>Διεύθυνση</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="zipcode_checkbox" id="zipcode_checkbox" type="checkbox" checked="checked">
                  <label>ΤΚ</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="area_checkbox" id="area_checkbox" type="checkbox" checked="checked">
                  <label>Περιοχή</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="municipality_checkbox" id="municipality_checkbox" type="checkbox" checked="checked">
                  <label>Δήμος</label>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="ui checkbox">
                  <input name="county_checkbox" id="county_checkbox" type="checkbox">
                  <label>Νομός</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="state_checkbox" id="state_checkbox" type="checkbox">
                  <label>Περιφέρεια</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="pertmima_checkbox" id="pertmima_checkbox" type="checkbox">
                  <label>Περ.τμ. Ε.Ε.Σ.</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="toptmima_checkbox" id="toptmima_checkbox" type="checkbox">
                  <label>Τοπικό τμήμα</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="phone_checkbox" id="phone_checkbox" type="checkbox">
                  <label>Τηλέφωνο</label>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="ui checkbox">
                  <input name="mobilephone_checkbox" id="mobilephone_checkbox" type="checkbox">
                  <label>Κινητό τηλέφωνο</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="email_checkbox" id="email_checkbox" type="checkbox">
                  <label>Email</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="fax_checkbox" id="fax_checkbox" type="checkbox">
                  <label>Fax</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="amka_checkbox" id="amka_checkbox" type="checkbox">
                  <label>ΑΜΚΑ</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="afm_checkbox" id="afm_checkbox" type="checkbox">
                  <label>ΑΦΜ</label>
                </div>
            </td>
        </tr>
        <tr>    
            <td>
                <div class="ui checkbox">
                  <input name="doy_checkbox" id="doy_checkbox" type="checkbox">
                  <label>ΔΟΥ</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="identity_number_checkbox" id="identity_number_checkbox" type="checkbox">
                  <label>Αριθμός ταυτότητας</label>
                </div>
            </td>
            <td>
                <div class="ui checkbox">
                  <input name="remaining_checkbox" id="remaining_checkbox" type="checkbox" checked="checked">
                  <label>Ατιμολόγητο</label>
                </div>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">

    $('.ui.dropdown').dropdown();
  
    $('.ui.checkbox').checkbox();
    
    function editMember(thememberid){
        var newlocation="<jstl_c:url value='/main/InsertMember'/>"+"?edit="+ thememberid;
        
        <% if(pending.equals("no")){ %>
            newlocation = newlocation+"&pending=no";
        <% } else if(pending.equals("yes")){ %>
            newlocation = newlocation+"&pending=yes";
        <% } else if(pending.equals("repeated")){ %>
            newlocation = newlocation+"&pending=repeated";
        <% } %>
            
        window.location.href = newlocation;
    }
    
    function deleteMember(thememberid){
        var $records = $("#records_table tbody tr[data-record]");
        var $dimmer = $(".ui.dimmer");
        
        $('#confirm_delete_'+thememberid).modal('show');
        
        $('#confirm_delete_'+thememberid).modal({closable: false, onApprove: function () {
            
            $("#dimmer_text").html("Διαγραφή μέλους "+thememberid+"...");
            $dimmer.dimmer("show");
            
            $.post("<jstl_c:url value='DeleteMember'/>", {"memberid": thememberid}).done(function () {
                
            
                $records.filter("[data-record='" + thememberid + "']").nextAll("tr").find("i[title='Αύξοντας Αριθμός']").each(function () {
                    var sn = parseInt($(this).html());
                    $(this).html(sn - 1);
                });

                $records.filter("[data-record='" + thememberid + "']").popup("destroy").remove();
                $records = $("#records_table tbody tr[data-record]");
            }).fail(function () {
                $dimmer.find(".text").html("Προέκυψε σφάλμα...Παρακαλούμε προσπαθήσε ξανά...");
                    setTimeout(function () {
                        $dimmer.dimmer("hide");
                    }, 2000);
            }).done(function () {
                $dimmer.dimmer("hide");
            });
        }});
    }
    
    $("#showspecific").on('change', function() {
        var newlocation="<jstl_c:url value='/main/ViewAllMembers'/>"+"?search="+ $("#showspecific").val();
        
        <% if(pending.equals("yes")){ %>
            newlocation="<jstl_c:url value='/main/ViewPendingSubscriptions'/>"+"?search="+ $("#showspecific").val();
        <% } %>
            
        <% if(pending.equals("repeated")){ %>
            newlocation="<jstl_c:url value='/main/ViewRepeatedSubscriptions'/>"+"?search="+ $("#showspecific").val();
        <% } %>
        
        window.location.href = newlocation;
    });
    
    $("#search_member_id").on('change', function() {
        var newlocation="<jstl_c:url value='/main/ViewAllMembers'/>"+"?search="+ $("#showspecific").val()+"&member_id="+ $("#search_member_id").val();
        
        <% if(pending.equals("yes")){ %>
            newlocation="<jstl_c:url value='/main/ViewPendingSubscriptions'/>"+"?search="+ $("#showspecific").val()+"&member_id="+ $("#search_member_id").val();
        <% } %>
            
        <% if(pending.equals("repeated")){ %>
            newlocation="<jstl_c:url value='/main/ViewRepeatedSubscriptions'/>"+"?search="+ $("#showspecific").val()+"&member_id="+ $("#search_member_id").val();
        <% } %>
        
        window.location.href = newlocation;
    });

    $("#only_two_years_nopaid").on('change', function() {
        
        if($("#only_two_years_nopaid").is(':checked')){
            var newlocation="<jstl_c:url value='/main/ViewAllMembers'/>"+"?search="+ $("#showspecific").val()+"&overtwoyears=yes";

            <% if(pending.equals("yes")){ %>
                newlocation="<jstl_c:url value='/main/ViewPendingSubscriptions'/>"+"?search="+ $("#showspecific").val()+"&member_id="+ $("#search_member_id").val();
            <% } %>
                
            <% if(pending.equals("repeated")){ %>
                newlocation="<jstl_c:url value='/main/ViewRepeatedSubscriptions'/>"+"?search="+ $("#showspecific").val()+"&member_id="+ $("#search_member_id").val();
            <% } %>

            window.location.href = newlocation;
        }else{
            var newlocation="<jstl_c:url value='/main/ViewAllMembers'/>"+"?search="+ $("#showspecific").val()+"&overtwoyears=no";

            <% if(pending.equals("yes")){ %>
                newlocation="<jstl_c:url value='/main/ViewPendingSubscriptions'/>"+"?search="+ $("#showspecific").val()+"&member_id="+ $("#search_member_id").val();
            <% } %>

            <% if(pending.equals("repeated")){ %>
                newlocation="<jstl_c:url value='/main/ViewRepeatedSubscriptions'/>"+"?search="+ $("#showspecific").val()+"&member_id="+ $("#search_member_id").val();
            <% } %>

            window.location.href = newlocation;
        }
    });

    function PrintElem(title, data)
    {
        Popup($(title).html(), $(data).html());
    }

    function Popup(title, data) 
    {
        var mywindow = window.open('', 'my div', 'height=400,width=600');
        
        <% if(pending.equals("yes")){ %>
        mywindow.document.write('<html><head><title>Αιτήσεις σε αναμονή</title>');
        <% } else if(pending.equals("no")){ %>
        mywindow.document.write('<html><head><title>Πίνακας Μελών</title>');
        <% } else if(pending.equals("repeated")){ %>
        mywindow.document.write('<html><head><title>Επαναλαμβανόμενες Αιτήσεις</title>');
        <% } %>
        
        /*optional stylesheet*/ //mywindow.document.write('<link rel="stylesheet" href="main.css" type="text/css" />');
        mywindow.document.write('<style type="text/css"> tr.border_bottom td {border-top: 1px solid #000; text-align:center;} div.outer {line-height: 50px;} div.inner{vertical-align: middle;}</style>');
        mywindow.document.write('<style type="text/css" media="print,screen" >');
        mywindow.document.write('thead { display:table-header-group; }');
        mywindow.document.write('tbody { display:table-row-group; }');
        mywindow.document.write('</style>');
        mywindow.document.write('</head><body>');
        
        mywindow.document.write('<h4 class="ui dividing header">');
        <% if(!department_name.equals("all") && members.size()>0){ %>
        mywindow.document.write('Περιφερειακό τμήμα <%=members.get(0).getPt().replaceAll("\\n", "").replaceAll("'", "") %>');
        <%      if(!members.get(0).getTt().equals("")){ %>
        mywindow.document.write(', Τοπικό τμήμα <%=members.get(0).getTt().replaceAll("\\n", "").replaceAll("'", "") %>');
        <%      } %>
        mywindow.document.write(', Περιφέρεια <%=members.get(0).getAddress_state_name().replaceAll("\\n", "").replaceAll("'", "") %>');
        mywindow.document.write(', Νομός <%=members.get(0).getAddress_county().replaceAll("\\n", "").replaceAll("'", "") %>');
        <% }else if(department_name.equals("all")) { %>
        mywindow.document.write('ΌΛΑ');    
        <% } %>
        <% if(pending.equals("yes")){ %>
        mywindow.document.write(', <br/><i>Αριθμός αιτήσεων σε αναμονή: <%=members.size() %>');
        <% } else if(pending.equals("no")){ %>
        mywindow.document.write(', <br/><i>Αριθμός μελών: <%=members.size() %>, Συνολικό ατιμολόγητο ποσό: <%=tobepaid %> €</i>');
        <% } else if(pending.equals("repeated")){ %>
        mywindow.document.write(', <br/><i>Αριθμός επαναλαμβανόμενων αιτήσεων: <%=members.size() %>');
        <% } %>
    
        mywindow.document.write('</h4><table><thead><tr class="border_bottom">');
        //mywindow.document.write(title);
        
        if($('#aa_checkbox')[0].checked == true){
            mywindow.document.write('<th>AA</th>');
        }
        if($('#member_id_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΚΩΔΙΚΟΣ ΜΕΛΟΥΣ</th>');
        }
        if($('#surname_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΕΠΩΝΥΜΟ</th>');
        }
        if($('#name_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΟΝΟΜΑ</th>');
        }
        if($('#fathersname_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΟΝΟΜΑ ΠΑΤΡΟΣ</th>');
        }
        if($('#job_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΕΠΑΓΓΕΛΜΑ</th>');
        }
        if($('#address_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΔΙΕΥΘΥΝΣΗ</th>');
        }
        if($('#zipcode_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΤΚ</th>');
        }
        if($('#area_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΠΕΡΙΟΧΗ</th>');
        }
        if($('#municipality_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΔΗΜΟΣ</th>');
        }
        if($('#county_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΝΟΜΟΣ</th>');
        }
        if($('#state_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΠΕΡΙΦΕΡΕΙΑ</th>');
        }
        if($('#pertmima_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΠΕΡ.ΤΜ. Ε.Ε.Σ.</th>');
        }
        if($('#toptmima_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΤΟΠΙΚΟ ΤΜΗΜΑ</th>');
        }
        if($('#phone_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΤΗΛΕΦΩΝΟ</th>');
        }
        if($('#mobilephone_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΚΙΝΗΤΟ ΤΗΛΕΦΩΝΟ</th>');
        }
        if($('#email_checkbox')[0].checked == true){
            mywindow.document.write('<th>EMAIL</th>');
        }
        if($('#fax_checkbox')[0].checked == true){
            mywindow.document.write('<th>FAX</th>');
        }
        if($('#amka_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΑΜΚΑ</th>');
        }
        if($('#afm_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΑΦΜ</th>');
        }
        if($('#doy_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΔΟΥ</th>');
        }
        if($('#identity_number_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΑΤ</th>');
        }
        if($('#remaining_checkbox')[0].checked == true){
            mywindow.document.write('<th>ΑΤΙΜΟΛΟΓΗΤΟ</th>');
        }
        mywindow.document.write('</tr></thead><tbody>');
        //mywindow.document.write(data);
        <%
            j = 0;
            for (Member member : members) {
                j++;
        %>
        mywindow.document.write('<tr class="border_bottom">');
        if($('#aa_checkbox')[0].checked == true){
            mywindow.document.write('<td><div class="outer"><span class="inner"><%=j %></span></div></td>');
        }
        if($('#member_id_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getMember_id().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#surname_checkbox')[0].checked == true){    
            mywindow.document.write('<td><%=member.getSurname().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#name_checkbox')[0].checked == true){  
            mywindow.document.write('<td><%=member.getName().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#fathersname_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getFathersname().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#job_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getJob().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#address_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getAddress().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "")  %></td>');
        }
        if($('#zipcode_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getAddress_zipcode().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#area_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getAddress_area().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#municipality_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getAddress_municipality().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#county_checkbox')[0].checked == true){  
            mywindow.document.write('<td><%=member.getAddress_county().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#state_checkbox')[0].checked == true){  
            mywindow.document.write('<td><%=member.getAddress_state_name().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#pertmima_checkbox')[0].checked == true){  
            mywindow.document.write('<td><%=member.getPt().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#toptmima_checkbox')[0].checked == true){  
            mywindow.document.write('<td><%=member.getTt().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        
        if($('#phone_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getPhone().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#mobilephone_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getMobile_phone().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#email_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getEmail().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#fax_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getFax().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#amka_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getAmka().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#afm_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getAfm().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#doy_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getDoy().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        if($('#identity_number_checkbox')[0].checked == true){
            mywindow.document.write('<td><%=member.getIdentity_number().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        
        if($('#remaining_checkbox')[0].checked == true){  
            mywindow.document.write('<td><%=member.getRemaining().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        }
        mywindow.document.write('</tr>');
        <% 
            } //for
        %>
        mywindow.document.write('</tbody></table></body></html>');

        mywindow.document.close(); // necessary for IE >= 10
        mywindow.focus(); // necessary for IE >= 10

        mywindow.print();
        mywindow.close();

        return true;
    }

    $('.button').popup({
        inline: true
    });
    
    function PrintMe(surname, name, address, zipcode, city){
        var mywindow = window.open('', 'my div', 'height=400,width=600');
        mywindow.document.write('<html><head><title>Ετικέτα</title>');
        mywindow.document.write('<style type="text/css"> tr.border_bottom td {border-top: 1px solid #000; text-align:center;} div.outer {line-height: 50px;} div.inner{vertical-align: middle;}</style>');
        mywindow.document.write('<style type="text/css" media="print,screen" >');
        mywindow.document.write('thead { display:table-header-group; }');
        mywindow.document.write('tbody { display:table-row-group; }');
        mywindow.document.write('</style>');
        mywindow.document.write('</head><body>');
        mywindow.document.write('<b>ΠΡΟΣ</b><br/>');
        mywindow.document.write(surname+' '+name+'<br/>');
        mywindow.document.write(address+', '+zipcode+'<br/>');
        mywindow.document.write(city);
        mywindow.document.write('</body></html>');
        
        mywindow.document.close(); // necessary for IE >= 10
        mywindow.focus(); // necessary for IE >= 10

        mywindow.print();
        mywindow.close();

        return true;
    }
    
    function printAllTickets(){
        var mywindow = window.open('', 'my div', 'height=400,width=600');
        mywindow.document.write('<html><head><title>Ετικέτες</title>');
        mywindow.document.write('<style type="text/css"> tr.border_bottom td {border-top: 1px solid #000; text-align:center;} div.outer {line-height: 50px;} div.inner{vertical-align: middle;}</style>');
        mywindow.document.write('<style type="text/css" media="print,screen" >');
        mywindow.document.write('thead { display:table-header-group; }');
        mywindow.document.write('tbody { display:table-row-group; }');
        mywindow.document.write('</style>');
        mywindow.document.write('</head><body>');
        mywindow.document.write('<table>');    
    <% 
        int row=0;
        int column=0;
        for (Member member : members) {
            if(column == 0){
    %>
       mywindow.document.write('<tr>');     
    <%  
            }
    %>
        mywindow.document.write('<td>');
        mywindow.document.write('<b>ΠΡΟΣ</b><br/>');
        mywindow.document.write('<%=member.getSurname().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>'+' '+'<%=member.getName().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>'+'<br/>');
        mywindow.document.write('<%=member.getAddress().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>'+', '+'<%=member.getAddress_zipcode().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>'+'<br/>');
        mywindow.document.write('<%=member.getAddress_municipality().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %>');
        mywindow.document.write('</td>');
    <%
            column++;
            if(column > 2){
                row++;
                column=0;
    %>            
                mywindow.document.write('</tr>');
    <%            
            }
        } 
    %>
        mywindow.document.write('</table>');
        //mywindow.document.write('<b>ΠΡΟΣ</b><br/>');
        //mywindow.document.write(surname+' '+name+'<br/>');
        //mywindow.document.write(address+', '+zipcode+'<br/>');
        //mywindow.document.write(city);
        mywindow.document.write('</body></html>');
        
        mywindow.document.close(); // necessary for IE >= 10
        mywindow.focus(); // necessary for IE >= 10

        mywindow.print();
        mywindow.close();

        return true;
    }

    function sendNewsletter(){
        window.location.href = "mailto:<%=mails %>";
    }
    
    function submitMoney(thememberid){
        var newlocation="InsertEconomics?memberid=" + thememberid;
        window.location.href = newlocation;
    }
    
    function viewPayments(thememberid){
        var newlocation="ViewEconomics?memberid=" + thememberid;
        window.location.href = newlocation;
    }
</script>