<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:diffmk="http://diffmk.sf.net/ns/diff">
   <xsl:import href="classpath:/xslt/org/jboss/xhtml.xsl"/>
   <xsl:import href="common.xsl"/>
   <xsl:import href="xhtml-single.xsl"/>
   <xsl:param name="html.stylesheet" select="'css/tools_release.css'"/>
   <xsl:template match="//diffmk:wrapper">
		<xsl:value-of select="."/>
	</xsl:template>
</xsl:stylesheet>
