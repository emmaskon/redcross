<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.User"%>
<%
    String successInsert=(String)request.getAttribute("successInsert");
    
    User user = (User) session.getAttribute("user");
%>
<% if(successInsert.equals("yes")){ %>
    <div class="ui success message">
        <i class="close icon"></i>
        <div class="header">
        Επιτυχής εισαγωγή στοιχείων!
        </div>
    </div>
<% } else if (successInsert.equals("wrong_credentials")) { %> 
    <div class="ui negative message">
        <i class="close icon"></i>
        <div class="header">
        Εισάγατε λανθασμένο όνομα ή κωδικό χρήστη. Παρακαλούμε εισάγετε τα σωστά διαπιστευτήρια.
        </div>
    </div>
<% } else if (successInsert.equals("two_different_new_passwords")) { %> 
    <div class="ui negative message">
        <i class="close icon"></i>
        <div class="header">
        Εισάγατε δύο διαφορετικούς νέους κωδικούς. Η αλλαγή κωδικού δεν μπορεί να πραγματοποιηθεί.
        </div>
    </div>
<% } %> 
<div class="ui form segment">
    <h4 class="ui dividing header">Αλλαγή Κωδικού Πρόσβασης</h4>
<form action="ChangePassword" method="post" id="password_change_form">
   <div class="fields">
        <div class="four wide field">
            <label>Όνομα Χρήστη</label>
            <div class="ui left icon input">
            <input name="username" id="username" placeholder="Όνομα Χρήστη" type="text">
            <i class="user icon"></i>
            </div>
        </div>
        <div class="four wide field">
            <label>Κωδικός Χρήστη</label>
            <div class="ui left icon input">
            <input name="password" id="password" placeholder="Κωδικός Χρήστη" type="password">
            <i class="privacy icon"></i>
            </div>
        </div>
        <div class="four wide field">
            <label>Νέος Κωδικός Χρήστη</label>
            <div class="ui left icon input">
            <input name="newpassword" name="newpassword" placeholder="Νέος Κωδικός Χρήστη" type="password">
            <i class="privacy icon"></i>
            </div>
        </div>
        <div class="four wide field">
            <label>Επιβεβαίωση Νέου Κωδικού Χρήστη</label>
            <div class="ui left icon input">
            <input name="newpasswordagain" id="newpasswordagain" placeholder="Επιβεβαίωση Νέου Κωδικού Χρήστη" type="password">
            <i class="privacy icon"></i>
            </div>
        </div>
    </div>
    <button id="submitbutton" class="ui green submit button">Υποβολή</button>
    <div id="resetbutton" class="ui red reset button">Εκκαθάριση</div>
 </form>
</div>

<script>
    var password_change_form = $("#password_change_form");
     
    $('.message .close').on('click', function() {
        $(this).closest('.message').fadeOut();
    });
    
    $('#resetbutton').on("click", function () {
        $('#password_change_form').get(0).reset();
    });

    $('#submitbutton').on("click", function () {
        password_change_form.form({
            username: {
                identifier: "username",
                rules: [
                    {type: "empty", prompt: "Εισάγετε το όνομα χρήστη"}
                ]
            },
            password: {
                identifier: "password",
                rules: [
                    {type: "empty", prompt: "Εισάγετε τον τρέχοντα κωδικό πρόσβασης"}
                ]
            },
            newpassword: {
                identifier: "newpassword",
                rules: [
                    {type: "empty", prompt: "Εισάγετε το νέο κωδικό πρόσβασης" },
                    {type: "length[8]", prompt: "Απαιτούνται τουλάχιστον 8 χαρακτήρες" }
                ]
            },
            newpasswordagain: {
                identifier: "newpasswordagain",
                rules: [
                    {type: "empty", prompt: "Εισάγετε το νέο κωδικό πρόσβασης"},
                    {type: "length[8]", prompt: "Απαιτούνται τουλάχιστον 8 χαρακτήρες" }
                ]
            }
            }, {
            inline: true,
            onSuccess: changePsw
        });
    });
    
    function changePsw(){
        password_change_form.submit();
    }
</script>