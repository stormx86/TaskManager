<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link rel="stylesheet" href ="css/login.css"/>
    <title>Sign In Page Page</title>
</head>
<body>

<div class="ribbon"></div>
<div class="login">
    <h1>Task Manager</h1>
    <p>Sign In Please</p>
    <form action="/login" method="post">
        <div class="input">
            <div class="blockinput">
                <i class="icon-envelope-alt"></i><input type="text" name="username" placeholder="Username">
            </div>
            <div class="blockinput">
                <i class="icon-unlock"></i><input type="password" name="password" placeholder="Password">
            </div>
        </div>
        <#if RequestParameters.error??>
            <div>
                <span style="font-size:15px; color: #ff362a;">Incorrect username or password</span>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button>Login</button>
    </form>
</div>

</body>
</html>