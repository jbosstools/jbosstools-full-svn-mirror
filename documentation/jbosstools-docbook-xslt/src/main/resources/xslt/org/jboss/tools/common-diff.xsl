<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:diffmk="http://diffmk.sf.net/ns/diff" version="1.0">

  <!-- This template is used for the diffmk build -->
  <xsl:template match="//diffmk:wrapper">
	<xsl:choose>
		<xsl:when test="@diffmk:change='deleted'">
				<xsl:text> </xsl:text>
		 </xsl:when>
		<xsl:when test="parent::node()[local-name()='title']">
				<xsl:value-of select="."/>
		 </xsl:when>
		 <xsl:otherwise>
			<span class="diffmkwrapper">
				<xsl:value-of select="."/> 
			</span>
		</xsl:otherwise>
	</xsl:choose>
  </xsl:template>
 </xsl:stylesheet>

