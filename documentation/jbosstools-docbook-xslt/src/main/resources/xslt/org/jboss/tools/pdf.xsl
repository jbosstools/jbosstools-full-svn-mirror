<?xml version='1.0'?>

<!--
   Copyright 2008-2009 JBoss by Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:diffmk="http://diffmk.sf.net/ns/diff"> 

   <xsl:import href="classpath:/xslt/org/jboss/pdf.xsl"/>
   <xsl:param name="force.page.count" select="no-force"/>
   
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
   <xsl:template name="callout-bug">
      <xsl:param name="conum" select='1'/>
      
      <xsl:choose>
         <!-- Draw callouts as images -->
         <xsl:when test="$callout.graphics != '0'
            and $conum &lt;= $callout.graphics.number.limit">
            <xsl:variable name="filename"
               select="concat($callout.graphics.path, $conum,
               $callout.graphics.extension)"/>
            
            <fo:external-graphic content-width="{$callout.icon.size}"
               width="{$callout.icon.size}" padding="0.0em" margin="0.0em">
               <xsl:attribute name="src">
                  <xsl:choose>
                     <xsl:when test="$passivetex.extensions != 0
                        or $fop.extensions != 0
                        or $arbortext.extensions != 0">
                        <xsl:value-of select="$filename"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:text>url(</xsl:text>
                        <xsl:value-of select="$filename"/>
                        <xsl:text>)</xsl:text>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:attribute>
            </fo:external-graphic>
         </xsl:when>
         
         <xsl:when test="$callout.unicode != 0
            and $conum &lt;= $callout.unicode.number.limit">
            <xsl:variable name="comarkup">
               <xsl:choose>
                  <xsl:when test="$callout.unicode.start.character = 10102">
                     <xsl:choose>
                        <xsl:when test="$conum = 1">&#10102;</xsl:when>
                        <xsl:when test="$conum = 2">&#10103;</xsl:when>
                        <xsl:when test="$conum = 3">&#10104;</xsl:when>
                        <xsl:when test="$conum = 4">&#10105;</xsl:when>
                        <xsl:when test="$conum = 5">&#10106;</xsl:when>
                        <xsl:when test="$conum = 6">&#10107;</xsl:when>
                        <xsl:when test="$conum = 7">&#10108;</xsl:when>
                        <xsl:when test="$conum = 8">&#10109;</xsl:when>
                        <xsl:when test="$conum = 9">&#10110;</xsl:when>
                        <xsl:when test="$conum = 10">&#10111;</xsl:when>
                     </xsl:choose>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:message>
                        <xsl:text>Don't know how to generate Unicode callouts </xsl:text>
                        <xsl:text>when $callout.unicode.start.character is </xsl:text>
                        <xsl:value-of select="$callout.unicode.start.character"/>
                     </xsl:message>
                     <fo:inline background-color="#404040"
                        force-page-count="no-force"
                        color="white"
                        padding-top="0.1em"
                        padding-bottom="0.1em"
                        padding-start="0.2em"
                        padding-end="0.2em"
                        baseline-shift="0.1em"
                        font-family="{$body.fontset}"
                        font-weight="bold"
                        font-size="75%">
                        <xsl:value-of select="$conum"/>
                     </fo:inline>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:variable>
            
            <xsl:choose>
               <xsl:when test="$callout.unicode.font != ''">
                  <fo:inline font-family="{$callout.unicode.font}">
                     <xsl:copy-of select="$comarkup"/>
                  </fo:inline>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:copy-of select="$comarkup"/>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:when>
         
         <!-- Most safe: draw a dark gray square with a white number inside -->
         <xsl:otherwise>
            <fo:inline background-color="#404040"
               force-page-count="no-force"
               color="white"
               padding-top="0.1em"
               padding-bottom="0.1em"
               padding-start="0.2em"
               padding-end="0.2em"
               baseline-shift="0.1em"
               font-family="{$body.fontset}"
               font-weight="bold"
               font-size="75%">
               <xsl:value-of select="$conum"/>
            </fo:inline>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template> 
   
   <!--avoid page sequence  to generate blank pages after even page numbers -->
   
   <xsl:template name="force.page.count">
      <xsl:param name="element" select="local-name(.)"/>
      <xsl:param name="master-reference" select="''"/>
      <xsl:text>no-force</xsl:text>
   </xsl:template>
   
   <!-- adding corpauthor entry to the titlepage -->
   
   <xsl:template name="book.titlepage.recto">
      <xsl:choose>
         <xsl:when test="bookinfo/title">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="bookinfo/title" />
         </xsl:when>
         
         <xsl:when test="info/title">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="info/title" />
         </xsl:when>
         <xsl:when test="title">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="title" />
         </xsl:when>
      </xsl:choose>
      
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
         select="bookinfo/issuenum" />
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
         select="info/issuenum" />
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
         select="issuenum" />
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/corpauthor"/>
      
      <xsl:choose>
         <xsl:when test="bookinfo/subtitle">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="bookinfo/subtitle" />
         </xsl:when>
         <xsl:when test="info/subtitle">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="info/subtitle" />
         </xsl:when>
         <xsl:when test="subtitle">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="subtitle" />
         </xsl:when>
      </xsl:choose>
      
      <fo:block xsl:use-attribute-sets="book.titlepage.recto.style"
         font-size="14pt" space-before="15.552pt">
         <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
            select="bookinfo/releaseinfo" />
      </fo:block>
      
      <fo:block text-align="center" space-before="15.552pt">
         <xsl:call-template name="person.name.list">
            <xsl:with-param name="person.list" select="bookinfo/authorgroup/author|bookinfo/authorgroup/corpauthor" />
            <xsl:with-param name="person.type" select="'author'"/>
         </xsl:call-template>
      </fo:block>
      
      <fo:block text-align="center" space-before="15.552pt">
         <xsl:call-template name="person.name.list">
            <xsl:with-param name="person.list" select="bookinfo/authorgroup/editor" />
            <xsl:with-param name="person.type" select="'editor'"/>
         </xsl:call-template>
      </fo:block>
      
      <fo:block text-align="center" space-before="15.552pt">
         <xsl:call-template name="person.name.list">
            <xsl:with-param name="person.list" select="bookinfo/authorgroup/othercredit" />
            <xsl:with-param name="person.type" select="'othercredit'"/>
         </xsl:call-template>
      </fo:block>
      
   </xsl:template>
   
   <xsl:template match="corpauthor" mode="book.titlepage.recto.mode">
      <fo:block>
         <xsl:apply-templates mode="book.titlepage.recto.mode"/>
      </fo:block>
   </xsl:template>
   
</xsl:stylesheet>
