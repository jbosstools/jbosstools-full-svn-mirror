<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan">
<xsl:output method="html" indent="yes" xalan:indent-amount="4"/>
<xsl:template match="/projects">

<xsl:variable name="baseurl"><xsl:value-of select="@baseurl"/></xsl:variable>

<table cellspacing="1" cellpadding="0" border="0" align="left"
style="width: 100%;" class="simpletablestyle">
    <thead>
        <tr class="header">
            <th>Description</th>
            <th>Version</th>
            <th>Download</th>
        </tr>
    </thead>
    <tbody>
      <xsl:for-each select="project">
	<xsl:variable name="rowClass">
	  <xsl:if test="position()  mod 2 = 1">oddRow</xsl:if>
	  <xsl:if test="position()  mod 2 = 0">evenRow</xsl:if>
	</xsl:variable>
	
        <tr>
	  <xsl:attribute name="class">
	    <xsl:value-of select="$rowClass"/>
	  </xsl:attribute>
	  <td class="rowLine"><xsl:value-of select="@name"/> - <xsl:value-of select="description"/><xsl:if test="string(a) != ''"><xsl:copy-of select="a"/></xsl:if></td>
	  <td class="rowLine"><xsl:value-of select="substring-before(@version,'-')"/></td>
	  <td class="rowLine">
	    <xsl:for-each select="download">
	      <b><a>
		<xsl:attribute name="href"><xsl:value-of select="$baseurl"/><xsl:value-of select="@url"/></xsl:attribute>
		<xsl:attribute name="onclick">javascript:pageTracker._trackPageview('/downloads/<xsl:value-of select="@url"/>');turnOnModal(this); return false;</xsl:attribute>
		<xsl:value-of select="@os"/>
		</a>
		(<xsl:value-of select="@size"/>)
	      </b>
	      <br/>
	    </xsl:for-each>
	  </td>
	</tr>
      </xsl:for-each>
    </tbody>
</table>  

</xsl:template>
</xsl:transform>