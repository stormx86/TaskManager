<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link rel="stylesheet" href ="css/main.css"/>
    <title>User List Page</title>
</head>
<body>
<form action="/" method="get">
    <input type="submit" class="button_cl" value="<--Back"/>
</form>
<form action="user/addUser" method="get">
    <input type="submit" class="button_cl" value="Add New User"/>
</form>
<table class="tbTasks">
    <thead>
    <tr>
        <th>Name</th>
        <th>Role</th>
        <th></th>
        <th></th>

    </tr>
    </thead>
    <tbody>
    <#list users as user>
        <tr>
            <td>${user.username}</td>
            <td><#list user.roles as role>${role}<#sep>, </#list></td>
            <td><a class="button_cl" href="/user/${user.id}"> Edit</a></td>
            <td><a class="button_cl" href="/user/delete/${user.id}"> Delete</a></td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>