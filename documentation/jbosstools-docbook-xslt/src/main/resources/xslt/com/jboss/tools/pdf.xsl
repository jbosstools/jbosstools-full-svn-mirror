<?xml version='1.0'?>

<!--
   Copyright 2008-2009 JBoss by Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:diffmk="http://diffmk.sf.net/ns/diff">

   <xsl:import href="classpath:/xslt/com/jboss/pdf.xsl"/>
   
   <!--            overwriting links properties                        -->
   <xsl:param name="ulink.show" select="0"></xsl:param>
  
   <xsl:attribute-set name="xref.properties">
      <xsl:attribute name="text-decoration">
         <xsl:choose>
            <xsl:when test="self::ulink">underline</xsl:when>
            <xsl:when test="self::link">underline</xsl:when>
            <!--xsl:otherwise>inherit</xsl:otherwise-->
         </xsl:choose>
      </xsl:attribute>
   </xsl:attribute-set>
   
<!--             note, impotent & tip boxes bgcolor overwriting          -->
   <xsl:attribute-set name="graphical.admonition.properties">
      
      <xsl:attribute name="color">
         <xsl:choose>
            <xsl:when test="self::note">#4C5253</xsl:when>
            <xsl:when test="self::caution">#533500</xsl:when>
            <xsl:when test="self::important">white</xsl:when>
            <xsl:when test="self::warning">white</xsl:when>
            <xsl:when test="self::tip">white</xsl:when>
            <xsl:otherwise>white</xsl:otherwise>
         </xsl:choose>
      </xsl:attribute>
      
      <xsl:attribute name="background-color">
         <xsl:choose>
            <xsl:when test="self::note">#B5BCBD</xsl:when>
            <xsl:when test="self::caution">#E3A835</xsl:when>
            <xsl:when test="self::important">#5d7694</xsl:when>
            <xsl:when test="self::warning">#7B1E1E</xsl:when>
            <xsl:when test="self::tip">#859986</xsl:when>
            <!--xsl:otherwise>#404040</xsl:otherwise-->
         </xsl:choose>
      </xsl:attribute>
      
      <xsl:attribute name="space-before.optimum">1em</xsl:attribute>
      <xsl:attribute name="space-before.minimum">0.8em</xsl:attribute>
      <xsl:attribute name="space-before.maximum">1.2em</xsl:attribute>
      <xsl:attribute name="space-after.optimum">1em</xsl:attribute>
      <xsl:attribute name="space-after.minimum">0.8em</xsl:attribute>
      <xsl:attribute name="space-after.maximum">1em</xsl:attribute>
      <xsl:attribute name="padding-bottom">12pt</xsl:attribute>
      <xsl:attribute name="padding-top">12pt</xsl:attribute>
      <xsl:attribute name="padding-right">12pt</xsl:attribute>
      <xsl:attribute name="padding-left">12pt</xsl:attribute>
      <xsl:attribute name="margin-left">
         <xsl:value-of select="$title.margin.left"/>
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
    <fo:inline> 
     <xsl:value-of select="."/><xsl:text> </xsl:text>
    </fo:inline>
</xsl:template>
   <!--avoid page sequence  to generate blank pages after even page numbers -->
   
   <xsl:template name="force.page.count">
      <xsl:param name="element" select="local-name(.)"/>
      <xsl:param name="master-reference" select="''"/>
      <xsl:text>no-force</xsl:text>
   </xsl:template>
</xsl:stylesheet>
