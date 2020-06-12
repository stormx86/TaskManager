<html>
<head>
    <#assign security=JspTaglibs["http://www.springframework.org/security/tags"]/>
    <#import "parts/pager.ftl" as p>
    <title>Welcome to the Task Manager</title>
    <link rel="stylesheet" type="text/css" href ="css/main.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>

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

<div>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="submit" class="button_cl" value="Sign Out"/>
    </form>

    <form action="refresh" method="post">
        <input type="submit" class="button_cl" value="Refresh"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
    <@security.authorize access="hasAnyAuthority('ADMIN')">
    <form action="/editTask/deleteAll" method="post">
        <input type="submit" class="button_cl" value="Delete All Tasks"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>

    <form action="/user" method="get">
        <input type="submit" class="button_cl" value="User list" />
    </form>
    </@security.authorize>

    <span id="ct" class="checktask"></span>

</div>
<@p.pager url page/>
    <div>
    <table id = "tasksTable" class="tbTasks" cellspacing='0'>
    <tr>
        <th style="display:none;">id</th>
        <th>DateTime</th>
        <th>From</th>
        <th>Subject</th>
        <th>Snippet</th>
        <th>Status</th>
        <th>EditBy</th>
        <@security.authorize access="hasAnyAuthority('ADMIN')">
        <th></th>
        </@security.authorize>
    </tr>
<#list page.content as task>
    <tr>
        <td style="display:none;">${task.id}</td>
        <td>${task.receivedAt}</td>
        <td>${task.sentBy}</td>
        <td>${task.subject}</td>
        <td>${task.snippet}</td>
        <td class="status">${task.status}</td>
        <td class="editBy">${task.editBy}</td>
        <@security.authorize access="hasAnyAuthority('ADMIN')">
        <td>
            <form action="/editTask/delete/${task.id}" method="get">
                <input type="submit" class="button_cl" value="Delete" />
            </form>
        </td>
        </@security.authorize>
    </tr>
<#else>
No task
</#list>
    </table>
    </div>
<@p.pager url page/>


</body>
</html>