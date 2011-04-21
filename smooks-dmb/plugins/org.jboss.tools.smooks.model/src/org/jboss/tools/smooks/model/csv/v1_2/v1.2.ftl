<#if bean.singleBinding??>
    <${nsp}:reader <@writeAttribs attribs="fields,separator,quote,skipLines,rootElementName,recordElementName,indent" /> >
        <@writePreText bean=bean.singleBinding />
        <${nsp}:singleBinding <@writeAttribs attribs="beanId,beanClass@class" bean=bean.singleBinding /> />
    </${nsp}:reader>
<#elseif bean.listBinding??>
    <${nsp}:reader <@writeAttribs attribs="fields,separator,quote,skipLines,rootElementName,recordElementName,indent" /> >
        <@writePreText bean=bean.listBinding />
        <${nsp}:listBinding <@writeAttribs attribs="beanId,beanClass@class" bean=bean.listBinding /> />
    </${nsp}:reader>
<#elseif bean.mapBinding??>
    <${nsp}:reader <@writeAttribs attribs="fields,separator,quote,skipLines,rootElementName,recordElementName,indent" /> >
        <@writePreText bean=bean.mapBinding />
        <${nsp}:mapBinding <@writeAttribs attribs="beanId,beanClass@class,keyField" bean=bean.mapBinding /> />
    </${nsp}:reader>
<#else>
    <${nsp}:reader <@writeAttribs attribs="fields,separator,quote,skipLines,rootElementName,recordElementName,indent" /> />
</#if>