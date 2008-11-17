<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

   <xsl:import href="classpath:/xslt/org/jboss/xhtml.xsl"/>
   <xsl:import href="common.xsl"/>

      	<xsl:template name="user.head.content">
		<xsl:param name="node" select="." />
		<script type="text/javascript" src="script/prototype-1.6.0.2.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>
		<script type="text/javascript" src="script/effects.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>
		<script type="text/javascript" src="script/scriptaculous.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>
		<script type="text/javascript" src="script/toggle.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>
	</xsl:template>

  <xsl:template name="header.navigation">
	<xsl:param name="prev" select="/foo"/>
	<xsl:param name="next" select="/foo"/>
	<xsl:param name="nav.context"/>
	<xsl:variable name="home" select="/*[1]"/>
	<xsl:variable name="up" select="parent::*"/>
	<xsl:variable name="row1" select="$navig.showtitles != 0"/>
	<xsl:variable name="row2" select="count($prev) &gt; 0 or (count($up) &gt; 0 and generate-id($up) != generate-id($home) and $navig.showtitles != 0) or count($next) &gt; 0"/>
	<xsl:if test="$suppress.navigation = '0' and $suppress.header.navigation = '0'">
		<xsl:if test="$row1 or $row2">
			<xsl:if test="$row1">
				<div id="overlay">
					<xsl:text> </xsl:text>
				</div>
 

      <!-- FEEDBACK -->
								
								
								
	<div id="feedback-maincontainer" style="display:none">
		<div id="feedback-header">
			Send your remarks, comments or wishes to doc team
		</div>
		<a href="#" onclick="$('feedback-maincontainer').hide(); return false;" id="feedback-close">
			<img src="images/close.png" class="feedback-images" />
		</a>
		<div id="feedback-state"><xsl:text> </xsl:text></div>
	
    		<form id="feedback-mailform">
			<div class="feedback-textbox-div">
			  Subject:<input type="text" id="subject" name="subject" title="Enter the subject of your message" class="feedback-textbox" />
			</div>
			<div class="feedback-textbox-div">
			  <span style="vertical-align: top;">Message:</span>
			  <textarea name="message" title="Type here the text of your message" id="message"><xsl:text> </xsl:text></textarea>
			</div>
			<div class="feedback-textbox-div">
			  Your name:<input type="text" id="name" name="name" title="Enter your name" class="feedback-textbox" />
			</div>
			<div class="feedback-textbox-div">
			  Your email:<input type="text" id="email" name="email" title="Enter your email address" class="feedback-textbox" />
			</div>
			<span class="feedback-button-container">
				<input type="submit" value="Send Message" name="submit" class="feedback-formbutton" title="Send Message" />
			</span>
			<span class="feedback-button-container">
				<input type="reset" value="Clear All Fields" class="feedback-formbutton" title="Clear All Fields" />
			</span>
	    </form>
	</div>
	<div id="feedback-wrapper">
		<a id="feedback-link" href="#" onclick="$('feedback-maincontainer').appear(); return false;">
			<img src="images/feedback_logo.png" class="feedback-images" width="100px"/>
		</a>
	</div>							
								
		
 <!-- FEEDBACK ENDS -->

				<p xmlns="http://www.w3.org/1999/xhtml">
					<xsl:attribute name="id">
						<xsl:text>title</xsl:text>
					</xsl:attribute>
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="$siteHref" />
						</xsl:attribute>
						<xsl:attribute name="class">
							<xsl:text>site_href</xsl:text>
						</xsl:attribute>
						<strong>
						        <xsl:value-of select="$siteLinkText"/>	
						</strong>
					</a>
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="$docHref" />
						</xsl:attribute>
						<xsl:attribute name="class">
							<xsl:text>doc_href</xsl:text>
						</xsl:attribute>
						<strong>
						        <xsl:value-of select="$docLinkText"/>	
						</strong>
					</a>
				</p>
			</xsl:if>
			<xsl:if test="$row2">
				<ul class="docnav" xmlns="http://www.w3.org/1999/xhtml">
					<li class="previous">
						<xsl:if test="count($prev)&gt;0">
							<a accesskey="p">
								<xsl:attribute name="href">
									<xsl:call-template name="href.target">
										<xsl:with-param name="object" select="$prev"/>
									</xsl:call-template>
								</xsl:attribute>
								<strong>
									<xsl:call-template name="navig.content">
										<xsl:with-param name="direction" select="'prev'"/>
									</xsl:call-template>
								</strong>
							</a>
						</xsl:if>
					</li>
					<li class="next">
						<xsl:if test="count($next)&gt;0">
							<a accesskey="n">
								<xsl:attribute name="href">
									<xsl:call-template name="href.target">
										<xsl:with-param name="object" select="$next"/>
									</xsl:call-template>
								</xsl:attribute>
								<strong>
									<xsl:call-template name="navig.content">
										<xsl:with-param name="direction" select="'next'"/>
									</xsl:call-template>
								</strong>
							</a>
						</xsl:if>
					</li>
				</ul>
			</xsl:if>
		</xsl:if>
		<xsl:if test="$header.rule != 0">
			<hr/>
		</xsl:if>
	</xsl:if>
</xsl:template>
</xsl:stylesheet>
