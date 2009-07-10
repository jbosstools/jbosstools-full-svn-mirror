<?xml version='1.0'?>

<!--
   Copyright 2008-2009 JBoss by Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" 
   xmlns:diffmk="http://diffmk.sf.net/ns/diff" 
   xmlns:xlink="http://www.w3.org/1999/xlink"
   xmlns:stext="http://nwalsh.com/xslt/ext/com.nwalsh.saxon.TextFactory"
   xmlns:xtext="com.nwalsh.xalan.Text"
   xmlns:lxslt="http://xml.apache.org/xslt"
   exclude-result-prefixes="xlink stext xtext lxslt"
   extension-element-prefixes="stext xtext">
   
   <xsl:import href="http://docbook.sourceforge.net/release/xsl/current/fo/pi.xsl"/>
   <xsl:import href="http://docbook.sourceforge.net/release/xsl/current/lib/lib.xsl"/>
   <xsl:import href="http://docbook.sourceforge.net/release/xsl/current/fo/graphics.xsl"/>

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
         <fo:external-graphic src="url('images/new.png')"/>
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
   <!--<xsl:param name="page.margin.top">0.1in</xsl:param>
   <xsl:param name="region.before.extent">0.6in</xsl:param>
   <xsl:param name="body.margin.top">0.9in</xsl:param>--> 
  
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
            
            <!--<xsl:when test="$sequence = 'blank' and $position = 'left'">
               <fo:page-number/>
            
            </xsl:when>
            
            <xsl:when test="$sequence = 'blank' and $position = 'center'">
               <xsl:text>This page intentionally left blank</xsl:text>
            </xsl:when>
            
            <xsl:when test="$sequence = 'blank' and $position = 'right'">
            </xsl:when>-->
            
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
   
   <!--added-->
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
   <!-- ################################################## -->
   <xsl:template name="process.image">
      <!-- When this template is called, the current node should be  -->
      <!-- a graphic, inlinegraphic, imagedata, or videodata. All    -->
      <!-- those elements have the same set of attributes, so we can -->
      <!-- handle them all in one place.                             -->
      
   <xsl:variable name="scalefit">
         <xsl:choose>
            <xsl:when test="$ignore.image.scaling != 0">0</xsl:when>
            <xsl:when test="@contentwidth">0</xsl:when>
            <xsl:when test="@contentdepth and 
               @contentdepth != '100%'">0</xsl:when>
            <xsl:when test="@scale">0</xsl:when>
            <xsl:when test="@scalefit"><xsl:value-of select="@scalefit"/></xsl:when>
            <xsl:when test="@width or @depth">1</xsl:when>
            <xsl:otherwise>1</xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      
      <xsl:variable name="scale">
         <xsl:choose>
            <xsl:when test="$ignore.image.scaling != 0">0</xsl:when>
            <xsl:when test="@contentwidth or @contentdepth">1.0</xsl:when>
            <xsl:when test="@scale">
               <xsl:value-of select="@scale div 100.0"/>
            </xsl:when>
            <xsl:otherwise>1.0</xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      
      <xsl:variable name="filename">
         <xsl:choose>
            <xsl:when test="local-name(.) = 'graphic'
               or local-name(.) = 'inlinegraphic'">
               <!-- handle legacy graphic and inlinegraphic by new template --> 
   <xsl:call-template name="mediaobject.filename">
                  <xsl:with-param name="object" select="."/>
               </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
               <!-- imagedata, videodata, audiodata -->
   <xsl:call-template name="mediaobject.filename">
                  <xsl:with-param name="object" select=".."/>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
          <xsl:variable name="content-type">
         <xsl:if test="@format">
            <xsl:call-template name="graphic.format.content-type">
               <xsl:with-param name="format" select="@format"/>
            </xsl:call-template>
         </xsl:if>
      </xsl:variable>
           <fo:external-graphic >
         <xsl:attribute name="src">
            <xsl:call-template name="fo-external-image">
               <xsl:with-param name="filename">
                  <xsl:if test="$img.src.path != '' and
                     not(starts-with($filename, '/')) and
                     not(contains($filename, '://'))">
                     <xsl:value-of select="$img.src.path"/>
                  </xsl:if>
                  <xsl:value-of select="$filename"/>
               </xsl:with-param>
            </xsl:call-template>
         </xsl:attribute>
         <xsl:attribute name="width">
            100%
         </xsl:attribute>

              <xsl:attribute name="content-width">
                 scale-down-to-fit
         </xsl:attribute>
         <xsl:attribute name="content-height">
            100%
            
         </xsl:attribute>
        <xsl:attribute name="scaling">
            uniform
         </xsl:attribute>     
       
         <xsl:attribute name="height">
            <xsl:choose>
               <xsl:when test="$ignore.image.scaling != 0">auto</xsl:when>
               <xsl:when test="contains(@depth,'%')">
                  <xsl:value-of select="@depth"/>
               </xsl:when>
               <xsl:when test="@depth">
                  <xsl:call-template name="length-spec">
                     <xsl:with-param name="length" select="@depth"/>
                     <xsl:with-param name="default.units" select="'px'"/>
                  </xsl:call-template>
               </xsl:when>
               <xsl:otherwise>auto</xsl:otherwise>
            </xsl:choose>
         </xsl:attribute>
                  <xsl:if test="$content-type != ''">
            <xsl:attribute name="content-type">
               <xsl:value-of select="concat('content-type:',$content-type)"/>
            </xsl:attribute>
         </xsl:if>
  
                  <xsl:if test="@align">
            <xsl:attribute name="text-align">
               <xsl:value-of select="@align"/>
            </xsl:attribute>
         </xsl:if>
         
         <xsl:if test="@valign">
            <xsl:attribute name="display-align">
               <xsl:choose>
                  <xsl:when test="@valign = 'top'">before</xsl:when>
                  <xsl:when test="@valign = 'middle'">center</xsl:when>
                  <xsl:when test="@valign = 'bottom'">after</xsl:when>
                  <xsl:otherwise>auto</xsl:otherwise>
               </xsl:choose>
            </xsl:attribute>
         </xsl:if>
      </fo:external-graphic>
   </xsl:template>
   <!--#################################remove column-width unspecified  Warning########-->
   <xsl:template name="generate.col">
      <!-- generate the table-column for column countcol -->
      <xsl:param name="countcol">1</xsl:param>
      <xsl:param name="colspecs" select="./colspec"/>
      <xsl:param name="count">1</xsl:param>
      <xsl:param name="colnum">1</xsl:param>
      
      <xsl:choose>
         <xsl:when test="$count>count($colspecs)">
            <fo:table-column column-number="{$countcol}" column-width="proportional-column-width(1)">
               <xsl:variable name="colwidth">
                  <xsl:call-template name="calc.column.width"/>
               </xsl:variable>
               <xsl:if test="$colwidth != 'proportional-column-width(1)'">
                  <xsl:attribute name="column-width">
                     <xsl:value-of select="$colwidth"/>
                  </xsl:attribute>
               </xsl:if>
            </fo:table-column>
         </xsl:when>
         <xsl:otherwise>
            <xsl:variable name="colspec" select="$colspecs[$count=position()]"/>
            
            <xsl:variable name="colspec.colnum">
               <xsl:choose>
                  <xsl:when test="$colspec/@colnum">
                     <xsl:value-of select="$colspec/@colnum"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:value-of select="$colnum"/>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:variable>
            
            <xsl:variable name="colspec.colwidth">
               <xsl:choose>
                  <xsl:when test="$colspec/@colwidth">
                     <xsl:value-of select="$colspec/@colwidth"/>
                  </xsl:when>
                  <xsl:otherwise>1*</xsl:otherwise>
               </xsl:choose>
            </xsl:variable>
            
            <xsl:choose>
               <xsl:when test="$colspec.colnum=$countcol">
                  <fo:table-column column-number="{$countcol}" column-width="proportional-column-width(1)">
                     <xsl:variable name="colwidth">
                        <xsl:call-template name="calc.column.width">
                           <xsl:with-param name="colwidth">
                              <xsl:value-of select="$colspec.colwidth"/>
                           </xsl:with-param>
                        </xsl:call-template>
                     </xsl:variable>
                     <xsl:if test="$colwidth != 'proportional-column-width(1)'">
                        <xsl:attribute name="column-width">
                           <xsl:value-of select="$colwidth"/>
                        </xsl:attribute>
                     </xsl:if>
                  </fo:table-column>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:call-template name="generate.col">
                     <xsl:with-param name="countcol" select="$countcol"/>
                     <xsl:with-param name="colspecs" select="$colspecs"/>
                     <xsl:with-param name="count" select="$count+1"/>
                     <xsl:with-param name="colnum">
                        <xsl:choose>
                           <xsl:when test="$colspec/@colnum">
                              <xsl:value-of select="$colspec/@colnum + 1"/>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:value-of select="$colnum + 1"/>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:with-param>
                  </xsl:call-template>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>
   <!-- ########################numeration correction####################### -->
   <xsl:template name="initial.page.number">
      <xsl:param name="element" select="local-name(.)"/>
      <xsl:param name="master-reference" select="''"/>
      
      <!-- Select the first content that the stylesheet places
         after the TOC -->
      <xsl:variable name="first.book.content" 
         select="ancestor::book/*[
         not(self::title or
         self::subtitle or
         self::titleabbrev or
         self::bookinfo or
         self::info or
         self::dedication or
         self::preface or
         self::toc or
         self::lot)][1]"/>
      <xsl:choose>
         <xsl:when test="$element = 'toc'">auto</xsl:when>
         <xsl:when test="$element = 'book'">1</xsl:when>
         <xsl:when test="$element = 'preface'">auto</xsl:when>
         <xsl:when test="($element = 'dedication' or $element = 'article') and
            not(preceding::chapter
            or preceding::preface
            or preceding::appendix
            or preceding::article
            or preceding::dedication
            or parent::part
            or parent::reference)">1</xsl:when>
         <xsl:when test="generate-id($first.book.content) =
            generate-id(.)">1</xsl:when>
         
         <xsl:otherwise>auto</xsl:otherwise>
      </xsl:choose>
   </xsl:template>
</xsl:stylesheet>
