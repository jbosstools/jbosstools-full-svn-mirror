<?xml version='1.0'?>
 
<!--
	Copyright 2009 JBoss by Red Hat
	License: LGPL
    Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

   <xsl:import href="classpath:/xslt/com/jboss/xhtml-single.xsl"/>
   <xsl:import href="common.xsl"/>

   <xsl:template name="book.titlepage.recto">
      <div id="overlay">
         <xsl:text> </xsl:text>
      </div>
      
      <!-- FEEDBACK -->
      <xsl:call-template name="feedback" />
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
      <xsl:choose>
         <xsl:when test="bookinfo/title">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/title"/>
         </xsl:when>
         <xsl:when test="info/title">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/title"/>
         </xsl:when>
         <xsl:when test="title">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="title"/>
         </xsl:when>
      </xsl:choose>
      
      <xsl:choose>
         <xsl:when test="bookinfo/subtitle">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/subtitle"/>
         </xsl:when>
         <xsl:when test="info/subtitle">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/subtitle"/>
         </xsl:when>
         <xsl:when test="subtitle">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="subtitle"/>
         </xsl:when>
      </xsl:choose>
      
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/corpauthor"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/corpauthor"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/authorgroup"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/authorgroup"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/author"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/author"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/othercredit"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/othercredit"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/releaseinfo"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/releaseinfo"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/copyright"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/copyright"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/legalnotice"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/legalnotice"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/pubdate"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/pubdate"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/revision"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/revision"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/revhistory"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/revhistory"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/abstract"/>
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/abstract"/>
   </xsl:template>
</xsl:stylesheet>
