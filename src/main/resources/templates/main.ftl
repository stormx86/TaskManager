<html>
<head>
    <#assign security=JspTaglibs["http://www.springframework.org/security/tags"]/>
    <#import "parts/menu.ftl" as m>
    <#import "parts/pager.ftl" as p>
    <title>Welcome to the Task Manager</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href ="css/main.css">
    <script src="https://code.jquery.com/jquery-3.5.1.js"></script>

    <!-- POST request, calling changeStatus method-->
    <script>
        $(document).ready(function () {
            $(document.body).on("click", ".status", function () {
                var id = $(this).closest("tr").find("td:first-child").text();
                var status = $(this).closest("tr").find("td.status");
                var editBy = $(this).closest("tr").find("td.editBy");
                $.ajax({
                    headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
                    url: "changestatus",
                    type: "POST",
                    data: {id: id},
                    success: function(response){
                        status.html(response[0]);
                        editBy.html(response[1]);
                        if(response[0]=="Processing") status.css('background-color','#b4f0c1')
                        else (status.css('background-color','#f6f6f6'));
                    }
                });
            })
        })
    </script>

    <!--coloring td "status"-->
    <script>
        $(document).ready(function () {
        $('td.status').each(function() {
            if ($(this).text() == 'Processing') {
                $(this).css('background-color', '#b4f0c1');}
            else ($(this).css('background-color', '#f6f6f6'));
        })
        });
    </script>

    <!--POST request, calling checkTask method -->
    <script>
        $(document).ready(function checkStatus () {
                $.ajax({
                    headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
                    url: "checktask",
                    type: "POST",
                    success: function(response){
                        if($("#ct").text() != response) {
                            $("#ct").html(response);
                        }
                    }
                });
            setTimeout(checkStatus, 5000);
        })
    </script>

    <!--NewTask message show/hide -->
    <script>
        $(document).ready(function ctDisplay () {
            $("#ct").fadeOut(3000);
            $("#ct").fadeIn(1500);
            setTimeout(ctDisplay, 7500);
        })

    </script>

    </head>
<body>
<div class="container-fluid">
    <@m.menu/>
    <div class="row">
        <div class="col">
            <@p.pager url page/>
        </div>
    </div>

    <div class="row">
        <div class="col">
            <table id = "tasksTable" class="table table-hover table-bordered">
                <tr>
                    <th style="display:none;">id</th>
                    <th style="width: 7%" scope="col">Received On</th>
                    <th style="width: 9%" scope="col">From</th>
                    <th style="width: 20%" scope="col">Subject</th>
                    <th scope="col">Snippet</th>
                    <th style="width: 5%" scope="col">Attachments</th>
                    <th style="width: 6%" scope="col">Status</th>
                    <th style="width: 8%" scope="col">Changed By</th>
                    <@security.authorize access="hasAnyAuthority('ADMIN')">
                        <th></th>
                    </@security.authorize>
                </tr>
                <#list page.content as task>
                    <tr>
                        <td style="display:none;">${task.id}</td>
                        <td>${task.receivedAtFormatted}</td>
                        <td>${task.sentBy}</td>
                        <td style="height: 90px">${task.subject}</td>
                        <td>${task.snippet}</td>
                        <td align="center">
                            <#if task.hasAttachment==true>
                            <form action="/editTask/getAttachment/${task.messageId}" method="get">
                                    <button type="submit" class="btn btn-link" title="Download">
                                        <i style="color: black; alignment: center" class="fas fa-download" aria-hidden="true"></i>
                                    </button>
                            </form>
                            </#if>
                        </td>
                        <td class="status">${task.status}</td>
                        <td class="editBy">${task.editBy}</td>
                        <@security.authorize access="hasAnyAuthority('ADMIN')">
                            <td>
                                <form action="/editTask/delete/${task.id}" method="get">
                                    <input type="submit" class="btn btn-outline-dark btn-sm" value="Delete" />
                                </form>
                            </td>
                        </@security.authorize>
                    </tr>
                </#list>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <@p.pager url page/>
        </div>
    </div>

</div>
<#--<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>-->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
</body>
</html>