<?xml version='1.0'?>

<!--
  Copyright 2008 JBoss, a division of Red Hat
  License: LGPL
  Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:diffmk="http://diffmk.sf.net/ns/diff" version="1.0">

  <!-- XHTML settings -->
  <xsl:param name="html.stylesheet" select="'css/tools.css'"/>
  
  

  <xsl:template name="head.content"> 
    <xsl:param name="node" select="."/> 
    <xsl:param name="title"> 
      <xsl:apply-templates select="$node" mode="object.title.markup.textonly"/> 
    </xsl:param> 
    
    <title xmlns="http://www.w3.org/1999/xhtml" > 
      <xsl:copy-of select="$title"/> 
    </title> 
    
    <xsl:if test="$html.stylesheet != ''"> 
      <xsl:call-template name="output.html.stylesheets"> 
        <xsl:with-param name="stylesheets" select="normalize-space($html.stylesheet)"/> 
      </xsl:call-template> 
    </xsl:if> 
    
    <xsl:if test="$link.mailto.url != ''"> 
      <link rev="made" href="{$link.mailto.url}"/> 
    </xsl:if> 
    
    <xsl:if test="$html.base != ''"> 
      <base href="{$html.base}"/> 
    </xsl:if> 
    
    <meta xmlns="http://www.w3.org/1999/xhtml" name="generator" content="DocBook {$DistroTitle} V{$VERSION}"/> 
    
    <xsl:if test="$generate.meta.abstract != 0"> 
      <xsl:variable name="info" select="(articleinfo |bookinfo |prefaceinfo |chapterinfo |appendixinfo |sectioninfo |sect1info |sect2info |sect3info |sect4info |sect5info |referenceinfo |refentryinfo |partinfo |info |docinfo)[1]"/> 
      <xsl:if test="$info and $info/abstract"> 
        <meta xmlns="http://www.w3.org/1999/xhtml" name="description"> 
          <xsl:attribute name="content"> 
            <xsl:for-each select="$info/abstract[1]/*"> 
              <xsl:value-of select="normalize-space(.)"/> 
              <xsl:if test="position() &lt; last()"> 
                <xsl:text> </xsl:text> 
              </xsl:if> 
            </xsl:for-each> 
          </xsl:attribute> 
        </meta> 
      </xsl:if> 
    </xsl:if> 
    
    <link rel="shortcut icon" type="image/vnd.microsoft.icon" href="images/favicon.ico" /> 
    
    <xsl:apply-templates select="." mode="head.keywords.content"/>
 
  </xsl:template>
  
  
 
  
  <!--                 Overriding toc.line                          -->
  <xsl:template name="toc.line"> 
    <xsl:param name="toc-context" select="."/> 
    <xsl:param name="depth" select="1"/> 
    <xsl:param name="depth.from.context" select="8"/> 

    <xsl:param name="min-diff" select="10"/> 
    <xsl:param name="pos" select="position()"/> 

    <span> 
      <xsl:attribute name="class"><xsl:value-of select="local-name(.)"/></xsl:attribute> 
      
      <!-- * if $autotoc.label.in.hyperlink is zero, then output the label --> 
      <!-- * before the hyperlinked title (as the DSSSL stylesheet does) --> 
      <xsl:if test="$autotoc.label.in.hyperlink = 0"> 
        <xsl:variable name="label"> 
          <xsl:apply-templates select="." mode="label.markup"/> 
        </xsl:variable> 
        <xsl:copy-of select="$label"/> 
        <xsl:if test="$label != ''"> 
          <xsl:value-of select="$autotoc.label.separator"/> 
        </xsl:if> 
      </xsl:if> 
      
      <a> 
        <xsl:attribute name="href"> 
          <xsl:call-template name="href.target"> 
            <xsl:with-param name="context" select="$toc-context"/> 
            <xsl:with-param name="toc-context" select="$toc-context"/> 
          </xsl:call-template> 
        </xsl:attribute>              
        <!-- * if $autotoc.label.in.hyperlink is non-zero, then output the label --> 
        <!-- * as part of the hyperlinked title --> 
        <xsl:if test="not($autotoc.label.in.hyperlink = 0)"> 
          <xsl:variable name="label"> 
            <xsl:apply-templates select="." mode="label.markup"/> 
          </xsl:variable> 
          <xsl:copy-of select="$label"/> 
          <xsl:if test="$label != ''"> 
            <xsl:value-of select="$autotoc.label.separator"/> 
          </xsl:if> 
        </xsl:if> 
        
        <xsl:apply-templates select="." mode="titleabbrev.markup"/> 
      </a> 
    </span> 
  </xsl:template> 
  <!-- XHTML and PDF -->
  
  
    <!-- This template is used for the release build -->
    <!--xsl:template match="//diffmk:wrapper">
	<xsl:choose>
		<xsl:when test="@diffmk:change='deleted'">
				<xsl:text> </xsl:text>
		 </xsl:when>
		<xsl:when test="parent::node()[local-name()='title']">
				<xsl:value-of select="."/>
		 </xsl:when>
		 <xsl:otherwise>
				<xsl:value-of select="."/> 
		</xsl:otherwise>
	</xsl:choose>
  </xsl:template-->
  
  
 

	<xsl:template match="abstract" mode="titlepage.mode">
	  <xsl:apply-templates select="." mode="class.attribute"/>
	  <xsl:call-template name="paragraph">
	    <xsl:with-param name="content">
	      <xsl:apply-templates mode="titlepage.mode"/>
	    </xsl:with-param>
	  </xsl:call-template>
	</xsl:template>



</xsl:stylesheet>
