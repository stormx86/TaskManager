<html>
<head>
    <link rel="stylesheet" type="text/css" href ="/css/main.css">
    <title>User Edit Page</title>
</head>
<body>

<form action="/user" method="post">

    <table class="tbTasks">
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th></th>

        </tr>
        </thead>
        <tbody>

        <tr>
            <td><input type="text" name="username" value="${user.username}"></td>
            <#list roles as role>
            <td><label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label></td>
            </#list>
            <td><input type="submit" class="button_cl" value="Save"/></td>
        </tr>

        </tbody>
    </table>
    <input type="hidden" value="${user.id}" name="userId">
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

</form>

</body>
</html>