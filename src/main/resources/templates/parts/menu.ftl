<#macro menu>
    <#assign security=JspTaglibs["http://www.springframework.org/security/tags"]/>
    <div class="row">
        <div class="col">
            <span style="font-size: 16px" id="logged_user">Logged-in as: <b><i><a href="/user/${loggedUser}">${loggedUser}</a></i></b></span>
            <br><br>
            <form action="/logout" method="post">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <input type="submit" class="btn btn-outline-dark btn-sm" value="Sign Out"/>
            </form>

            <form action="/refresh" method="post">
                <input type="submit" class="btn btn-outline-dark btn-sm" value="Refresh"/>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            </form>
            <@security.authorize access="hasAnyAuthority('ADMIN')">
                <form action="/admin" method="get">
                    <input type="submit" class="btn btn-outline-primary btn-sm" value="Admin Panel" />
                </form>
            </@security.authorize>

            <span id="ct" class="checktask"></span>

        </div>
    </div>
</#macro>
