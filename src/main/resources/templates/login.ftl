<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sign In Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href ="css/login.css">
</head>
<body>
<div class="login-form">
    <form action="/login" method="post">
        <h4 align="center">Task Manager</h4>
        <p align="center">Sign In Please</p>
        <div class="form-group">
            <input type="text" class="form-control" name="username" placeholder="Username">
        </div>
        <div class="form-group">
            <input type="password" class="form-control" name="password" placeholder="Password">
            <#if RequestParameters.error??>
                <div>
                    <span style="font-size:15px; color: #ff362a;">Incorrect username or password</span>
                </div>
            </#if>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block">Sign in</button>
        </div>
    </form>
</div>
</body>
</html>