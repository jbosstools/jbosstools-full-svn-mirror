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


   <xsl:attribute-set name="header.content.properties">
      <xsl:attribute name="font-family">Helvetica</xsl:attribute>
      <xsl:attribute name="font-size">9pt</xsl:attribute>
      <xsl:attribute name="font-weight">bold</xsl:attribute>
      
   </xsl:attribute-set>
   <xsl:template name="header.content">  
      <xsl:param name="pageclass" select="''"/>
      <xsl:param name="sequence" select="''"/>
      <xsl:param name="position" select="''"/>
      <xsl:param name="gentext-key" select="''"/>
      <fo:block> 
         <!-- sequence can be odd, even, first, blank
            position can be left, center, right-->
         <xsl:choose>
            
            <xsl:when test="$sequence = 'odd' and $position = 'left'">
               <xsl:apply-templates select="." 
                  mode="object.title.markup"/>
            </xsl:when>
            
            <xsl:when test="$sequence = 'odd' and $position = 'center'">
               <xsl:call-template name="draft.text"/>
            </xsl:when>
            
            <xsl:when test="$sequence = 'odd' and $position = 'right'">
               
            </xsl:when>
            
            <xsl:when test="$sequence = 'even' and $position = 'left'">  
               <xsl:apply-templates select="." 
                  mode="object.title.markup"/>
            </xsl:when>
            
            <xsl:when test="$sequence = 'even' and $position = 'center'">
               <xsl:call-template name="draft.text"/>
            </xsl:when>
            
            <xsl:when test="$sequence = 'even' and $position = 'right'">
               
            </xsl:when>
            
            <xsl:when test="$sequence = 'first' and $position = 'left'">
               <xsl:apply-templates select="." 
                  mode="object.title.markup"/>
            </xsl:when>
            
            <xsl:when test="$sequence = 'first' and $position = 'right'">
               
            </xsl:when>
            
            <xsl:when test="$sequence = 'first' and $position = 'center'"> 
               <xsl:value-of 
                  select="ancestor-or-self::book/bookinfo/corpauthor"/>
               
            </xsl:when>
            
            <xsl:when test="$sequence = 'blank' and $position = 'left'">
               <fo:page-number/>
               
            </xsl:when>
            
            <xsl:when test="$sequence = 'blank' and $position = 'center'">
               <xsl:text>This page intentionally left blank</xsl:text>
            </xsl:when>
            
            <xsl:when test="$sequence = 'blank' and $position = 'right'">
            </xsl:when>
            
         </xsl:choose>
      </fo:block>
   </xsl:template>
   
   <xsl:template name="footer.content">
      <xsl:param name="pageclass" select="''"/>
      <xsl:param name="sequence" select="''"/>
      <xsl:param name="position" select="''"/>
      <xsl:param name="gentext-key" select="''"/>
      
      <!--
         <fo:block>
         <xsl:value-of select="$pageclass"/>
         <xsl:text>, </xsl:text>
         <xsl:value-of select="$sequence"/>
         <xsl:text>, </xsl:text>
         <xsl:value-of select="$position"/>
         <xsl:text>, </xsl:text>
         <xsl:value-of select="$gentext-key"/>
         </fo:block>
      -->
      
      <fo:block>
         <!-- pageclass can be front, body, back -->
         <!-- sequence can be odd, even, first, blank -->
         <!-- position can be left, center, right -->
         <xsl:choose>
            <xsl:when test="($sequence = 'odd'or $sequence = 'even' or $sequence = 'blank' or $sequence = 'first') and $position = 'right'">
               <fo:page-number/>
            </xsl:when>
         </xsl:choose>
      </fo:block>
   </xsl:template>
   <!-- added-->
      
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
