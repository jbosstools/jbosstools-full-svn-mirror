<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:diffmk="http://diffmk.sf.net/ns/diff">

   <xsl:import href="classpath:/xslt/com/jboss/pdf.xsl"/>
   
   
   <!--xsl:template name="toc.line">
      <xsl:param name="toc-context" select="NOTANODE"/>  
      <xsl:variable name="id">  
         <xsl:call-template name="object.id"/>
      </xsl:variable>
      
      <xsl:variable name="label">  
         <xsl:apply-templates select="." mode="label.markup"/>  
      </xsl:variable>
      
      <fo:block xsl:use-attribute-sets="toc.line.properties">  
         <fo:inline keep-with-next.within-line="always">
            
            <fo:basic-link internal-destination="{$id}" 
                                       background-image="new.png"
                                       background-repeat="no-repeat"
                                       background-position-horizontal="right">  
               
              
               <xsl:if test="$label != ''">
                  <xsl:copy-of select="$label"/>
                  <xsl:value-of select="$autotoc.label.separator"/>
               </xsl:if>
               <xsl:apply-templates select="." mode="title.markup"/>  
            </fo:basic-link>
         </fo:inline>
         <fo:inline keep-together.within-line="always"> 
            <xsl:text> </xsl:text>
            <fo:leader leader-pattern="dots"
               leader-pattern-width="3pt"
               leader-alignment="reference-area"
               keep-with-next.within-line="always"/>
            <xsl:text> </xsl:text>
            <fo:basic-link internal-destination="{$id}">
               <fo:page-number-citation ref-id="{$id}"/>
            </fo:basic-link>
         </fo:inline>
      </fo:block>
      </xsl:template-->

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

   <!-- tables headers bgcolor overwriting  -->
   
   <!--xsl:template name="table.cell.block.properties">
      <xsl:if test="ancestor::thead or ancestor::tfoot">
         <xsl:attribute name="font-weight">bold</xsl:attribute>
         <xsl:attribute name="background-color">green</xsl:attribute>
         <xsl:attribute name="border-color">green</xsl:attribute>
         <xsl:attribute name="color">white</xsl:attribute>
      </xsl:if>
   </xsl:template-->
   
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
         <xsl:value-of select="."/> 
         </fo:inline>
  </xsl:otherwise>
 </xsl:choose>
  </xsl:template>   
</xsl:stylesheet>
