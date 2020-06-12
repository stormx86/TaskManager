<#macro pager url page>
    <div class="pagination">

                <a href="#" tabindex="-1">Pages</a>

        <#list 1..page.getTotalPages() as p>
            <#if (p-1) == page.getNumber()>

                    <a class="active" href="#">${p}</a>

            <#else>

                    <a href="${url}?page=${p-1}">${p}</a>

            </#if>
        </#list>

    </div>
</#macro>