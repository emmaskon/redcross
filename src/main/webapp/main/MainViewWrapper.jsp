<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl_c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="jstl_fn"%>
<%@page import="models.User"%>
<!DOCTYPE html>

<%
User user = (User) session.getAttribute("user");
%>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<jstl_c:url value='/css/general.css' />" />
        <title>Ελληνικός Ερυθρός Σταυρός</title>
        <script src="<jstl_c:url value='/javascript/jquery.min.js' />" ></script>
        <script src="<jstl_c:url value='/javascript/jquery-migrate.min.js' />" ></script>
        <script src="<jstl_c:url value='/javascript/tablesort_module.js' />" ></script>
        <script src="<jstl_c:url value='/javascript/semantic.js' />" ></script>
        <script src="<jstl_c:url value='/javascript/search_module.js' />" ></script>
        <script src="<jstl_c:url value='/javascript/semantic_extensions.js' />" ></script>
        <script src="https://www.google.com/recaptcha/api.js?hl=el" async defer></script>
        <script>
            $(document).ready(function () {
                var sidebar = $("nav > .ui.sidebar.vertical");
                var $unit_search_modal = $("#unit_search_modal");

                $("nav > .ui.menu > .header.item").on("click", function (evt) {
                    sidebar.toggleClass("visible");
                    evt.stopPropagation();
                });

                $(document).on("click", function () {
                    if (sidebar.hasClass("visible")) {
                        sidebar.removeClass("visible");
                    }
                });

                sidebar.on("click", function (evt) {
                    evt.stopPropagation();
                });

                sidebar.find(".item > .compress.layout.icon").parent().on("click", function () {
                    sidebar.removeClass("visible");
                });

                sidebar.find("#mental_health_unit_menu > a").on("click", function (evt) {
                    var title = $(this).attr("title");
                    sidebar.removeClass("visible");
                    var target;

                    if (title.indexOf("Κέντρα Ημέρας") !== -1) {
                        target = "daily_centers";
                    } else if (title.indexOf("Στεγαστικού Τύπου") !== -1) {
                        target = "housing";
                    } else if (title.indexOf("Κινητές Μονάδες") !== -1) {
                        target = "mobile_units";
                    } else {
                        target = "reintegration";
                    }

                    var fields = [
                        {
                            real_name: "Όνομα Μονάδας",
                            json_name: "unit_name",
                            width: "eight",
                            link: {
                                base: "<jstl_c:url value='/admin/'/>" + target,
                            }
                        },
                        {
                            real_name: "ΑΦΜ Φορέα",
                            json_name: "institution_afm",
                            width: "four",
                        },
                        {
                            real_name: "Περιφέρεια",
                            json_name: "region",
                        },
                    ];

                    fields[0].link.fields = [fields[0], fields[1]];
                    $unit_search_modal.find(".content").search_("destroy");
                    $unit_search_modal.find(".content").search_("show", {search_url: "<jstl_c:url value='/admin/'/>" + target + "/Search", "fields": fields, controls: []});
                    $unit_search_modal.find("span.sub.header").html("Επιλέξτε τη Μ.Ψ.Υ που επιθυμείτε");
                    $unit_search_modal.modal("show");


                });

                $("nav .menu[title='Αποσύνδεση']").on("click", function () {
                    location.replace("<jstl_c:url value='/Logout' />")
                });

                $("#settings_menu a[title='Μονάδες Ψ.Υ.']").on("click", function () {
                    var fields = [
                        {
                            real_name: "Όνομα φορέα",
                            json_name: "institution_name",
                            width: "eight",
                            link: {
                                base: "<jstl_c:url value='/admin/institution_units'/>",
                                field_jnames: ["institution_afm"]
                            }
                        },
                        {
                            real_name: "ΑΦΜ",
                            json_name: "institution_afm",
                            width: "four",
                        },
                        {
                            real_name: "Όνομα χρήστη",
                            json_name: "username",
                        },
                    ];



                    fields[0].link.fields = [fields[1]];
                    $unit_search_modal.find(".content").search_("destroy");
                    $unit_search_modal.find(".content").search_("show", {search_url: "<jstl_c:url value='/admin/institution_accounts/SearchInstitutionAccounts'/>", "fields": fields, controls: []});
                    $unit_search_modal.find("span.sub.header").html("Επιλέξτε τον φορέα που επιθυμείτε");
                    $("#unit_search_modal").modal("show");
                });

            });

        </script>
    </head>
    <body class="ui stackable horizontally padded grid">
        <header class="three equal height column row" style="background-color: white; padding-bottom: 0;">
            <div class="left floated column">
                <a href="<jstl_c:url value='/' />" >
                    <img src="<jstl_c:url value='/images/redcross.png' />" alt="Λογότυπο συστήματος" style="width: 150px; height: 100px">
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
        <div class="ui divider" style="margin: 0"></div>

        <nav class="row" style="margin: 0; padding: 0;">
            <!-- top menu -->
            <div class="ui menu" style="margin: 0">
<!--                <div class="header item" style="cursor: pointer">
                    <i class="sidebar icon"></i>
                    Διαχείριση 
                </div>-->
                <jstl_c:import url="top_menu_items.jsp" />
                <!-- View Specific Menu -->
                <jstl_c:if test="${not empty view_relative_path}">
                    <jstl_c:set var="path_segments" value="${jstl_fn:split(view_relative_path, '/')}"/>           
                    <jstl_c:set var="view_relative_path_prefix" value="${jstl_fn:replace(jstl_fn:substring(view_relative_path, 0, jstl_fn:length(view_relative_path) - jstl_fn:length(path_segments[jstl_fn:length(path_segments)-1])),'/','')}" />
                    
                </jstl_c:if>
                <div class="right menu" title="Αποσύνδεση">
                    <div class="header item" style="cursor: pointer">
                        <i class="lock icon"></i>
                        Αποσύνδεση 
                    </div>
                </div>
                <%  if(user.getRole().equals("su")){ %>
                <div class="right menu">
                    <a class="item" href="<jstl_c:url value='/log/ViewLog'/>"><i class="list icon"></i>Αρχείο καταγραφής</a>
                </div>
                <%  } %>
            </div>

            <!-- sidebar -->
            <div class="ui left vertical black inverted labeled icon pushable sidebar menu overlay">
                <a class="item">
                    <i class="compress layout icon"></i>
                </a>     


                <div class="item">
                    <div class="ui small active inverted header">Μονάδες Ψυχικής Υγείας <i class="doctor icon right"></i></div>
                    <div class="menu" id="mental_health_unit_menu">
                        <a class="item" href="#" title="Κέντρα Ημέρας, Κέντρα Ψυχικής Υγείας, Νοσοκομεία Ημέρας,Ιατροπαιδαγωγικά Κέντρα">
                            Κέντρα Ημέρας ,<br/> 
                            Κέντρα Ψυχικής Υγείας,<br/>
                            Νοσοκομεία Ημέρας, <br/> 
                            Ιατροπαιδαγωγικά Κέντρα                       
                        </a>
                        <a class="item" title="Κινητές Μονάδες Ψυχικής Υγείας">
                            Κινητές Μονάδες Ψυχικής Υγείας
                        </a>
                        <a class="item" title="Μονάδες Ψυχοκοινωνικής Αποκατάστασης Στεγαστικού Τύπου">
                            Μονάδες Ψυχοκοινωνικής Αποκατάστασης Στεγαστικού Τύπου
                        </a>
                        <a class="item" title="Ειδικά Κέντρα Κοινωνικής Επανένταξης, Ειδικές Μονάδες Αποκατάστασης και Επαγγελματικής Επανένταξης">
                            Ειδικά Κέντρα Κοινωνικής Επανένταξης, <br/>
                            Ειδικές Μονάδες Αποκατάστασης και Επαγγελματικής Επανένταξης
                        </a>
                    </div>
                </div>

                <div class="item">
                    <div class="ui small active inverted header">Πίνακας Ελέγχου <i class="settings icon right"></i></div>
                    <div class="menu" id="settings_menu">
                        <a class="item" href="<jstl_c:url value='/admin/institution_accounts' />">
                            Λογαριασμοί φορέων
                        </a>
                        <a class="item" title="Μονάδες Ψ.Υ.">
                            Μονάδες Ψ.Υ.
                        </a>
                        <a class="item" href="<jstl_c:url value='/admin/references' />">
                            Αναφορές
                        </a>
                        <a class="item" href="<jstl_c:url value='/admin/statistics/FinalSubmissions' />">
                            Στατιστικά
                        </a>
                        <a class="item" href="<jstl_c:url value='/admin/deadlines/Deadlines' />">
                            Προθεσμίες
                        </a>
                    </div>
                </div>
            </div>

            <!-- units search modal -->
            <div class="ui modal" id="unittypes_search_modal">
                <i class="close icon"></i>
                <div class="ui header"></div>
                <div class="content">
                    <div class="ui segment">
                        <div class="ui dimmer">
                            <div class="ui indeterminate text loader">Λήψη δεδομένων. Παρακαλώ περιμένετε.</div>
                        </div>
                        <table class="ui table dc">
                            <thead>
                                <tr>
                                    <th>Όνομα</th>
                                    <th>Φορέας</th>
                                    <th>Νομός</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                        <table class="ui table hc">
                            <thead>
                                <tr>
                                    <th>Όνομα</th>
                                    <th>Φορέας</th>
                                    <th>Νομός</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                        <table class="ui table rc">
                            <thead>
                                <tr>
                                    <th>Όνομα</th>
                                    <th>Φορέας</th>
                                    <th>Νομός</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                        <table class="ui table mu">
                            <thead>
                                <tr>
                                    <th>Όνομα</th>
                                    <th>Φορέας</th>
                                    <th>Νομός</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="actions">
                </div>
            </div>

            <!-- unit search modal2 -->
            <div class="ui fullscreen modal" id="unit_search_modal">
                <i class="close icon"></i>
                <div class="ui header">Αναζήτηση<br/>
                    <span class="sub header"></span>
                </div>
                <div class="content">                
                </div>
                <div class="actions">
                </div>
            </div>

        </nav>
        <div class="ui divider" style="margin: 0"></div>

        <main class="ui segment grid" style="margin: 0; padding: 5px 0;">
            <div class="two equal height column row" style="padding: 0; margin: 0">
                <div class="nine wide column">
                    <jstl_c:if test="${not empty view_relative_path}">
                        <jstl_c:import url="breadcrumb.jsp" />
                    </jstl_c:if>
                </div>
                <div class="right floated right aligned column">
                    <div class="ui blue label">
                        <i class="user icon"></i> Έχετε συνδεθεί ως <a href="#">${user.username}</a>
                    </div>                
                </div>
            </div> 
            <div class="sixteen wide column">

                <jstl_c:if test="${not empty view_relative_path}">

                    <jstl_c:import url="${view_relative_path}" />
                </jstl_c:if>
            </div>
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
<%--                         <a href="${requestScope["javax.servlet.forward.request_uri"]}?${pageContext.request.queryString}" class="ui green inverted icon button">Ανανέωση<i class="refresh icon"></i></a> --%>
                    </div>
                </div>
            </div>
            </noscript>
        </main>
        <div class="ui divider" style="margin: 0"></div>
    </body>
</html>
