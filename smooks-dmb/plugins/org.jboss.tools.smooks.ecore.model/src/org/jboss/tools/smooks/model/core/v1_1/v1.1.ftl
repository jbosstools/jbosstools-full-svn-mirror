<?xml version="1.0"?>
<smooks-resource-list <@writeNamespaces indent="22"/>>

<#if bean.params??>
    <@writePreText bean=bean.params />
    <params>
    <#list bean.params.params as param>
        <@writePreText bean=param />
        <param <@writeAttribs bean=param attribs="name,type"/>>${param.value}</param>
    </#list>
    </params>
</#if>
<#list bean.components as component><@writeBean bean=component /></#list>

</smooks-resource-list>