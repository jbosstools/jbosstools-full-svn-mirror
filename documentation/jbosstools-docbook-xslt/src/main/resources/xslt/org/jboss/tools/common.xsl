<?xml version='1.0'?>

<!--
  Copyright 2009 JBoss by Red Hat
  License: LGPL
  Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:diffmk="http://diffmk.sf.net/ns/diff" version="1.0">

  <!-- XHTML settings -->
  <xsl:param name="html.stylesheet" select="'css/tools.css'"/>
  <xsl:template name="feedback">
    <!--[if IE 6]><iframe frameborder="0" class="problemLayer" id="place"><xsl:text> </xsl:text></iframe><![endif]-->
    <div class="time_out_div" id="timeOutDiv"><xsl:text> </xsl:text></div>
    <div id="feedback-maincontainer">
      <h3 id="feedback-header">
        Create new JBoss Tools Documentation Jira issue
        <a href="javascript:void(0);" onclick="hidePopup('feedback-maincontainer', 'feedback-mailform', 'feedback-iFrame','feedback-submit', 'feedback-maincontainer');" id="feedback-close">
          <img src="images/close_org.png" class="feedback-images" />
        </a>
      </h3>
      <iframe id='feedback-iFrame' name="feedback-iFrame"><xsl:text> </xsl:text></iframe>
      <form onsubmit="return validate_form()" id="feedback-mailform" method="post" action="https://jira.jboss.org/jira/secure/CreateIssueDetails!init.jspa?pid=10020&amp;issuetype=3" target="feedback-iFrame">
        <input type="hidden" id="priority" name="priority" value="3" />
        <input type="hidden" id="components" name="components" value="12311170" />
        <input type="hidden" id="versions" name="versions" value="12312451" />
        <input type="hidden" id="customfield_12310031" name="customfield_12310031" value="Documentation (Ref Guide, User Guide, etc.)" />
        
        <label for="summary">Summary</label>
        <input type="text" id="feedback-summary" name="feedback-summary" title="Summarize the subject of the issue in a few words" maxlength="255"  onKeyDown="countLeft('feedback-summary', 'left', 255);" 
          onKeyUp="countLeft('feedback-summary', 'left', 255);"/>
        <div id="summary-helper-left" class="feedback-helper">
          <span id="left">255</span> characters left
        </div>
        <div class="clear"><xsl:text> </xsl:text></div>
        <label for="feedback-description">Description</label>
        <textarea id="feedback-description" name="feedback-description" title="Provide more details about the issue" onKeyDown="countLeft('feedback-description', 'none', 500);" 
          onKeyUp="countLeft('feedback-description', 'none', 500);"><xsl:text> </xsl:text></textarea>
        <div class="clear"><xsl:text> </xsl:text></div>
        <label for="feedback-environment">Environment</label>
        <textarea id="feedback-environment" name="feedback-environment" title="Describe your environment"><xsl:text> </xsl:text></textarea>
      </form>
      <div id="guide_words">This will launch the Jboss Tools  Jira page - to complete your feedback please login if needed, and submit the Jira.</div>
      <input type="button" id="feedback-submit" value="Proceed to Jira" name="submit" class="feedback-formbutton" title="Proceed to create new issue" onclick="fillForm('feedback-mailform'); submitForm('feedback-mailform', 'feedback-iFrame', 'feedback-submit', 'feedback-maincontainer');"/>
    </div>
    <div id="feedback-wrapper">
      <a id="feedback-link" onclick="showPopup('feedback-maincontainer');">
        <img src="images/feedback_logo.png" class="feedback-images" onload="init('feedback-summary', 'feedback-description');"/>
      </a>
    </div>							
  </xsl:template>
  

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
    <script type="text/javascript" src="script/toggle.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>
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
    <xsl:template match="//diffmk:wrapper">
	<xsl:value-of select="."/>
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
