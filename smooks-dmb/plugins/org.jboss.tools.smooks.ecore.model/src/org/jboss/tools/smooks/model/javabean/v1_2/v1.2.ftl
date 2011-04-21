<#assign numChildElemenst = (bean.valueBindings?size + bean.wireBindings?size + bean.expressionBindings?size) > 
<#if (numChildElemenst > 0)>
    <${nsp}:bean <@writeAttribs attribs="beanId,beanClass@class,createOnElement,createOnElementNS"/>>
    <#list bean.valueBindings as valueBinding>
        <@writePreText bean=valueBinding />
    <#assign numDecodeParams = (valueBinding.decodeParams?size) >
    <#if (numDecodeParams > 0)>
        <${nsp}:value <@writeAttribs bean=valueBinding attribs="property,setterMethod,data,dataNS,decoder,defaultVal@default"/>>
            <#list valueBinding.decodeParams as decodeParam>
            <@writePreText bean=decodeParam />
            <${nsp}:decodeParam <@writeAttribs bean=decodeParam attribs="name"/>>${decodeParam.value}</${nsp}:decodeParam>
            </#list>
        </${nsp}:value>
    <#else>
        <${nsp}:value <@writeAttribs bean=valueBinding attribs="property,setterMethod,data,dataNS,decoder,defaultVal@default"/> />
    </#if>
    </#list>
    <#list bean.wireBindings as wireBinding>
        <@writePreText bean=wireBinding />
        <${nsp}:wiring <@writeAttribs bean=wireBinding attribs="property,setterMethod,beanIdRef,wireOnElement,wireOnElementNS"/> />
    </#list>
    <#list bean.expressionBindings as expressionBinding>
        <@writePreText bean=expressionBinding />
        <${nsp}:expression <@writeAttribs bean=expressionBinding attribs="property,setterMethod,execOnElement,execOnElementNS,initVal"/> />
    </#list>
    </${nsp}:bean>
<#else>
    <${nsp}:bean <@writeAttribs attribs="beanId,beanClass@class,createOnElement,createOnElementNS"/> />
</#if>
