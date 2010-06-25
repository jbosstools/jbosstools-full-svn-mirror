    <${nsp}:freemarker <@writeAttribs attribs="applyOnElement,applyOnElementNS,applyBefore" />>
        <${nsp}:template><![CDATA[${bean.template}]]></${nsp}:template>
        <#if (bean.params.params?size > 0)>
        <#list bean.params.params as param>
        <param name="${param.name}">${param.value}</param>
        </#list>
        </#if>
    </${nsp}:freemarker>