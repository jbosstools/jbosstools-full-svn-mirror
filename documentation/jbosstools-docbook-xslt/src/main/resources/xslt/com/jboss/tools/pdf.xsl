<?xml version='1.0'?>

<!--
   Copyright 2008-2009 JBoss by Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:diffmk="http://diffmk.sf.net/ns/diff">

   <xsl:import href="classpath:/xslt/com/jboss/pdf.xsl"/>
   
   <!--            overwriting links properties                        -->
   <xsl:param name="ulink.show" select="0"></xsl:param>

   <xsl:template name="book.titlepage.separator"/>
   <xsl:template name="book.titlepage.verso"/>
   <xsl:template name="book.titlepage3.recto"/>
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
   <!-- added-->
      
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


<xsl:template match="//corpauthor/inlinemediaobject" />

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
   <!--avoid page sequence  to generate blank pages after even page numbers -->
   
   <xsl:template name="force.page.count">
      <xsl:param name="element" select="local-name(.)"/>
      <xsl:param name="master-reference" select="''"/>
      <xsl:text>no-force</xsl:text>
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
   
   <xsl:template name="process.menuchoice">
      <xsl:param name="nodelist" select="guibutton|guiicon|guilabel|guimenu|guimenuitem|guisubmenu|interface"/><!-- not(shortcut) -->
      <xsl:param name="count" select="1"/>
      
      <xsl:variable name="mm.separator">
         <xsl:choose>
            <xsl:when test="($fop.extensions != 0 or $fop1.extensions != 0 ) and
               contains($menuchoice.menu.separator, '&#x2192;')">
               <fo:inline font-family="Symbol" font-weight="normal">
                  <xsl:copy-of select="$menuchoice.menu.separator"/>
               </fo:inline>
            </xsl:when>
            <xsl:otherwise>
               <xsl:copy-of select="$menuchoice.menu.separator"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      
      <xsl:choose>
         <xsl:when test="$count>count($nodelist)"></xsl:when>
         <xsl:when test="$count=1">
            <xsl:apply-templates select="$nodelist[$count=position()]"/>
            <xsl:call-template name="process.menuchoice">
               <xsl:with-param name="nodelist" select="$nodelist"/>
               <xsl:with-param name="count" select="$count+1"/>
            </xsl:call-template>
         </xsl:when>
         <xsl:otherwise>
            <xsl:variable name="node" select="$nodelist[$count=position()]"/>
            <xsl:choose>
               <xsl:when test="local-name($node)='guimenuitem'
                  or local-name($node)='guisubmenu'">
                  <xsl:copy-of select="$mm.separator"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:copy-of select="$menuchoice.separator"/>
               </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="$node"/>
            <xsl:call-template name="process.menuchoice">
               <xsl:with-param name="nodelist" select="$nodelist"/>
               <xsl:with-param name="count" select="$count+1"/>
            </xsl:call-template>
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
