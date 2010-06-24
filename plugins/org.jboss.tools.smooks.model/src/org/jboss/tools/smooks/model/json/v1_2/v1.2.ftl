<#if bean.keyMap??>
    <${nsp}:reader <@writeAttribs attribs="rootName,arrayElementName,nullValueReplacement,keyWhitspaceReplacement,keyPrefixOnNumeric,illegalElementNameCharReplacement,indent" />>
        <${nsp}:keyMap>
            <#assign keys = bean.keyMap?keys>
            <#list keys as from>
            <${nsp}:key from="${from?html}" to="${bean.keyMap[from]}"/>
            </#list>
        </${nsp}:keyMap>
    </${nsp}:reader>
<#else>
    <${nsp}:reader <@writeAttribs attribs="rootName,arrayElementName,nullValueReplacement,keyWhitspaceReplacement,keyPrefixOnNumeric,illegalElementNameCharReplacement,indent" /> />
</#if>
    