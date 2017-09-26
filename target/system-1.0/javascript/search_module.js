
/*Requires jquery and tablesort plugin*/

/*search*/
(function ($) {
    /*
     * options =  {search_url, title:..., fields: ...,  controls: ...,}
     * field = {real_name: ..., json_name: ..., link {base:..., fields:...}, visibility:...visible, hidden or none..., width:...}
     * !!! width = one ... sixteen or ""
     */
    $.fn.search_ = function (behavior, options) {
        var search_form = $(
                "<form class='ui form segment' data-search='true'>" +
                "<div class='field'>" +
                "<table class='ui sortable celled table'>" +
                "<thead class='full-width'>" +
                "</thead>" +
                "<tbody>" +
                "</tbody>" +
                "<tfoot>" +
                "</tfoot>" +
                "</table>" +
                "</div>" +
                "<div class='ui yellow message' id='no_results_msg' style='display: none; text-align: center'>" +
                "Δεν βρέθηκαν αποτελέσματα" +
                "</div>" +
                "<div class='fields'>" +
                "<div class='five wide field'>" +
                "<div class='ui pagination menu'>" +
                "</div>" +
                "</div>" +
                "<div class='eight wide inline field'>" +
                "<label>Πλήθος αποτελεσμάτων ανά σελίδα: </label>" +
                "<select class='ui compact dropdown' name='results_per_page'>" +
                "<option value='5'>5</option>" +
                "<option value='10'>10</option>" +
                "<option value='20'>20</option>" +
                "<option value='50'>50</option>" +
                "</select>" +
                "</div>" +
                "<div class='three wide field'>" +
                "<div class='ui action input' title='Αναζήτηση βάσει προθέματος'>" +
                "<input type='text' placeholder='Αναζήτηση...' name='search'>" +
                "<div class='ui icon button'>" +
                "<i class='search icon'></i>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "<div class='ui dimmer'>" +
                "<div class='ui text loader'> Φόρτωση δεδομένων... </div>" +
                "</div>" +
                "</form>");

        var $results_table_body = search_form.find("table > tbody");
        var $pagination_menu = search_form.find(".ui.pagination.menu");
        var $no_results_msg = search_form.find("#no_results_msg");
        var $dimmer = search_form.find(".ui.dimmer");

        var number_of_pages, search_term, fields, controls, i, head_row;

        if (behavior === "show") {
            fields = options.fields;
            controls = options.controls;
            if (controls === undefined || controls === null) {
                controls = [];
            }

            if (options.title !== undefined && options.title !== null) {
                search_form.prepend("<h4 class='ui dividing header'>" + options.title + "</h4>");
            }

            head_row = "<tr>";
            for (i = 0; i < fields.length; i++) {
                if (!fields[i].visibility || fields[i].visibility !== "none") {
                    head_row += "<th ";
                    if (fields[i].width && fields[i].width !== "") {
                        head_row += "class='" + fields[i].width + " wide' ";
                    }
                    if (fields[i].visibility && fields[i].visibility === "hidden") {
                        head_row += "style='display: none' ";
                    }

                    head_row += ">" + fields[i].real_name + "</th>";
                }
            }
            head_row += "</tr>";
            search_form.find("thead").append(head_row);
            function list(offset, results_per_page) {

                $no_results_msg.hide();
                $dimmer.dimmer("show");
                if (!search_term) {
                    search_term = "";
                }
                $.post(options.search_url,
                        {"offset": offset, limit: results_per_page, "search_term": search_term})
                        .done(function (response) {
                            var results = response.results;
                            var i, j, z, current_page_number, td_contents, pagination_block_number, pagination_menu_first_page_number, pagination_menu_last_page_number, row, link;
                            $results_table_body.empty();
                            $pagination_menu.empty();
                            for (i = 0; i < results.length; i++) {
                                row = "<tr>";
                                for (j = 0; j < fields.length; j++) {
                                    if (!fields[j].visibility || fields[j].visibility !== "none") {
                                        link = fields[j].link;
                                        if (link) {
                                            td_contents = "<a href='" + link.base;
                                            if (link.fields.length > 0) {
                                                td_contents += "?";
                                                for (z = 0; z < link.fields.length; z++) {
                                                    td_contents += link.fields[z].json_name + "=" + encodeURIComponent((results[i])[link.fields[z].json_name]) + "&";
                                                }

                                                td_contents = td_contents.substring(0, td_contents.length - 1);
                                            }

                                            td_contents += "'>" + (results[i])[fields[j].json_name] + "</a>";
                                        } else {
                                            td_contents = (results[i])[fields[j].json_name];
                                        }

                                        row += "<td ";
                                        if (search_term !== "" && (results[i])[fields[j].json_name].search(new RegExp(search_term, "i")) === 0) {
                                            row += "class = 'warning' ";
                                        }

                                        if (fields[j].visibility !== undefined && fields[j].visibility !== null && fields[j].visibility === "hidden") {
                                            row += "style='display: none' ";
                                        }

                                        row += "> " + td_contents + " </td>";
                                    }
                                }
                                for (j = 0; j < controls.length; j++) {
                                    row += "<td>" + controls[i].html() + "</td>";
                                }
                                row += "</tr>";
                                $results_table_body.append(row);
                            }

                            if (results.length == 0) {
                                if (offset > 0) {
                                    list(0, results_per_page);
                                } else {
                                    $no_results_msg.show();
                                }
                            } else {
                                number_of_pages = Math.ceil(response.total_number / results_per_page);
                                current_page_number = Math.ceil((offset + 1) / results_per_page);
                                pagination_block_number = Math.ceil(current_page_number / 5);
                                pagination_menu_first_page_number = (pagination_block_number - 1) * 5 + 1;
                                if (pagination_menu_first_page_number + 5 > number_of_pages) {
                                    pagination_menu_last_page_number = number_of_pages;
                                } else {
                                    pagination_menu_last_page_number = pagination_menu_first_page_number + 4;
                                }

                                if (pagination_menu_last_page_number > 1) {
                                    for (i = pagination_menu_first_page_number; i < current_page_number; i++) {
                                        $pagination_menu.append(
                                                "<a class='item'>" + i + "</a>"
                                                );
                                    }

                                    $pagination_menu.append(
                                            "<a class='active item'>" + i + "</a>"
                                            );
                                    for (i = current_page_number + 1; i <= pagination_menu_last_page_number; i++) {
                                        $pagination_menu.append(
                                                "<a class='item'>" + i + "</a>"
                                                );
                                    }

                                    if (pagination_block_number > 1) {
                                        $pagination_menu.prepend(
                                                "<a class='icon item'>" +
                                                "<i class='chevron left icon'></i>" +
                                                "</a>"
                                                );
                                        $pagination_menu.prepend(
                                                "<a class='icon item'>" +
                                                "<i class='step backward icon'></i>" +
                                                "</a>"
                                                );
                                    }

                                    if (pagination_menu_last_page_number < number_of_pages) {
                                        $pagination_menu.append(
                                                "<a class='icon item'>" +
                                                "<i class='chevron right icon'></i>" +
                                                "</a>"
                                                );
                                        $pagination_menu.append(
                                                "<a class='icon item'>" +
                                                "<i class='step forward icon'></i>" +
                                                "</a>"
                                                );
                                    }

                                }
                            }
                            if (options.onFinish) {
                                options.onFinish();
                            }
                        }
                        )
                        .fail(function () {
                            alert("error");
                        }).always(function () {
                    $dimmer.dimmer("hide");
                });
            }

            $pagination_menu.on("click", "a:not(.icon)", function () {
                var selected_page_number = parseInt($(this).text());
                var results_per_page = search_form.find("select[name='results_per_page']").val();
                var offset = (selected_page_number - 1) * results_per_page;
                list(offset, results_per_page);
            });
            $pagination_menu.on("click", "a[class^='icon']", function () {
                var $pagination_menu_first_page = parseInt($(this).parent().find("a:not(.icon)").first().text());
                var $pagination_menu_last_page = parseInt($(this).parent().find("a:not(.icon)").last().text());
                var results_per_page = search_form.find("select[name='results_per_page']").val();
                var offset;
                if ($(this).children().is(".step.backward.icon")) {
                    list(0, results_per_page);
                } else if ($(this).children(".step.forward.icon").length == 1) {
                    offset = (number_of_pages - 1) * results_per_page;
                    list(offset, results_per_page);
                } else if ($(this).children().is(".left.chevron.icon")) {
                    offset = ($pagination_menu_first_page - 2) * results_per_page;
                    list(offset, results_per_page);
                } else if ($(this).children().is(".right.chevron.icon")) {
                    offset = $pagination_menu_last_page * results_per_page;
                    list(offset, results_per_page);
                }
            });
            search_form.find("select[name='results_per_page']").on("change", function () {
                list(0, parseInt($(this).val()));
            });
            search_form.find("input[name='search']").on("keypress", function (evt) {
                var results_per_page;
                if (evt.which == 13) {//enter
                    search_term = $(this).val();
                    results_per_page = search_form.find("select[name='results_per_page']").val();
                    list(0, results_per_page);
                    evt.preventDefault();
                }
            });
            search_form.find("input[name='search']").siblings(".ui.icon.button").on("click", function () {
                var evt = jQuery.Event("keypress");
                evt.which = 13; //enter
                $(this).siblings("input[name='search']").trigger(evt);
            });
            search_form.find(".ui.dropdown").dropdown();
            list(0, 5);
            $(this).append(search_form);
            search_form = search_form;
            search_form.find("table").tablesort();

        } else if ("destroy") {
            if ($(this).find("form[data-search]").length > 0) {
                $(this).find("form[data-search]").remove();
            }
        }
    }
}(jQuery));
//    var fields = [
//        {
//            real_name: "Όνομα φορέα",
//            json_name: "name",
//            width: "four",
//            link_base: "https://google.gr"
//        },
//        {
//            real_name: "ΑΦΜ",
//            json_name: "afm",
//            width: "four",
//            link_base: ""
//        },
//        {
//            real_name: "Όνομα χρήστη",
//            json_name: "username",
//            width: "four",
//            link_base: ""
//        },
//    ];
//
//    $("div[title='aa'").search({search_url: "<jstl_c:url value='/admin/institution_accounts/SearchInstitutionAccounts'/>", title: "Προβολή φορέων ψυχικής Υγείας", "fields": fields, controls: []});


