<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <#import "parts/menu.ftl" as m>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <link rel="stylesheet" href ="../css/main.css"/>
    <title>Admin Panel</title>
</head>
<body>
<div class="container-fluid">
    <@m.menu/>
    <br>
    <div class="row">
        <div class="col">
    <form action="/" method="get">
        <input type="submit" class="btn btn-primary btn-sm" value="<--Back"/>
    </form>
    <form action="/editTask/deleteAll" method="post">
        <input type="submit" class="btn btn-outline-danger btn-sm" value="Delete All Tasks"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
        </div>
    </div>
    <br>
    <div class="row">
        <div class="col-4">
            <hr>
            <h5 class="card-title">Create new user</h5>
            <div class="row">
                <div class="col-5">
                    <form id="addUser" action="/admin/addUser" method="post">
                        <input class="form-control ${(usernameError??)?string('is-invalid', '')}" type="text" name="username" placeholder="Enter username">
                        <#if usernameError??>
                            <div class="invalid-feedback">
                                ${usernameError}
                            </div>
                        </#if>
                        <br>
                        <#if responseMessage??>
                            <div class="alert alert-${responseMessage}" role="alert">
                                Successfully created!
                            </div>
                        </#if>
                    </form>
                </div>
                <div class="col-1">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" form="addUser"/>
                    <input type="submit" class="btn btn-success btn-sm" value="Create" form="addUser">
                </div>

            </div>
            <hr>
            <h5 class="card-title">User List</h5>
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th style="width: 50%">Name</th>
                    <th>Role</th>
                    <th style="width: 20%">Manage</th>
                </tr>
                </thead>
                <tbody>
                <#list users as user>
                    <tr>
                        <td>${user.username}</td>
                        <td><#list user.roles as role>${role}<#sep>, </#list></td>
                        <td><a class="button_cl" href="/admin/${user.id}"> Edit</a><a class="button_cl" href="/admin/delete/${user.id}"> Delete</a></td>

                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>

</div>
<#--<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>-->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
</body>
</html>