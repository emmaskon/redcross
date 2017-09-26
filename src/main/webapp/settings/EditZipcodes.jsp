<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@ page import="java.util.*" %>
<%@ page import="models.Zipcode" %>
<%@ page import="models.User" %>

<%
    ArrayList<Zipcode> zipcodes = (ArrayList<Zipcode>) request.getAttribute("zipcodes");
    String successInsert=(String)request.getAttribute("successInsert");
    
    User user = (User) session.getAttribute("user");
    
    if(user==null){ 
%>
<script>
    location.replace("<jstl_c:url value='/Logout' />");
</script>    
<%
    }
%>

<% if(successInsert.equals("yes")){ %>
    <div class="ui success message" style="display:block" id="insertmsg">
        <i class="close icon"></i>
        <div class="header">
        Επιτυχής εισαγωγή στοιχείων!
        </div>
    </div>
<% } else if (!successInsert.equals("yes") && !successInsert.equals("unknown")) { %> 
    <div class="ui negative message" style="display:block" id="insertmsg">
        <i class="close icon"></i>
        <div class="header">
        Μη επιτυχής εισαγωγή στοιχείων. Παρακαλούμε ελέξτε αν ο ταυδρομικός κώδικος υπάρχει ήδη και εισάγετε σωστά τα δεδομένα.
        </div>
    </div>
<% } else { %>
    <div class="ui negative message" style="display:none" id="insertmsg">
        <i class="close icon"></i>
        <div class="header">
        </div>
    </div>
<% } %>

<div class="ui success message" style="display:none" id="deleteokmsg">
    <i class="close icon"></i>
    <div class="header">
    Επιτυχής διαγραφη στοιχείων!
    </div>
</div>
<div class="ui negative message" style="display:none" id="deletenotokmsg">
    <i class="close icon"></i>
    <div class="header">
    Υπάρχουν μέλη με αυτό τον ΤΚ. Δεν είναι δυνατή η διαγραφή.
    </div>
</div>

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
<div id="zipcodes" class="ui segment">
    <div class="ui form segment">   
        <form action="EditZipcodes" method="post" id="newzipcodeform">
            <div class="fields">
                <div class="six wide field">
                    <label>Προσθήκη Νέου Ταχυδρομικού Κώδικα</label>
                    <input name="newzipcode" id="newzipcode" placeholder="Νέος Ταχυδρομικός Κώδικας" type="text">
                </div>
                <div class="six wide field">
                    <label>&nbsp;</label>
                    <button id="submitbutton" class="ui green submit button">Υποβολή</button>
                    <div id="resetbutton" class="ui red reset button">Εκκαθάριση</div>
                </div>
            </div>
        </form>
    </div>
    <table class="ui celled structured small table" id="records_table">
        <thead>
            <tr>
                <th colspan="13">
                    <div class="nine wide field">
                        <label>ΤΑΧΥΔΡΟΜΙΚΟΙ ΚΩΔΙΚΕΣ</label>
                    </div>
                </th>
            </tr>
            <tr id="titles">
                <th>ΤΚ</th>
                <th>ΓΕΩΓΡΑΦΙΚΗ ΠΕΡΙΟΧΗ</th>
            </tr>
        </thead>
        <tbody id="data">
            <%
                for (Zipcode zipcode : zipcodes) {
            %>
            <tr data-record="<%=zipcode.getZipcode() %>">
                <td><%=zipcode.getZipcode() %>&nbsp;&nbsp;
                    <button class="ui icon button red delete btn circular mini" id="delete<%=zipcode.getZipcode() %>" onclick="deleteZipcode('<%=zipcode.getZipcode() %>')">
                            <i class="remove circle icon"></i>
                        </button>
                        
                        <form class="ui form" id="deletezipcode<%=zipcode.getZipcode() %>">
                            <input type="hidden" name="delzipcodeid<%=zipcode.getZipcode() %>" id="delzipcodeid<%=zipcode.getZipcode() %>" value="<%=zipcode.getZipcode() %>" />
                            <div class="ui small basic modal" id="confirm_delete_<%=zipcode.getZipcode() %>">
                                <div class="ui icon header">
                                    <i class="warning icon"></i>
                                    Επιβεβαίωση
                                </div>
                                <div class="content">
                                    <p>Έιστε βέβαιοι ότι θέλετε να διαγράψετε τον ΤΚ <%=zipcode.getZipcode() %>;</p>
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
                
                </td>
                <td><%=zipcode.getArea() %></td>
            </tr>
            <% 
                } //for
            %>
        </tbody>
    </table>
</div>
        
<script>
    var insertform = $("#newzipcodeform");
    
    $('#resetbutton').on("click", function () {
        $('#newzipcodeform').get(0).reset();
    });
    
    $('#submitbutton').on("click", function () {
        insertform.form({
            newzipcode: {
	            identifier: "newzipcode",
	            rules: [
	                {type: "empty", prompt: "Συμπληρώστε τον νέο ΤΚ"},
                        { type: "length[5]", prompt: "Μη έγκυρος TK" },
                        { type: "maxLength[5]", prompt: "Μη έγκυρος TK" }
	            ]
	        }}, {
            inline: true,
            onSuccess: addNewZipcode
        });
    });
    
    function addNewZipcode(){
        insertform.submit();
    }
    
    $('.message .close').on('click', function() {
        $(this).closest('.message').fadeOut();
    });
    
    function deleteZipcode(thezipcode){
        var $records = $("#records_table tbody tr[data-record]");
        var $dimmer = $(".ui.dimmer");
        
        $('#confirm_delete_'+thezipcode).modal('show');
        
        $('#confirm_delete_'+thezipcode).modal({closable: false, onApprove: function () {
            
            $("#dimmer_text").html("Διαγραφή ΤΚ "+thezipcode+"...");
            $dimmer.dimmer("show");
            
            $.post("<jstl_c:url value='DeleteZipcode'/>", {"zipcode": thezipcode}).done(function (data) {
                
                if(data == undefined){
                    $dimmer.find(".text").html("Προέκυψε σφάλμα...Παρακαλούμε προσπαθήσε ξανά...");
                    setTimeout(function () {
                        $dimmer.dimmer("hide");
                    }, 2000);
                }else{
                    if(data.status == "member_exists"){
                        document.getElementById("deletenotokmsg").style.display="block";
                        document.getElementById("deleteokmsg").style.display="none";
                        document.getElementById("insertmsg").style.display="none";
                    }else{
                        $records.filter("[data-record='" + thezipcode + "']").popup("destroy").remove();
                        $records = $("#records_table tbody tr[data-record]");
                        document.getElementById("deletenotokmsg").style.display="none";
                        document.getElementById("deleteokmsg").style.display="block";
                        document.getElementById("insertmsg").style.display="none";
                    }
                }
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
</script>