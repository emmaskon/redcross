<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@ page import="java.util.*" %>
<%@ page import="models.User" %>
<%@ page import="models.Log" %>

<%
    ArrayList<Log> logs = (ArrayList<Log>) request.getAttribute("logs");
    String fromdate = (String) request.getAttribute("fromdate");
    String todate = (String) request.getAttribute("todate");
    
    User user = (User) session.getAttribute("user");
    
    if(user==null){ 
%>
<script>
    location.replace("<jstl_c:url value='/Logout' />");
</script>    
<%
    }
%>

<div id="log_div" class="ui segment">
    <h4 class="ui dividing header">Αρχείο Καταγραφής</h4>
    <table class="ui celled structured small table">
        <thead>
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
            <tr>
                <th colspan="13">
                    <div class="nine wide field">
                        <label>ΑΡΧΕΙΟ ΚΑΤΑΓΡΑΦΗΣ</label>
                    </div>
                </th>
            </tr>
            <tr id="titles">
                <th>ΑΑ</th>
                <th>ΟΝΟΜΑ ΧΡΗΣΤΗ</th>
                <th>ΗΜΕΡΟΜΗΝΙΑ</th>
                <th>ΩΡΑ</th>
                <th>ΕΝΕΡΓΕΙΑ</th>
            </tr>
        </thead>
        <tbody id="data">
            <%
                int j = 0;

                for (Log log : logs) {
                    j++;
            %>
            <tr>
                <td>
                    <i><%=j %></i>
                </td>
                <td><%=log.getUsername() %></td>
                <td><%=log.getDate() %></td>
                <td><%=log.getTime() %></td>
                <td><%=log.getAction() %></td>
            </tr>
            <% 
                } //for
            %>
        </tbody>
    </table>
    <button class="ui green button" id="printbtn" onclick="PrintLog('log_div')">Εκτύπωση</button>
</div>
        
<script>
$(function () {
    $("#fromdate").datepicker();
});

$(function () {
    $("#todate").datepicker();
});    
    
$("#fromdate").on('change', function() {
    dateRangeChanged();
});

$("#todate").on('change', function() {
    dateRangeChanged();
});

function dateRangeChanged(){
    var newlocation="<jstl_c:url value='/log/ViewLog'/>"+"?fromdate="+ $("#fromdate").val()+"&todate="+ $("#todate").val();

    window.location.href = newlocation;
}

function PrintLog(log_div){
    var mywindow = window.open('', 'my div', 'height=400,width=600');

    mywindow.document.write($("#"+log_div).html());

    mywindow.document.close(); // necessary for IE >= 10
    mywindow.focus(); // necessary for IE >= 10

    mywindow.print();
    mywindow.close();
}

</script>