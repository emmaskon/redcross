<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@ page import="java.util.*" %>
<%@ page import="models.Municipality" %>
<%@ page import="models.User" %>

<%
    ArrayList<Municipality> municipalities = (ArrayList<Municipality>) request.getAttribute("municipalities");
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
        Μη επιτυχής εισαγωγή στοιχείων. Παρακαλούμε ελέξτε αν ο δήμος υπάρχει ήδη και εισάγετε σωστά τα δεδομένα.
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
    Υπάρχουν μέλη σε αυτό το δήμο. Δεν είναι δυνατή η διαγραφή.
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
<div id="municipalities" class="ui segment">
    <div class="ui form segment">   
        <form action="EditMunicipalities" method="post" id="newmunicipalityform">
            <div class="fields">
                <div class="six wide field">
                    <label>Προσθήκη Νέου Δήμου</label>
                    <input name="newmunicipality" id="newmunicipality" placeholder="Νέος Δήμος" type="text" style="text-transform:uppercase;">
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
                        <label>ΔΗΜΟΙ</label>
                    </div>
                </th>
            </tr>
            <tr id="titles">
                <th>ΑΑ</th>
                <th>ΟΝΟΜΑ ΔΗΜΟΥ</th>
            </tr>
        </thead>
        <tbody id="data">
            <%
                int j = 0;

                for (Municipality municipality : municipalities) {
                    j++;
            %>
            <tr data-record="<%=municipality.getName().replaceAll("\\s", "_") %>">
                <td style="width:100px;">
                    <i title="Αύξοντας Αριθμός"><%=j %></i>&nbsp;&nbsp;
                    <button class="ui icon button red delete btn circular mini" id="delete<%=municipality.getName().replaceAll("\\s", "_") %>" onclick="deleteMunicipality('<%=municipality.getName().replaceAll("\\s", "_") %>')">
                            <i class="remove circle icon"></i>
                        </button>
                        
                        <form class="ui form" id="deletemunicipality<%=municipality.getName().replaceAll("\\s", "_") %>">
                            <input type="hidden" name="delmunicipalityid<%=municipality.getName().replaceAll("\\s", "_") %>" id="delmunicipalityid<%=municipality.getName().replaceAll("\\s", "_") %>" value="<%=municipality.getName().replaceAll("\\s", "_") %>" />
                            <div class="ui small basic modal" id="confirm_delete_<%=municipality.getName().replaceAll("\\s", "_") %>">
                                <div class="ui icon header">
                                    <i class="warning icon"></i>
                                    Επιβεβαίωση
                                </div>
                                <div class="content">
                                    <p>Έιστε βέβαιοι ότι θέλετε να διαγράψετε το δήμο <%=municipality.getName() %>;</p>
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
                <td><%=municipality.getName() %></td>
            </tr>
            <% 
                } //for
            %>
        </tbody>
    </table>
</div>
        
<script>
    var insertform = $("#newmunicipalityform");
    
    $('#resetbutton').on("click", function () {
        $('#newmunicipalityform').get(0).reset();
    });
    
    $('#submitbutton').on("click", function () {
        insertform.form({
            newmunicipality: {
	            identifier: "newmunicipality",
	            rules: [
	                {type: "empty", prompt: "Συμπληρώστε το όνομα του νέου δήμου"}
	            ]
	        }}, {
            inline: true,
            onSuccess: addNewMunicipality
        });
    });
    
    function addNewMunicipality(){
        insertform.submit();
    }
    
    $('.message .close').on('click', function() {
        $(this).closest('.message').fadeOut();
    });
    
    function deleteMunicipality(themunicipalityid){
        var $records = $("#records_table tbody tr[data-record]");
        var $dimmer = $(".ui.dimmer");
        
        $('#confirm_delete_'+themunicipalityid).modal('show');
        
        $('#confirm_delete_'+themunicipalityid).modal({closable: false, onApprove: function () {
            
            $("#dimmer_text").html("Διαγραφή δήμου...");
            $dimmer.dimmer("show");
            
            $.post("<jstl_c:url value='DeleteMunicipality'/>", {"municipalityid": themunicipalityid}).done(function (data) {
                
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
                        $records.filter("[data-record='" + themunicipalityid + "']").nextAll("tr").find("i[title='Αύξοντας Αριθμός']").each(function () {
                            var sn = parseInt($(this).html());
                            $(this).html(sn - 1);
                        });

                        $records.filter("[data-record='" + themunicipalityid + "']").popup("destroy").remove();
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