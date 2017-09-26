<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@ page import="java.util.*" %>
<%@ page import="models.Doy" %>
<%@ page import="models.User" %>

<%
    ArrayList<Doy> doys = (ArrayList<Doy>) request.getAttribute("doys");
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
        Μη επιτυχής εισαγωγή στοιχείων. Παρακαλούμε ελέξτε αν η ΔΟΥ υπάρχει ήδη και εισάγετε σωστά τα δεδομένα.
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
    Υπάρχουν μέλη με αυτή τη ΔΟΥ. Δεν είναι δυνατή η διαγραφή.
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
<div id="doys" class="ui segment">
    <div class="ui form segment">
        <form action="EditDoys" method="post" id="newdoyform">
            <div class="fields">
                <div class="six wide field">
                    <label>Προσθήκη Νέας ΔΟΥ</label>
                    <input name="newdoy" id="newdoy" placeholder="Νέα ΔΟΥ" type="text" style="text-transform:uppercase;">
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
                        <label>ΔΟΥ</label>
                    </div>
                </th>
            </tr>
            <tr id="titles">
                <th>ΑΑ</th>
                <th>ΤΙΤΛΟΣ ΔΟΥ</th>
            </tr>
        </thead>
        <tbody id="data">
            <%
                int j = 0;

                for (Doy doy : doys) {
                    j++;
            %>
            <tr data-record="<%=doy.getName().replaceAll("\\s", "_").replaceAll("'", "w") %>">
                <td style="width:100px;">
                    <i title="Αύξοντας Αριθμός"><%=j %></i>&nbsp;&nbsp;
                    <button class="ui icon button red delete btn circular mini" id="delete<%=doy.getName().replaceAll("\\s", "_").replaceAll("'", "w") %>" onclick="deleteDoy('<%=doy.getName().replaceAll("\\s", "_").replaceAll("'", "w") %>')">
                            <i class="remove circle icon"></i>
                        </button>
                        
                    <form class="ui form" id="deletedoy<%=doy.getName().replaceAll("\\s", "_").replaceAll("'", "w") %>">
                            <input type="hidden" name="deldoyid<%=doy.getName().replaceAll("\\s", "_").replaceAll("'", "w") %>" id="deldoyid<%=doy.getName().replaceAll("\\s", "_").replaceAll("'", "w") %>" value="<%=doy.getName().replaceAll("\\s", "_").replaceAll("'", "w") %>" />
                            <div class="ui small basic modal" id="confirm_delete_<%=doy.getName().replaceAll("\\s", "_").replaceAll("'", "w") %>">
                                <div class="ui icon header">
                                    <i class="warning icon"></i>
                                    Επιβεβαίωση
                                </div>
                                <div class="content">
                                    <p>Έιστε βέβαιοι ότι θέλετε να διαγράψετε τη ΔΟΥ <%=doy.getName() %>;</p>
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
                <td><%=doy.getName() %></td>
            </tr>
            <% 
                } //for
            %>
        </tbody>
    </table>
</div>
        
<script>
    var insertform = $("#newdoyform");
    
    $('#resetbutton').on("click", function () {
        $('#newdoyform').get(0).reset();
    });
    
    $('#submitbutton').on("click", function () {
        insertform.form({
            newdoy: {
	            identifier: "newdoy",
	            rules: [
	                {type: "empty", prompt: "Συμπληρώστε τον τίτλο της νέας ΔΟΥ"}
	            ]
	        }}, {
            inline: true,
            onSuccess: addNewDoy
        });
    });
    
    function addNewDoy(){
        insertform.submit();
    }
    
    $('.message .close').on('click', function() {
        $(this).closest('.message').fadeOut();
    });
    
    function deleteDoy(thedoyid){
        var $records = $("#records_table tbody tr[data-record]");
        var $dimmer = $(".ui.dimmer");

        $('#confirm_delete_'+thedoyid).modal('show');

        $('#confirm_delete_'+thedoyid).modal({closable: false, onApprove: function () {

            $("#dimmer_text").html("Διαγραφή ΔΟΥ...");
            $dimmer.dimmer("show");
            
            $.post("<jstl_c:url value='DeleteDoy'/>", {"doyid": thedoyid}).done(function (data) {
                
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
                        $records.filter("[data-record='" + thedoyid + "']").nextAll("tr").find("i[title='Αύξοντας Αριθμός']").each(function () {
                            var sn = parseInt($(this).html());
                            $(this).html(sn - 1);
                        });

                        $records.filter("[data-record='" + thedoyid + "']").popup("destroy").remove();
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