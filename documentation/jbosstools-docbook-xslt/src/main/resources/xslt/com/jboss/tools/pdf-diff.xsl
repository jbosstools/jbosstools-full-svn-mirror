<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:diffmk="http://diffmk.sf.net/ns/diff"> 

   <xsl:import href="classpath:/xslt/org/jboss/pdf.xsl"/>
   <xsl:import href="pdf.xsl"/>
  
   
<xsl:template match="//diffmk:wrapper">
<xsl:choose>
  <xsl:when test="@diffmk:change='deleted'">
      <fo:inline> </fo:inline>
   </xsl:when>
   <xsl:otherwise>
    <fo:inline background-color="#cce2f6"> 
     <xsl:value-of select="."/><xsl:text> </xsl:text>
    </fo:inline>
  </xsl:otherwise>
 </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>
