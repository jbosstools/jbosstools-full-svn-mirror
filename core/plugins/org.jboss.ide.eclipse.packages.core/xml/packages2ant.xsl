<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<xsl:template match="packages">
		<project default="generatePackages">
			<xsl:for-each select="//property[contains(@name, 'project-root')]">
				<property name="{@name}" value="{@value}"/>
			</xsl:for-each>
			<target name="generatePackages">
				<xsl:attribute name="depends">
					<xsl:for-each select="package">
						<xsl:value-of select="@name"/>
						<xsl:if test="position() != last()">,</xsl:if>
					</xsl:for-each>
				</xsl:attribute>
			</target>
			<xsl:apply-templates select="package"/>
		</project>
	</xsl:template>

	<xsl:template match="package">
		<xsl:param name="prefix" select="''"/>
		
		<xsl:variable name="id" select="generate-id()"/>
		<xsl:variable name="target-name" select="concat($prefix, @name)"/>
		<xsl:variable name="sub-package-prefix" select="concat($prefix, @name, '/')"/>
		<xsl:variable name="filename"><xsl:if test="count(@todir) &gt; 0 and @todir != ''"><xsl:value-of select="@todir"/>/</xsl:if><xsl:value-of select="@name"/></xsl:variable>
		
		<target>
			<xsl:attribute name="name"><xsl:value-of select="$target-name"/></xsl:attribute>
			<xsl:attribute name="depends">
				<xsl:for-each select="descendant::package[generate-id(ancestor::package[1]) = $id]">
					<xsl:value-of select="concat($sub-package-prefix, @name)"/>
					<xsl:if test="position() != last()">,</xsl:if>				
				</xsl:for-each>
			</xsl:attribute>
			
			<jar>
				<xsl:attribute name="destfile"><xsl:value-of select="$filename"/></xsl:attribute>
				<xsl:if test="count(@manifest) &gt; 0 and @manifest != ''">
					<xsl:attribute name="manifest"><xsl:value-of select="@manifest"/></xsl:attribute>
				</xsl:if>
				<xsl:apply-templates select="descendant::fileset[generate-id(ancestor::package[1]) = $id]"/>
				<xsl:for-each select="descendant::package[generate-id(ancestor::package[1]) = $id]">
					<zipfileset dir="." file="{@name}">
						<xsl:if test="count(parent::folder) &gt; 0"><xsl:attribute name="prefix"><xsl:apply-templates select="parent::folder"/></xsl:attribute></xsl:if>
					</zipfileset>
				</xsl:for-each>
			</jar>
			<xsl:for-each select="descendant::package[generate-id(ancestor::package[1]) = $id]">
				<delete>
					<xsl:attribute name="file">
						<xsl:if test="count(parent::folder) &gt; 0"><xsl:attribute name="prefix"><xsl:apply-templates select="parent::folder"/></xsl:attribute></xsl:if>
						<xsl:value-of select="@name"/>
					</xsl:attribute>
				</delete>
			</xsl:for-each>
		</target>
		
		<xsl:apply-templates select="descendant::package[generate-id(ancestor::package[1]) = $id]">
			<xsl:with-param name="prefix" select="$sub-package-prefix"/>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="folder">
		<xsl:if test="count(parent::folder) &gt; 0"><xsl:apply-templates select="parent::folder"/>/</xsl:if><xsl:value-of select="@name"/>
	</xsl:template>

	<xsl:template name="get-filename">
		<xsl:param name="path"/>
		<xsl:variable name="newpath" select="substring-after($path, '/')"/>
		<xsl:choose>
			<xsl:when test="$newpath = ''"><xsl:value-of select="$path"/></xsl:when>
			<xsl:otherwise><xsl:call-template name="get-filename"><xsl:with-param name="path" select="$newpath"/></xsl:call-template></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="fileset">
		<xsl:variable name="project-var">
			<xsl:choose>
				<xsl:when test="count(@project) = 0 or @project = ''">
					${project-root}
				</xsl:when>
				<xsl:otherwise>
					${project-root-<xsl:value-of select="@project"/>}
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="project-sub-dir">
			<xsl:choose>
				<xsl:when test="count(@dir) &gt; 0 and @dir != ''">
					<xsl:value-of select="concat(normalize-space($project-var), '/', @dir)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="normalize-space($project-var)"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<zipfileset>
			<xsl:choose>
				<xsl:when test="count(@file) &gt; 0 and @file != ''">
					<xsl:variable name="filename">
						<xsl:call-template name="get-filename">
							<xsl:with-param name="path" select="@file"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="count(@tofile) &gt; 0 and @tofile != ''">
							<xsl:variable name="fullpath">
								<xsl:if test="count(parent::folder) &gt; 0"><xsl:apply-templates select="parent::folder"/>/</xsl:if>
								<xsl:value-of select="@tofile"/>
							</xsl:variable>
							<xsl:attribute name="fullpath" ><xsl:value-of select="normalize-space($fullpath)"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="count(parent::folder) &gt; 0"><xsl:attribute name="prefix"><xsl:apply-templates select="parent::folder"/></xsl:attribute></xsl:if>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:attribute name="file"><xsl:value-of select="normalize-space($project-sub-dir)"/>/<xsl:value-of select="@file"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="dir"><xsl:value-of select="normalize-space($project-sub-dir)"/></xsl:attribute>
					<xsl:if test="count(parent::folder) &gt; 0">
						<xsl:variable name="prefix"><xsl:apply-templates select="parent::folder"/></xsl:variable>
						<xsl:attribute name="prefix"><xsl:value-of select="normalize-space($prefix)"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="count(@includes) &gt; 0 and @includes != ''">
						<xsl:attribute name="includes"><xsl:value-of select="@includes"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="count(@excludes) &gt; 0 and @excludes != ''">
						<xsl:attribute name="excludes"><xsl:value-of select="@excludes"/></xsl:attribute>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</zipfileset>
	</xsl:template>

</xsl:stylesheet>
