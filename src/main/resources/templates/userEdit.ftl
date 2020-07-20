<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <#import "parts/menu.ftl" as m>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <link rel="stylesheet" href ="../../css/main.css"/>
    <title>Edit user - ${user.username}</title>
</head>
<body>

<div class="container-fluid">
    <@m.menu/>
    <div class="row">
        <div class="col">
            <form>
                <input class="btn btn-primary btn-sm" type="button" value="<--Back" onclick="window.location.replace('http://localhost:8080/admin')">
            </form>
        </div>
    </div>
<div class="row">
    <div class="col">
        <h5 class="card-title">Edit user - ${user.username}</h5>
        <form id="saveUser" action="/admin/save" method="post">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Role 1</th>
                    <th>Role 2</th>
                </tr>
                </thead>
                <tbody>

                <tr>
                    <td>
                        <input class="form-control ${(usernameError??)?string('is-invalid', '')}" type="text" name="newUsername" value="${user.username}">
                        <#if usernameError??>
                            <div class="invalid-feedback">
                                ${usernameError}
                            </div>
                        </#if>
                    </td>
                    <#list roles as role>
                        <td><label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label></td>
                    </#list>
                </tr>

                </tbody>
            </table>
            <#if responseMessage??>
                <div class="alert alert-${responseMessage}" role="alert">
                    Successfully saved!
                </div>
            </#if>
            <#if resetResponseMessage??>
                <div class="alert alert-${resetResponseMessage}" role="alert">
                    Password successfully reset!
                </div>
            </#if>
            <input type="hidden" value="${user.id}" name="userId">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        </form>
    </div>
</div>
    <div class="row">
        <div class="col">
            <input type="submit" class="btn btn-success btn-sm" value="Save" form="saveUser"/>
            <form id="resetPassword" action="/admin/resetUserPassword/${user.id}" method="get">
                <input type="submit" class="btn btn-primary btn-sm" value="Reset password"/>
            </form>
        </div>
    </div>


</div>
<#--<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>-->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
</body>
</html>