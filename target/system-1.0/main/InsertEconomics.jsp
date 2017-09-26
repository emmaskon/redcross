<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@ page import="java.util.*" %>
<%@ page import="models.User" %>
<%@ page import="models.Department" %>
<%@ page import="models.State" %>
<%@ page import="models.County" %>
<%@ page import="models.Member" %>
<%@ page import="models.Member_Economic" %>
<%@ page import="models.Payment_Type" %>
<%@ page import="models.Subscription_Type" %>
<%
    ArrayList<Department> departments = (ArrayList<Department>) request.getAttribute("departments");
    ArrayList<State> states = (ArrayList<State>) request.getAttribute("states");
    ArrayList<County> counties = (ArrayList<County>) request.getAttribute("counties");
    ArrayList<Payment_Type> payment_types = (ArrayList<Payment_Type>) request.getAttribute("payment_types");
    ArrayList<Subscription_Type> subscription_types = (ArrayList<Subscription_Type>) request.getAttribute("subscription_types");
    //ArrayList<Member_Economic> economics = (ArrayList<Member_Economic>) request.getAttribute("economics");
    //ArrayList<Subscription_Cost> subscriptions = (ArrayList<Subscription_Cost>) request.getAttribute("subscriptions");
    //Department current_department = (Department) request.getAttribute("department");
    String successInsert=(String)request.getAttribute("successInsert");
    String m_id=(String)request.getAttribute("m_id");
    String registration_payment=(String)request.getAttribute("registration_payment");
    String edit_mode=(String)request.getAttribute("edit_mode");
    Member_Economic member_economic=(Member_Economic)request.getAttribute("member_economic");
    
    User user = (User) session.getAttribute("user");
%>
<% if(successInsert.equals("yes")){ %>
    <div class="ui success message">
        <i class="close icon"></i>
        <div class="header">
        Επιτυχής εισαγωγή στοιχείων!
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
    Προσθήκη Οικονομικών Στοιχείων Μέλους 
    </h4>
    <form action="InsertEconomics" method="post" id="insert-form" enctype="multipart/form-data">
        <input type="hidden" id="edit_mode" name="edit_mode" value="<%=edit_mode %>" >
        <input type="hidden" id="old_invoice" name="old_invoice" 
        <% if(edit_mode.equals("yes") && (member_economic.getInvoice()!=null)){ %>   
            value="<%=member_economic.getInvoice() %>"
        <% } %>  
        >
        <input type="hidden" id="old_voucher_file_id" name="old_voucher_file_id" 
        <% if(edit_mode.equals("yes")){ %>   
            value="<%=member_economic.getVoucher_file_id() %>"
        <% } %>  
        >
        <input type="hidden" id="old_subscription_type" name="old_subscription_type" 
        <% if(edit_mode.equals("yes") && (member_economic.getSubscription_type()!=null)){ %>   
            value="<%=member_economic.getSubscription_type() %>"
        <% } %>  
        >
        <div class="fields">
            <div class="four wide field">
                <label>Αριθμός Μητρώου Μέλους</label>
                <% if(!edit_mode.equals("yes")){ %>
                <div class="ui action input" >
                <% } %> 
                    <input name="member_id" id="member_id" placeholder="Αριθμός Μητρώου Μέλους" type="text"
                        <% if(edit_mode.equals("yes") && member_economic.getMember_id()!=null){ %>   
                        value="<%=member_economic.getMember_id() %>" readonly
                        <% } else if(!m_id.equals("")){ %>
                        value="<%=m_id %>" readonly
                        <% } %>
                    >
                    <% if(!edit_mode.equals("yes")){ %>   
                    <div class="ui icon button searchmember" id="amka_check_btn">
                        <i class="search icon"></i>
                    </div>
                </div>
                    <% } %> 
            </div>
            <div class="four wide field">
                <label>Επίθετο</label>
                <input name="last-name" id="lastname" placeholder="Επίθετο" type="text" readonly
                    <% if(edit_mode.equals("yes") && (member_economic.getMember().getSurname()!=null)){ %>   
                       value="<%=member_economic.getMember().getSurname() %>"
                    <% } %>
                >
            </div>
            <div class="four wide field">
                <label>Όνομα</label>
                <input name="first-name" id="firstname" placeholder="Όνομα" type="text" readonly
                    <% if(edit_mode.equals("yes") && (member_economic.getMember().getName()!=null)){ %>   
                       value="<%=member_economic.getMember().getName()  %>"
                    <% } %>
                >
            </div>
            <div class="four wide field">
                <label>Έτος Συνδρομής</label>
                <input name="subscription-year" id="subscriptionyear" placeholder="Έτος συνδρομής" type="text" readonly
                    <% if(edit_mode.equals("yes") && (member_economic.getSubscription_year()!=null)){ %>   
                       value="<%=member_economic.getSubscription_year() %>"
                    <% } %>
                >
            </div>
        </div>
        <div class="fields">
            <div class="four wide field">
                <label>Τύπος Συνδρομής</label>
                <div class="ui selection dropdown">
                    <input type="hidden" name="subscription-type" id="subscriptiontype"
                    <% if(edit_mode.equals("yes") && (member_economic.getSubscription_type()!=null)){ %>   
                       value="<%=member_economic.getSubscription_type() %>"
                    <% } else if(!m_id.equals("") && registration_payment.equals("yes")){ %>
                        value="Εγγραφή"
                    <% } %>
                    >
                    <div class="default text">Τύπος Συνδρομής</div>
                    <i class="dropdown icon"></i>
                    <div class="menu">
                        <%
                            for (Subscription_Type subscription_type : subscription_types) {
                                if(!m_id.equals("")){
                                    if(registration_payment.equals("yes")){
                                        if(subscription_type.getType().equals("Εγγραφή")){
                        %>
                        <div class="item" data-value="<%=subscription_type.getType() %>"><%=subscription_type.getType() %> (<%=subscription_type.getCost() %>€)</div>
                        <%
                                        }
                                    }else{
                        %>
                        <div class="item" data-value="<%=subscription_type.getType() %>"><%=subscription_type.getType() %> (<%=subscription_type.getCost() %>€)</div>                
                        <%
                                    }
                                }else{
                        %>
                        <div class="item" data-value="<%=subscription_type.getType() %>"><%=subscription_type.getType() %> (<%=subscription_type.getCost() %>€)</div>            
                        <%
                                }
                            }
                        %>
                    </div>
                </div>
            </div>
            <div class="four wide field">
                <label>Τρόπος Πληρωμής</label>
                <div class="ui selection dropdown">
                    <input type="hidden" name="payment-type" id="paymenttype"
                    <% if(edit_mode.equals("yes") && (member_economic.getPayment_type()!=null)){ %>   
                       value="<%=member_economic.getPayment_type() %>"
                    <% } %>
                    >
                    <div class="default text">Τρόπος Πληρωμής</div>
                    <i class="dropdown icon"></i>
                    <div class="menu">
                        <%
                            for (Payment_Type payment_type : payment_types) {
                        %>
                        <div class="item" data-value="<%=payment_type.getType() %>"><%=payment_type.getType() %></div>
                        <%
                            }
                        %>
                    </div>
                </div> <br/>
                <div class="field" id="voucher_div"     
                <% 
                    if(edit_mode.equals("yes") && (member_economic.getPayment_type()!=null)){ 
                        if(member_economic.getPayment_type().equals("Μέσω Τραπέζης")){
                %>
                            style="display: block;"
                <%      
                        }else{
                %>
                            style="display: none;"
                <%
                        }
                    } else { 
                %>
                    style="display: none;"
                <% } %>
                >
                    <div class="ui action input">
                        <input placeholder="Παραστατικό" type="text" name="voucher_filepath" id="voucher_filepath" value="" readonly>
                        <div class="ui icon button voucherselectfile" id="voucher_upload_btn">
                            <i class="upload icon"></i>
                        </div>
                        <input type="file" style="display:none" name="voucher_file" id="voucher_file">
                        <% if(edit_mode.equals("yes") && member_economic.getVoucher_file_id()>0){ %>
                        <div class="ui icon circular blue button" id="existing_file_open_btn">
                            <i class="file text outline icon"></i>
                        </div>
                        <div class="ui flowing popup bottom  left transition hidden" style="min-width:210px;">
                            <table class="ui celled structured small table">
                                <thead>
                                    <th>Τρέχον παραστατικό.</th>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>
                                        <li>Για προβολή κάντε κλικ.</li>
                                        <li>Για αντικατάσταση επιλέξτε ένα άλλο παραστατικό.</li>
                                    </td>
                                </tr>
                                </tbody>
                            </ul>
                            </table>
                        </div>
                        <% } %>
                    </div>
                    <br/><i id="max_size_msg">Μέγιστο μέγεθος αρχείου: 1ΜΒ</i>
                </div>
            </div>
            <div class="four wide field">
                <div class="date field">
                 <label>Ημερομηνία Πληρωμής</label>
                 <input id="datepicker" name="payment-date" placeholder="xx/xx/xxxx" type="date"
                 <% if(edit_mode.equals("yes") && (member_economic.getPayment_date()!=null)){ %>   
                       value="<%=member_economic.getPayment_date() %>"
                 <% } %>
                 >
                </div>
            </div>
            <div class="four wide field">
                <label>Αριθμός Παραστατικού</label>
                <input name="invoice" id="invoice" placeholder="Αριθμός Παραστατικού" type="text"
                <% if(edit_mode.equals("yes") && (member_economic.getInvoice()!=null)){ %>   
                       value="<%=member_economic.getInvoice() %>"
                <% } %>       
                >
            </div>
          </div>
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
    
    $('.button').popup({
        inline: true
    });
    
    $('#resetbutton').on("click", function () {
        $('#insert-form').get(0).reset();
    });
    
    $('.message .close').on('click', function() {
        $(this).closest('.message').fadeOut();
    });
    
    $("#paymenttype").on("change", function () {
        if($("#paymenttype").val()=="Μέσω Τραπέζης"){
            document.getElementById("voucher_div").style.display = "block";
        }else{
            document.getElementById("voucher_div").style.display = "none";
        }
    });
    
    $("#voucher_upload_btn").on("click", function () {
        $("#voucher_file").one("change", function () {
            if ((!this.files[0].size || this.files[0].size <= 1024 * 1024)) {
                $("#voucher_filepath").val($(this).val()).trigger("input");
                document.getElementById("max_size_msg").style.color = "black";
            }else{
                $("#voucher_filepath").val("").trigger("input");
                document.getElementById("max_size_msg").style.color = "red";
            }
        }).trigger("click");
    });
    
    $("#existing_file_open_btn").on("click", function () {
        var newlocation="<jstl_c:url value='/main/VoucherDownload'/>"+"?filename=<%=member_economic.getVoucher_file_id() %>.<%=member_economic.getVoucher_file_type() %>";
        window.location.href = newlocation;
    });
      
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
            member_id: {
                identifier: "member_id",
                rules: [
                    {type: "empty", prompt: "Συμπληρώστε τον αριθμό μητρώου μέλους"}
                ]
            },
            subscriptionyear: {
                identifier: "subscriptionyear",
                rules: [
                    {type: "empty", prompt: "Συμπληρώστε το έτος συνδρομής"},
                    {type: "length[4]",  prompt: "Μη έγκυρο έτος" },
                    {type: "maxLength[4]", prompt: "Μη έγκυρο έτος"}
                ]
            },
            subscriptiontype: {
                identifier: "subscriptiontype",
                rules: [
                    {type: "empty", prompt: "Επιλέξτε τύπο συνδρομής"}
                ]
            },
            /*paymenttype: {
                identifier: "paymenttype",
                rules: [
                    {type: "empty", prompt: "Επιλέξτε τρόπο πληρωμής"}
                ]
            },*/
            datepicker: {
                identifier: "datepicker",
                rules: [
                    {type: "empty", prompt: "Συμπληρώστε την ημ/νία πληρωμής"}
                ]
            }/*,
            invoice: {
                identifier: "invoice",
                rules: [
                    {type: "empty", prompt: "Συμπληρώστε τον αριθμό παραστατικού"}
                ]
            }*/
        }, {
            inline: true,
            onSuccess: addEconomic
        });
    });
    
    function addEconomic(){
        insertform.submit();
    }
    
    $(function () {
        $("#datepicker").datepicker();
    });
    
    function searchMember(){
    	$.get("<jstl_c:url value='/main/MemberLiveSearch?member_id="+$("#member_id").val()+"' />", {offset: 0, limit: 100}).done(function (data) {
            var member = data.member;
            for (i = 0; i < member.length; i++) {
                $("#lastname").val(member[i].surname);
                $("#firstname").val(member[i].name);
                $("#subscriptionyear").val(member[i].year_to_be_paid);
            }

            $('#lastname, #firstname, #subscriptionyear').trigger('blur');
        }
        ).always(function () {
            
        });
    }
    
    $(".ui.icon.button.searchmember").on("click", function (evt) {
	searchMember();
        evt.stopPropagation();
    });

    $("#member_id").on("change", function (evt) {
	searchMember();
        evt.stopPropagation();
    });
   
    <% if(!m_id.equals("")){ %>
        searchMember();
    <% } %>
</script>

<style>
    .ui-datepicker { font-size: 9pt !important; }
</style>
