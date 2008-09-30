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
    
    <link rel="shortcut icon" type="image/vnd.microsoft.icon" href="images/favicon0.ico" /> 
    
    <xsl:apply-templates select="." mode="head.keywords.content"/> 
<script type="text/javascript" src="script/toggle.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>

  </xsl:template>
  
  
  <!--                 Overriding toc.line                          -->
  <xsl:template name="toc.line"> 
    <xsl:param name="toc-context" select="."/> 
    <xsl:param name="depth" select="1"/> 
    <xsl:param name="depth.from.context" select="8"/> 
    
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
        
        <xsl:choose> 
          <xsl:when test="@role='new'"> 
            <xsl:attribute name="class"> 
              <xsl:value-of select="@role"/> 
            </xsl:attribute> 
          </xsl:when> 
          <xsl:when test="@role='updated'"> 
            <xsl:attribute name="class"> 
              <xsl:value-of select="@role"/> 
            </xsl:attribute> 
          </xsl:when> 
        </xsl:choose> 
        
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
  
  <xsl:template match="book" mode="toc"> 
    <xsl:param name="toc-context" select="."/> 
    
    <xsl:call-template name="subtoc"> 
      <xsl:with-param name="toc-context" select="$toc-context"/> 
      <xsl:with-param name="nodes" select="part|reference |preface|chapter|appendix |article |bibliography|glossary|index |refentry |bridgehead[$bridgehead.in.toc != 0]"/> 
    </xsl:call-template> 
  </xsl:template>
  
  <!-- XHTML and PDF -->
  <xsl:template match="//node()[@diffmk:change]">
  	<xsl:choose>
  		 <xsl:when test="local-name()='note' or local-name()='tip' or local-name()='important' or local-name()='warning' or local-name()='caution'"> 
  			<xsl:call-template name="my.graphical.admonition"/>
		</xsl:when> 
         <xsl:when test="local-name()='diffmk:wrapper'">
         	<span class="diffmkwrapper">
  			<xsl:value-of select="."/> 
  		</span>
         </xsl:when>
  	</xsl:choose>
  </xsl:template>
  <xsl:template name="my.graphical.admonition">
	<xsl:variable name="admon.type">
		<xsl:choose>
			<xsl:when test="local-name(.)='note'">Note</xsl:when>
			<xsl:when test="local-name(.)='warning'">Warning</xsl:when>
			<xsl:when test="local-name(.)='caution'">Caution</xsl:when>
			<xsl:when test="local-name(.)='tip'">Tip</xsl:when>
			<xsl:when test="local-name(.)='important'">Important</xsl:when>
			<xsl:otherwise>Note</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:variable name="alt">
		<xsl:call-template name="gentext">
			<xsl:with-param name="key" select="$admon.type"/>
		</xsl:call-template>
	</xsl:variable>

	<div xmlns="http://www.w3.org/1999/xhtml">
	 	<xsl:apply-templates select="." mode="class.attribute"/>
		<xsl:if test="$admon.style != ''">
			<xsl:attribute name="style">
				<xsl:value-of select="$admon.style"/>
			</xsl:attribute>
		</xsl:if>
		<xsl:call-template name="anchor"/>
		<xsl:if test="$admon.textlabel != 0 or title">
			<h2>
				<xsl:apply-templates select="." mode="object.title.markup"/>
			</h2>
		</xsl:if>
		<div class="diffmkwrapper">
  			<xsl:apply-templates /> 
		</div>
	</div>
	</xsl:template>
	<xsl:template match="abstract" mode="titlepage.mode">
	  <xsl:apply-templates select="." mode="class.attribute"/>
	  <xsl:call-template name="paragraph">
	    <xsl:with-param name="content">
	      <xsl:apply-templates mode="titlepage.mode"/>
	    </xsl:with-param>
	  </xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
