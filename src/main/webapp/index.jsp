<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<jstl:url value='/css/general.css' />" />
        <title>Ελληνικός Ερυθρός Σταυρός</title>
        <script src="<jstl:url value='/javascript/jquery.min.js' />" ></script>
        <script src="<jstl:url value='/javascript/jquery-migrate.min.js' />" ></script>
        <script src="<jstl:url value='/javascript/semantic.js' />" ></script>
        <script src="https://www.google.com/recaptcha/api.js?hl=el" async defer></script>
        <script>
            <jstl:if test="${is_recaptcha_enabled}"> 
            function enableSubmitButton() {
                $("button[type='submit']").prop("disabled", false);
            }

            function disableSubmitButton() {
                $("button[type='submit']").prop("disabled", true);
            }

            $(document).ready(function () {
                disableSubmitButton();
            });
            </jstl:if>
            <jstl:if test="${login_retry}">
            $(document).ready(function () {
                $("input[name='username'],input[name='password'").one("input", function () {
                    $("form div.negative.message").css("display", "none");
                });

                $("form div.negative.message > i.icon.close").one("click", function () {
                    $("form div.negative.message").css("display", "none");
                });
            });
            </jstl:if>
        </script>
    </head>
    <body class="ui stackable horizontally padded grid">
        <header class="three equal height column row" style="background-color: white; padding-bottom: 0;">
            <div class="left floated column">
                <a href="<jstl:url value='/' />" >
                    <img src="<jstl:url value='/images/redcross.png' />" alt="Λογότυπο συστήματος" style="width: 150px; height: 100px">
                </a>
            </div>
            <div class="six wide center aligned column">
                <div style="line-height: 100px;">
                    <h3 class="ui header" style="line-height: 20px; display: inline-block; vertical-align: middle;">
                        Ελληνικός Ερυθρός Σταυρός
                    </h3>
                </div>
            </div>     
        </header>
        <div class="ui divider" style="margin: 0 0 1rem"></div>

        <main class="one column row">
            <form class="ui form five wide centered floated column" action="LoginController" method="post">
                <h4 class="ui top attached header">Είσοδος χρήστη</h4>
                <div class="ui bottom attached segment">                  
                    <div class="field">
                        <label for="username">Όνομα χρήστη: </label>
                        <div class="ui left icon input">
                            <input type="text" placeholder="Όνομα χρήστη" name="username" id="username">
                            <i class="user icon"></i>
                        </div>
                    </div>
                    <div class="field">
                        <label for="password">Κωδικός πρόσβασης: </label>
                        <div class="ui left icon input">
                            <input type="password" placeholder="Κωδικός πρόσβασης" name="password" id="password">
                            <i class="lock icon"></i>
                        </div>
                    </div>
                    <jstl:if test="${is_recaptcha_enabled}">
                        <div class="field">
                            <div class="g-recaptcha" data-sitekey="${recaptcha_site_key}" data-callback="enableSubmitButton" data-expired-callback="disableSubmitButton"></div>
                        </div>
                    </jstl:if>
                    <button class="ui submit button" type="submit">Είσοδος</button>
                    <div class="ui segment">
                        <a href="#">Δεν μπορώ να συνδεθώ στο λογαριασμό μου</a>
                    </div>
                    <jstl:if test="${login_retry}">
                        <div class="ui bottom attached negative message">
                            <i class="close icon"></i>
                            <div class="header">
                                Μη έγκυρα διαπιστευτήρια.
                            </div>
                            <p>
                                Παρακαλούμε προσπαθήστε ξανά.
                            </p>
                        </div>
                    </jstl:if>                      
                </div>
            </form>
            <noscript>
            <div class="ui active dimmer">
                <div class="content">
                    <div class="center">
                        <h2 class="ui inverted icon header">
                            <i class="warning sign icon"></i>
                            Ενεργοποίηση Javascript
                            <div class="sub header">Η πρόσβαση στον παρόντα ιστότοπο προϋποθέτει πως η JavaScript είναι ενεργοποιημένη στο πρόγραμμα περιήγηση σας.
                                <br/>Ενεργοποιήστε τη Javascript και ανανεώστε τη τρέχουσα σελίδα.</div>
                        </h2>
                        <br/>
                        <a href="${requestScope["javax.servlet.forward.request_uri"]}?${pageContext.request.queryString}" class="ui green inverted icon button">Ανανέωση<i class="refresh icon"></i></a>
                    </div>
                </div>
            </div>
            </noscript>
        </main>
    </body>
</html>