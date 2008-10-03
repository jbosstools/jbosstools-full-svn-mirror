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
          <xsl:when test="@role='new' or @role='updated'"> 
            <xsl:attribute name="class"> 
              <xsl:value-of select="@role"/> 
            </xsl:attribute> 
          </xsl:when> 
	<!-- For mkdiff compatibility-->
	<xsl:when test="@revisionflag='added' or @revisionflag='changed'"> 
            <xsl:attribute name="class"> 
              <xsl:value-of select="@revisionflag"/> 
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

<!-- THERE ARE 2 TEMPLATES FOR ADDING 'ADDED', 'CHANGED' ICONS TO ALL OF THE TITLES-->

<xsl:template name="component.title">
  <xsl:param name="node" select="."/>

  <xsl:variable name="level">
    <xsl:choose>
      <xsl:when test="ancestor::section">
        <xsl:value-of select="count(ancestor::section)+1"/>
      </xsl:when>
      <xsl:when test="ancestor::sect5">6</xsl:when>
      <xsl:when test="ancestor::sect4">5</xsl:when>
      <xsl:when test="ancestor::sect3">4</xsl:when>
      <xsl:when test="ancestor::sect2">3</xsl:when>
      <xsl:when test="ancestor::sect1">2</xsl:when>
      <xsl:otherwise>1</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <!-- Let's handle the case where a component (bibliography, for example)
       occurs inside a section; will we need parameters for this? -->
	
  <xsl:element name="h{$level+1}">
		<xsl:attribute name="class">
			title
		</xsl:attribute>
    <xsl:if test="$generate.id.attributes = 0">
      <xsl:call-template name="anchor">
	<xsl:with-param name="node" select="$node"/>
	<xsl:with-param name="conditional" select="0"/>
      </xsl:call-template>
    </xsl:if>
      <xsl:apply-templates select="$node" mode="object.title.markup">
      <xsl:with-param name="allow-anchors" select="1"/>
    </xsl:apply-templates>
  </xsl:element>
  
  	<xsl:choose> 
		  <xsl:when test="../@role='new' or ../@revisionflag='added'"> 
			<img src="images/new.png" alt="new" class="img_marker" />
		  </xsl:when> 
		<!-- For mkdiff compatibility-->
		<xsl:when test="../@role='updated' or ../@revisionflag='changed'"> 
			<img src="images/updated.png" alt="updated" class="img_marker" />
		</xsl:when>
	</xsl:choose> 
</xsl:template>

<!-- ==================================================================== -->

<xsl:template name="section.heading">
  <xsl:param name="section" select="."/>
  <xsl:param name="level" select="1"/>
  <xsl:param name="allow-anchors" select="1"/>
  <xsl:param name="title"/>
  <xsl:param name="class" select="'title'"/>

  <xsl:variable name="id">
    <xsl:choose>
      <!-- if title is in an *info wrapper, get the grandparent -->
      <xsl:when test="contains(local-name(..), 'info')">
        <xsl:call-template name="object.id">
          <xsl:with-param name="object" select="../.."/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="object.id">
          <xsl:with-param name="object" select=".."/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <!-- HTML H level is one higher than section level -->
  <xsl:variable name="hlevel">
    <xsl:choose>
      <!-- highest valid HTML H level is H6; so anything nested deeper
           than 5 levels down just becomes H6 -->
      <xsl:when test="$level &gt; 5">6</xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$level + 1"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  
  <xsl:element name="h{$hlevel}">
    <xsl:attribute name="class">
	<xsl:value-of select="$class"/>
    </xsl:attribute>
    <xsl:if test="$css.decoration != '0'">
      <xsl:if test="$hlevel&lt;3">
        <xsl:attribute name="style">clear: both</xsl:attribute>
      </xsl:if>
    </xsl:if>
    <xsl:if test="$allow-anchors != 0 and $generate.id.attributes = 0">
      <xsl:call-template name="anchor">
        <xsl:with-param name="node" select="$section"/>
        <xsl:with-param name="conditional" select="0"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="$generate.id.attributes != 0 and not(local-name(.) = 'appendix')">
      <xsl:attribute name="id"><xsl:value-of select="$id"/></xsl:attribute>
    </xsl:if>
    <xsl:copy-of select="$title"/>
  </xsl:element>
 	<xsl:choose> 
		  <xsl:when test="../@role='new' or ../@revisionflag='added'"> 
			<img src="images/new.png" alt="new" class="img_marker" />
		  </xsl:when> 
		<!-- For mkdiff compatibility-->
		<xsl:when test="../@role='updated' or ../@revisionflag='changed'"> 
			<img src="images/updated.png" alt="updated" class="img_marker" />
		</xsl:when>
	</xsl:choose>
</xsl:template>

<!-- ==================================================================== -->


</xsl:stylesheet>
