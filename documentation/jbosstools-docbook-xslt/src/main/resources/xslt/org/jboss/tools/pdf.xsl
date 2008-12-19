<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:diffmk="http://diffmk.sf.net/ns/diff"> 

   <xsl:import href="classpath:/xslt/org/jboss/pdf.xsl"/>
  
   <!--            overwriting links properties                        -->
   
   <xsl:attribute-set name="xref.properties">
      <xsl:attribute name="text-decoration">
         <xsl:choose>
            <xsl:when test="self::ulink">underline</xsl:when>
            <xsl:when test="self::link">underline</xsl:when>
            <!--xsl:otherwise>inherit</xsl:otherwise-->
         </xsl:choose>
      </xsl:attribute>
   </xsl:attribute-set>
   
   <!--              highlighting meaningful words                      -->
   
   <xsl:template match="property">
      <xsl:variable name="currant" select="child::node()"/>
      <fo:inline color="#0066cc"> 
         <xsl:value-of select="$currant"/> 
      </fo:inline>
   </xsl:template>
   
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
