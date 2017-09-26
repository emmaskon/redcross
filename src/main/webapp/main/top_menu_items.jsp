<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.User"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="jstl_fn"%>
<%
User user = (User) session.getAttribute("user");
    
if(user==null){ 
%>
<script>
    location.replace("<jstl_c:url value='/Logout' />");
</script>    
<%
}
%>
<a class="item" href="<jstl_c:url value='/main/SearchMember'/>"><i class="home icon"></i>Αρχική</a>
<div class="ui dropdown item">
    Διαχείριση μελών
    <div class="menu">
        <a class="item" href="<jstl_c:url value='/main/ViewAllMembers?search=select'/>">Πίνακας μελών</a>
        <a class="item" href="<jstl_c:url value='/main/InsertMember'/>">Προσθήκη μέλους</a>
        <a class="item" href="<jstl_c:url value='/main/ViewPendingSubscriptions'/>">Αιτήσεις σε αναμονή</a>
        <a class="item" href="<jstl_c:url value='/main/ViewRepeatedSubscriptions?search=select'/>">Επαναλαμβανόμενες αιτήσεις</a>
    </div>
</div>

<div class="ui dropdown item">
    Οικονομικές Κινήσεις
    <div class="menu">
        <a class="item" href="<jstl_c:url value='/main/ViewEconomics?search=select'/>">Προβολή Οικονομικών Kινήσεων</a>
        <a class="item" href="<jstl_c:url value='/main/InsertEconomics'/>">Καταχώρηση Πληρωμών</a>
    </div>
</div>

<div class="ui dropdown item">
    Ρυθμίσεις
    <div class="menu">
        <a class="item" href="<jstl_c:url value='/settings/ChangePassword'/>">Αλλαγή Κωδικού Πρόσβασης</a>
        <%  if(user.getRole().equals("su")){ %>
        <a class="item" href="<jstl_c:url value='/settings/SetDepartmentPassword'/>">Καθορισμός Κωδικού Πρόσβασης Τμήματος</a>
        <div class="item">
            <i class="dropdown icon"></i>
            <span class="text">Επεξεργασία Βοηθητικών Δεδομένων</span>
            <div class="right menu">
              <a class="item" href="<jstl_c:url value='/settings/EditMunicipalities'/>">Επεξεργασία Δήμων</a>
              <a class="item" href="<jstl_c:url value='/settings/EditDoys'/>">Επεξεργασία ΔΟΥ</a>
              <a class="item" href="<jstl_c:url value='/settings/EditZipcodes'/>">Επεξεργασία TK</a>
              <a class="item" href="<jstl_c:url value='/settings/EditJobs'/>">Επεξεργασία Επαγγελμάτων</a>
            </div>
        </div>
        <%  } %>
    </div>
</div>
<script>
    $(".ui.dropdown").dropdown();
</script>

