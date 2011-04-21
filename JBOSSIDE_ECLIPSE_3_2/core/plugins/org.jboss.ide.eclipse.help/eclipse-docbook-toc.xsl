<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">
 
 	<xsl:param name="build-dir"/>
 	<xsl:param name="label"/>
 	
  <xsl:template match="chapter">
    <xsl:if test="not(@id)">
    	<xsl:message>'id' attribute is not present on element 'chapter' titled: .</xsl:message>
    </xsl:if>
    <xsl:text>
    </xsl:text>
    <topic label="{title}"  href="{$build-dir}/en/html/{@id}.html">
      <xsl:apply-templates/>
    </topic>
  </xsl:template>

  <xsl:template match="sect1|sect2|sect3">
    <xsl:if test="not(@id)">
    	<xsl:message>'id' attribute is not present on 'sect' element titled: '<xsl:value-of select="title"/>'
    	<xsl:text> </xsl:text>in module: <xsl:value-of select="ancestor::chapter/@id"/>.xml
    	</xsl:message>
    </xsl:if>  
    <xsl:text>
    </xsl:text>
    <topic label="{title}"  href="{$build-dir}/en/html/{ancestor::chapter/@id}.html#{@id}">
      <xsl:apply-templates/>
    </topic>
  </xsl:template>

  <xsl:template match="preface">
    <xsl:text>
    </xsl:text> 
    <topic label="{title}"  href="{$build-dir}/en/html/preface.html">
      <xsl:apply-templates/>
    </topic>
  </xsl:template>

  <xsl:template match="book">
    <?NLS TYPE="org.eclipse.help.toc"?>
    <toc label="{$label}" >
    <xsl:text>
    </xsl:text>  
      <topic label="Table of Contents" href="{$build-dir}/en/html/index.html"/>
      <xsl:apply-templates/>
    </toc>
  </xsl:template>

  <xsl:template match="*|@*|text()">
    <xsl:apply-templates/>
  </xsl:template>

</xsl:stylesheet>
