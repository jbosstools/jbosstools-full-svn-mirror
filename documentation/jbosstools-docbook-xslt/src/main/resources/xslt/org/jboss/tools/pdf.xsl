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
    <fo:inline> 
     <xsl:value-of select="."/><xsl:text> </xsl:text>
    </fo:inline>
</xsl:template>
  
     <!--###################################################
      Custom TOC (bold chapter titles)
      ################################################### -->

   <!-- Improve the TOC. -->
   <xsl:template name="toc.line">
      <xsl:variable name="id">
         <xsl:call-template name="object.id" />
      </xsl:variable>

      <xsl:variable name="label">
         <xsl:apply-templates select="." mode="label.markup" />
      </xsl:variable>

      <fo:block text-align-last="justify" end-indent="{$toc.indent.width}pt"
         last-line-end-indent="-{$toc.indent.width}pt">
         <fo:inline keep-with-next.within-line="always">
            <fo:basic-link internal-destination="{$id}">

               <!-- Chapter titles should be bold. -->
               <xsl:choose>
                  <xsl:when test="local-name(.) = 'chapter'">
                     <xsl:attribute name="font-weight">bold</xsl:attribute>
                  </xsl:when>
               </xsl:choose>

               <xsl:if test="$label != ''">
                  <xsl:copy-of select="$label" />
                  <xsl:value-of select="$autotoc.label.separator" />
               </xsl:if>
               <xsl:apply-templates select="." mode="titleabbrev.markup" />
            </fo:basic-link>
         </fo:inline>
                           
         <!--xsl:choose>       
		<xsl:when test="local-name(.)='section' and current()/@diffmk:change='added'">
			<xsl:call-template name="markerTypes">
	    			<xsl:with-param name="marker-type">attribute</xsl:with-param>
				<xsl:with-param name="marker-value">added</xsl:with-param>
			</xsl:call-template>
		</xsl:when>	
		<xsl:when test="local-name(.)='chapter' and current()/@diffmk:change='added'">
			<xsl:call-template name="markerTypes">
	    			<xsl:with-param name="marker-type">attribute</xsl:with-param>
				<xsl:with-param name="marker-value">added</xsl:with-param>
			</xsl:call-template>
		</xsl:when>	
		<xsl:when test="local-name(.)='section' and current()//diffmk:wrapper">
			<xsl:call-template name="markerTypes">
	    			<xsl:with-param name="marker-type">attribute</xsl:with-param>
				<xsl:with-param name="marker-value">updated</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
	</xsl:choose-->
	<fo:external-graphic src="images/new.png"/>
         <fo:inline keep-together.within-line="always">
            <xsl:text> </xsl:text>
            <fo:leader leader-pattern="dots" leader-pattern-width="3pt"
               leader-alignment="reference-area"
               keep-with-next.within-line="always" />
            <xsl:text> </xsl:text>
            <fo:basic-link internal-destination="{$id}">
               <fo:page-number-citation ref-id="{$id}" />
            </fo:basic-link>
         </fo:inline>
      </fo:block>
   </xsl:template>
</xsl:stylesheet>
