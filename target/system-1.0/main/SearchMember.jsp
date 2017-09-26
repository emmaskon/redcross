<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    
    User user = (User) session.getAttribute("user");
%>
<div class="ui form segment">
    <h4 class="ui dividing header">Αναζήτηση</h4>
    <form action="SearchMember" method="post" id="insert-form">
        <div class="fields">
            <div class="six wide field">
                <label style="text-align:center;font-style:italic;text-decoration:underline;">ΠΡΟΣΩΠΙΚΑ ΣΤΟΙΧΕΙΑ</label>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Επίθετο</label>
                        <input name="last-name" id="lastname" placeholder="Επίθετο" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Όνομα</label>
                        <input name="first-name" id="firstname" placeholder="Όνομα" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Όνομα Πατρός</label>
                        <input name="father-name" placeholder="Όνομα Πατρός" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>ΑΜΚΑ</label>
                        <input name="amka" id="amka" placeholder="ΑΜΚΑ" type="text">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>ΑΦΜ</label>
                        <input name="afm" id="afm" placeholder="ΑΦΜ" type="text">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>ΔΟΥ</label>
                        <div class="ui selection dropdown">
                            <input type="hidden" name="doy" id="doy">
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
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>ΑΔΤ</label>
                        <input name="adt" placeholder="ΑΔΤ" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Επάγγελμα</label>
                        <div class="ui action input" >
                            <input name="job" id="job" placeholder="ΕΠΆΓΓΕΛΜΑ" type="text">
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
                    </div><div class="field"></div><!--for layout issues-->
                </div>
            </div>
            <div class="five wide field">
                <label style="text-align:center;font-style:italic;text-decoration:underline;">ΔΙΕΥΘΥΝΣΗ ΚΑΙ ΣΤΟΙΧΕΙΑ ΕΠΙΚΟΙΝΩΝΙΑΣ</label>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Διεύθυνση</label>
                        <input name="address" placeholder="Διεύθυνση" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>ΤΚ</label>
                        <div class="ui action input">
                            <input name="zip-code" id="zipcode" placeholder="ΤΚ" type="text">
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
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Περιοχή</label>
                        <input name="area" placeholder="Περιοχή" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Δήμος</label>
                        <div class="ui selection dropdown">
                            <input type="hidden" name="municipality" id="municipality">
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
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Νομός</label>
                        <div class="ui selection dropdown">
                            <input type="hidden" name="county" id="county">
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
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Περιφέρεια</label>
                        <div class="ui selection dropdown">
                            <input type="hidden" name="state" id="state">
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
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Τηλέφωνο</label>
                        <input name="phone" placeholder="Τηλέφωνο" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Κινητό</label>
                        <input name="cell" placeholder="Κινητό" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Email</label>
                        <input name="email" placeholder="EMAIL" type="text">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Fax</label>
                        <input name="fax" placeholder="Fax" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
            </div>
            <div class="five wide field">
                <label style="text-align:center;font-style:italic;text-decoration:underline;">ΛΟΙΠΑ ΣΤΟΙΧΕΙΑ</label>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Κωδικός μέλους</label>
                        <input name="member_id" id="member_id" placeholder="Κωδικός μέλους" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Τμήμα</label>
                        <div class="ui selection dropdown">
                            <input type="hidden" name="department" id="department">
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
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <div class="date field">
                         <label>Ημερομηνία Αίτησης/Εγγραφής</label>
                         <input id="datepicker2" name="applicationdate" placeholder="xx/xx/xxxx" type="text" readonly style="cursor:pointer">
                        </div>
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <%  if(user.getRole().equals("kd") || user.getRole().equals("su") || user.getRole().equals("pt")) { %>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Κατάσταση προώθησης εγγραφής από το Π.Τ.</label>
                        <input type="radio" id="ptstatus" name="ptstatus" value="1"> Ναι &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="ptstatus" name="ptstatus" value="0"> Όχι &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="ptstatus" name="ptstatus" value="" checked> Αδιάφορο
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <% } %>
                <%  if(user.getRole().equals("kd") || user.getRole().equals("su")) { %>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Κατάσταση αποδοχής εγγραφής από την Κ.Δ.</label>
                        <input type="radio" id="kdstatus" name="kdstatus" value="1"> Ναι &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="kdstatus" name="kdstatus" value="0"> Όχι &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="kdstatus" name="kdstatus" value="" checked> Αδιάφορο
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <div class="fields">
                    <div class="sixteen wide field">
                        <div class="date field">
                         <label>Ημερομηνία Απόφασης Κ.Δ.</label>
                         <input id="datepicker1" name="decisiondatekd" placeholder="xx/xx/xxxx" type="text" readonly style="cursor:pointer">
                        </div>
                    </div><div class="field"></div><!--for layout issues-->
                </div>
                <% } %>
                <div class="fields">
                    <div class="sixteen wide field">
                        <label>Υπόλοιπο Οφειλής</label>
                        <input name="remainingcost" id="remainingcost" placeholder="Υπόλοιπο Οφειλής" type="text" style="text-transform:uppercase;">
                    </div><div class="field"></div><!--for layout issues-->
                </div>
            </div>
        </div>
        <button id="submitbutton" class="ui green submit button">Αναζήτηση</button>
        <div id="resetbutton" class="ui red reset button">Επαναφορά</div>
    </form>
</div>

<!--Only for developer user -->
<!--<form action="IncreaseMoney" method="post" id="increasemoney-form">
    <button id="increasemoney" class="ui orange button">Αύξηση ατιμολόγητου</button>
</form>-->

<script>
    var insertform = $("#insert-form");
    
    $('.ui.dropdown').dropdown();
    
    $('#resetbutton').on("click", function () {
        $('#insert-form').get(0).reset();
    });
    
    //Only for developer user 
    /*$('#increasemoney').on("click", function () {
        $('#increasemoney-form').submit();
    });*/
    
    $('#submitbutton').on("click", function () {
        $('#insert-form').submit();
    });
    
    $('.message .close').on('click', function() {
        $(this).closest('.message').fadeOut();
    });
    
    $(function () {
        $("#datepicker1").datepicker();
    });
    
    $(function () {
        $("#datepicker2").datepicker();
    });
    
    $(function () {
        $("#datepicker3").datepicker();
    });
    
    $('.application_date_not_exists').popup({
        inline: true
    });
    
    $("#jobselect").on("change", function (evt) {
	$("#job").val($("#jobselect").val());
    });
    
    $("#zipcodeselect").on("change", function (evt) {
	$("#zipcode").val($("#zipcodeselect").val());
    });
</script>

<style>
    .ui-datepicker { font-size: 9pt !important; }
</style>
