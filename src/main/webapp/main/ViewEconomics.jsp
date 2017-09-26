<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@ page import="java.util.*" %>
<%@ page import="models.User" %>
<%@ page import="models.Member" %>
<%@ page import="models.Department" %>
<%@ page import="models.Member_Economic" %>
<%@ page import="models.Payment_Type" %>
<%@ page import="models.Subscription_Type" %>

<%
    ArrayList<Department> departments = (ArrayList<Department>) request.getAttribute("departments");
    //ArrayList<Payment_Type> payments = (ArrayList<Payment_Type>) request.getAttribute("payments");
    ArrayList<Member_Economic> member_economics = (ArrayList<Member_Economic>) request.getAttribute("member_economics");
    //ArrayList<Subscription_Cost> subscriptions = (ArrayList<Subscription_Cost>) request.getAttribute("subscriptions");
    Department current_department = (Department) request.getAttribute("department");
    String member_id = (String) request.getAttribute("member_id");
    String specific_search = (String) request.getAttribute("specific_search");
    String department_name = (String) request.getAttribute("department_name");
    String pending = (String) request.getAttribute("pending");
    String fromdate = (String) request.getAttribute("fromdate");
    String todate = (String) request.getAttribute("todate");
    String total_paid = (String) request.getAttribute("total_paid");
    User user = (User) session.getAttribute("user");
    
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
      Επιλέξτε ένα τμήμα για να εμφανίσετε τα οικονομικά στοιχεία του!
    </div>
  </div>
</div>
<% } %>
<div id="members" class="ui segment">
    <% if(member_id.equals("")){ %>
    <h4 class="ui dividing header">Πίνακας Οικονομικών Kινήσεων Μελών</h4>
    <% }else{ %>
    <h4 class="ui dividing header">Πίνακας Οικονομικών Kινήσεων Μέλους <i>(<%=member_id %>)</i></h4>
    <% } %>
    <table class="ui celled structured small table">
        <thead>
            <% if(member_id.equals("")){ %>
            <tr>
             <div class="ui form segment">   
             <div class="inline fields">
                    <div class="four wide field">
                        <div class="date field">
                         <label>Από:</label>
                         <input id="fromdate" name="fromdate" placeholder="xx/xx/xxxx" type="date" value="<%=fromdate %>">
                        </div>
                    </div>
                    <div class="four wide field">
                        <div class="date field">
                         <label>Έως:</label>
                         <input id="todate" name="todate" placeholder="xx/xx/xxxx" type="date" value="<%=todate %>">
                        </div>
                    </div>
            </div>
            </div>
            </tr>
            <% } %>
            <tr>
                <th colspan="13">
                    <div class="nine wide field">
                        <% if(member_id.equals("")){ %>
                            <label>ΕΓΓΕΓΡΑΜΜΕΝΑ ΜΕΛΗ&nbsp;&nbsp;</label>
                            <% if(department_name.equals("select")){ %>
                            <div class="ui selection dropdown" style="width:400px;">
                            <% }else{ %>
                            <div class="ui selection dropdown" style="width:400px;">
                            <% } %>
                                <% if((!department_name.equals("all"))&& (!department_name.equals("select")) 
                                        && current_department.getType()!=null && current_department.getStateId()!=null  && current_department.getCityId()!=null
                                        && (!current_department.getType().equals("")) && (!current_department.getStateId().equals("")) && (!current_department.getCityId().equals(""))
                                     ){ %>
                                <input type="hidden" name="showspecific" id="showspecific" value="<%=current_department.getStateId() %>.<%=current_department.getCityId() %>">
                                <div class="default text"><%=current_department.getType() %> <%=department_name %> (<%=current_department.getStateId() %><%=current_department.getCityId() %>)</div>
                                <% }else if(department_name.equals("select")){ %>
                                <input type="hidden" name="showspecific" id="showspecific" value="select">
                                <div class="default text">Επιλέξτε ένα τμήμα</div>
                                <% }else{ %>
                                <input type="hidden" name="showspecific" id="showspecific" value="all">
                                <div class="default text">ΌΛΑ</div>
                                <% } %>
                                <i class="dropdown icon"></i>
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
                        <% } %>
                        &nbsp;<i>Συνολικό καταβληθέν ποσό: <%=total_paid %> €</i>
                    </div>
                </th>
            </tr>
            
            <tr id="titles">
                <th>ΑΑ</th>
                <th>ΚΩΔΙΚΟΣ ΜΕΛΟΥΣ</th>
                <th>ΕΠΩΝΥΜΟ</th>
                <th>ΟΝΟΜΑ</th>
                <th>ΤΡΟΠΟΣ ΠΛΗΡΩΜΗΣ</th>                 
                <th>ΗΜΕΡΟΜΗΝΙΑ ΠΛΗΡΩΜΗΣ</th>
                <th>ΑΡΙΘΜΟΣ ΠΑΡΑΣΤΑΤΙΚΟΥ</th>
                <th>ΤΥΠΟΣ ΣΥΝΔΡΟΜΗΣ</th>
                <th>ΚΑΤΑΒΛΗΘΕΝ ΠΟΣΟ</th>
                <th>ΠΛΗΡΩΤΕΟ ΕΤΟΣ ΣΥΝΔΡΟΜΗΣ</th>
            </tr>
        </thead>
        <tbody id="data">
            <%
                int j = 0;
                
                if(member_economics!=null){
                for (Member_Economic member_economic : member_economics) {
                    j++;
            %>
            <tr>
                <td style="min-width:90px">
                    <i><%=j %>&nbsp;&nbsp;
                        <button class="ui icon button editbtn" id="<%=member_economic.getMember_id() %>.<%=member_economic.getInvoice() %>" onclick="editMemberEconomic('<%=member_economic.getMember_id() %>.<%=member_economic.getInvoice() %>')">
                            <i class="edit icon"></i>
                        </button>  
                    </i>
                </td>
                <td><%=member_economic.getMember_id() %></td>
                <td><%=member_economic.getMember().getSurname() %></td>
                <td><%=member_economic.getMember().getName() %></td>
                <td style="min-width:160px;">
                    <%=member_economic.getPayment_type() %>
                    <% if(member_economic.getVoucher_file_id()>0){ %>&nbsp;
                    <div style="width:40px;" class="ui circular vertical animated icon button" id="existing_file_open_btn<%=member_economic.getMember_id() %>" onclick="openVoucher('<%=member_economic.getVoucher_file_id() %>.<%=member_economic.getVoucher_file_type() %>')">
                        <div class="visible content"><i class="file text outline icon"></i></div>
                        <div class="hidden content">&nbsp;<i class="file text green icon"></i></div>
                    </div>
                    <div class="ui flowing popup top left transition hidden">
                        <table class="ui celled structured small table">
                            <thead>
                                <tr>
                                    <td><b>Προβολή παραστατικού τραπέζης</b></td>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <% } %>
                </td>
                <td><%=member_economic.getPayment_date() %></td>
                <td>
                    <div style="width:40px;" class="ui circular vertical animated icon button" onclick="PrintInvoice('invoice<%=member_economic.getInvoice() %>')">
                        <div class="visible content"><i class="payment icon"></i></div>
                        <div class="hidden content">&nbsp;<i class="print green icon"></i></div>
                    </div>
                    <div class="ui flowing popup top left transition hidden">
                        <table class="ui celled structured small table">
                        <thead>
                            <tr>
                                <th>Εκτύπωση απόδειξης</th>
                            </tr>
                        </thead>
                        <tbody id="invoice<%=member_economic.getInvoice() %>">
                            <style>
                                .invoice_line {
                                    font-weight: bold;
                                }
                            </style>
                            <tr>
                                <td>
                                    <img src="<jstl_c:url value='/images/redcross.png' />" alt="Λογότυπο συστήματος" style="width: 150px; height: 100px">
                                    <p style="text-align:center;font-size:16px;font-weight:bold;">
                                        ΑΠΟΔΕΙΞΗ
                                    </p>
                                    <p style="min-width:350px;font-size:15px;">
                                        Ο/Η <font class="invoice_line"><%=member_economic.getMember().getSurname() %> <%=member_economic.getMember().getName() %></font><br/>
                                        Διεύθυνση <font class="invoice_line"><%=member_economic.getMember().getAddress() %>, <%=member_economic.getMember().getAddress_municipality() %>, TK: <%=member_economic.getMember().getAddress_zipcode() %></font><br/>
                                        Μέλος ΕΕΣ με Α.Μ. <font class="invoice_line"><%=member_economic.getMember_id() %></font><br/>
                                        κατέβαλε το ποσό <font class="invoice_line"><%=member_economic.getPaid() %></font> € για συνδρομή έτους <font class="invoice_line"><%=member_economic.getSubscription_year() %></font><br/>
                                        <p style="min-width:100%;font-size:15px;">
                                        &nbsp;&nbsp;&nbsp;&nbsp;Ημερομηνία   <font style="float:right;">Ο ταμίας&nbsp;&nbsp;&nbsp;&nbsp;</font><br/>
                                        <font class="invoice_line">&nbsp;&nbsp;&nbsp;&nbsp;<%=member_economic.getPayment_date() %></font>
                                        </p>
                                    </p>
                                </td>
                            </tr>
                        </tbody>
                        </table>
                    </div>
                    <% if(!member_economic.getInvoice().startsWith("auto")){ %>
                    <%=member_economic.getInvoice() %>
                    <% } %>
                </td>
                <td><%=member_economic.getSubscription_type() %></td>
                <td><%=member_economic.getPaid() %> €</td>
                <td><%=member_economic.getSubscription_year() %></td>
            </tr>
            <% 
                } //for
                }
            %>
        </tbody>
    </table>
</div>
<button class="ui green button" id="printbtn">
    Εκτύπωση οικονομικών κινήσεων
</button>
<script type="text/javascript">

    $('.ui.dropdown').dropdown();
    
    function editMemberEconomic(theid){
        var newlocation="<jstl_c:url value='/main/InsertEconomics'/>"+"?edit="+ theid;
        window.location.href = newlocation;
    }
    
    $("#showspecific").on('change', function() {
        dateRangeChanged();
    });
    
    $("#fromdate").on('change', function() {
        dateRangeChanged();
    });
    
    $("#todate").on('change', function() {
        dateRangeChanged();
    });
    
    function dateRangeChanged(){
        var newlocation="<jstl_c:url value='/main/ViewEconomics'/>"+"?search="+ $("#showspecific").val()+"&fromdate="+ $("#fromdate").val()+"&todate="+ $("#todate").val();
        
        <% if(pending.equals("yes")){ %>
            newlocation="<jstl_c:url value='/main/ViewPendingSubscriptions'/>"+"?search="+ $("#showspecific").val()+"&fromdate="+ $("#fromdate").val()+"&todate="+ $("#todate").val();
        <% } %>
        
        window.location.href = newlocation;
    }

    $('#printbtn').on('click', function(event) {
        Popup($("#titles").html(), $("#data").html());
    });
    
    function openVoucher(filename){
        var newlocation="<jstl_c:url value='/main/VoucherDownload'/>"+"?filename="+filename;
        window.location.href = newlocation;
    }

    function Popup(title, data) 
    {
        var mywindow = window.open('', 'my div', 'height=400,width=600');
        
        <% if(member_id.equals("")){ %>
        mywindow.document.write('<html><head><title>Οικονομικές Kινήσεις Μελών</title>');
        mywindow.document.write('<h4>Οικονομικές Kινήσεις Μελών<br>');
        <% }else{ %>
        mywindow.document.write('<html><head><title>Οικονομικές Kινήσεις Μέλους <i>(<%=member_id %>)</title>');
        mywindow.document.write('<h4>Οικονομικές Kινήσεις Μέλους <i>(<%=member_id %>)<br>');
        <% } %>
        
        <% if(member_id.equals("")){ %>
        <% if((!department_name.equals("all"))&& (!department_name.equals("select")) 
                && current_department.getType()!=null && current_department.getStateId()!=null  && current_department.getCityId()!=null
                && (!current_department.getType().equals("")) && (!current_department.getStateId().equals("")) && (!current_department.getCityId().equals(""))
             ){ %>
        mywindow.document.write('<%=current_department.getType() %> <%=department_name %> (<%=current_department.getStateId() %><%=current_department.getCityId() %>)');
        <% }else if(department_name.equals("select")){ %>
        mywindow.document.write('Επιλέξτε ένα τμήμα');
        <% }else{ %>
        mywindow.document.write('ΌΛΑ');
        <% } %>
        <% } %>
        
        <% if(member_id.equals("")){ %>
        mywindow.document.write(' , <i>Συνολικό καταβληθέν ποσό: <%=total_paid %> €</i>');
        <% }else{ %>
        mywindow.document.write('<i>Συνολικό καταβληθέν ποσό: <%=total_paid %> €</i>');
        <% } %>
        mywindow.document.write('</h4><style type="text/css"> tr.border_bottom td {border-top: 1px solid #000; text-align:center;} div.outer {line-height: 50px;} div.inner{vertical-align: middle;}</style>');
        mywindow.document.write('<style type="text/css" media="print,screen">');
        mywindow.document.write('thead { display:table-header-group; }');
        mywindow.document.write('tbody { display:table-row-group; }');
        mywindow.document.write('</style>');
        mywindow.document.write('</head><body>');

        mywindow.document.write('<table><thead><tr class="border_bottom">');
        
        //mywindow.document.write(title);
        mywindow.document.write('<tr>');
        mywindow.document.write('<th>ΑΑ</th>');
        mywindow.document.write('<th>ΚΩΔΙΚΟΣ ΜΕΛΟΥΣ</th>');
        mywindow.document.write('<th>ΕΠΩΝΥΜΟ</th>');
        mywindow.document.write('<th>ΟΝΟΜΑ</th>');
        mywindow.document.write('<th>ΤΡΟΠΟΣ ΠΛΗΡΩΜΗΣ</th>');            
        mywindow.document.write('<th>ΗΜΕΡΟΜΗΝΙΑ ΠΛΗΡΩΜΗΣ</th>');
        mywindow.document.write('<th>ΑΡΙΘΜΟΣ ΠΑΡΑΣΤΑΤΙΚΟΥ</th>');
        mywindow.document.write('<th>ΤΥΠΟΣ ΣΥΝΔΡΟΜΗΣ</th>');
        mywindow.document.write('<th>ΚΑΤΑΒΛΗΘΕΝ ΠΟΣΟ</th>');
        mywindow.document.write('<th>ΠΛΗΡΩΤΕΟ ΕΤΟΣ ΣΥΝΔΡΟΜΗΣ</th>');
        mywindow.document.write('</tr>');

        mywindow.document.write('</tr></thead><tbody>');
        //mywindow.document.write(data);
        
        <%
            j = 0;

            if(member_economics!=null){
            for (Member_Economic member_economic : member_economics) {
                j++;
        %>
        mywindow.document.write('<tr>');
        mywindow.document.write('<td style="min-width:90px">');
        mywindow.document.write('<%=j %>');
        mywindow.document.write('</td>');
        mywindow.document.write('<td><%=member_economic.getMember_id().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        mywindow.document.write('<td><%=member_economic.getMember().getSurname().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        mywindow.document.write('<td><%=member_economic.getMember().getName().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        mywindow.document.write('<td><%=member_economic.getPayment_type().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        mywindow.document.write('<td><%=member_economic.getPayment_date().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        mywindow.document.write('<td><%=member_economic.getInvoice().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        mywindow.document.write('<td><%=member_economic.getSubscription_type().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        mywindow.document.write('<td><%=Integer.toString(member_economic.getPaid()).replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %> €</td>');
        mywindow.document.write('<td><%=member_economic.getSubscription_year().replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("'", "") %></td>');
        
        mywindow.document.write('</tr>');
        <% 
            } //for
            }
        %>
        mywindow.document.write('</tbody></table></body></html>');

        mywindow.document.close(); // necessary for IE >= 10
        mywindow.focus(); // necessary for IE >= 10

        mywindow.print();
        mywindow.close();

        return true;
    }

    function PrintInvoice(invoice_id){
        var mywindow = window.open('', 'my div', 'height=400,width=600');
        
        mywindow.document.write($("#"+invoice_id).html());
        
        mywindow.document.close(); // necessary for IE >= 10
        mywindow.focus(); // necessary for IE >= 10

        mywindow.print();
        mywindow.close();
    }

    $(function () {
        $("#fromdate").datepicker();
    });
    
    $(function () {
        $("#todate").datepicker();
    });
    
    $('.button').popup({
        inline: true
    });
</script>

<style>
    .ui-datepicker { font-size: 9pt !important; }
</style>