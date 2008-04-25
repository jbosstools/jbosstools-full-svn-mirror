<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="builds">
		<downloads>
			<categories>
				<category>
					<id>stable-release</id>
					<name>Stable releases</name>
					<description>The latest stable release of JBoss Eclipse IDE</description>
				</category>
				<category>
					<id>development-release</id>
					<name>Development releases</name>
					<description>The latest development release of JBoss Eclipse IDE</description>
				</category>
				<category>
					<id>integration</id>
					<name>Integration builds</name>
					<description>The latest integration builds of JBoss Eclipse IDE</description>
				</category>
				<category>
					<id>nightly</id>
					<name>Nightly builds</name>
					<description>The latest nightly builds of JBoss Eclipse IDE</description>
				</category>
			</categories>
			<root-category>
				<name>JBoss Eclipse IDE Downloads</name>
				<description>Various download packages for the latest builds and releases of JBoss Eclipse IDE</description>
			</root-category>
			<files>
				<xsl:apply-templates select="stable-release-builds" />
				<xsl:apply-templates select="development-release-builds" />
				<xsl:apply-templates select="integration-builds" />
				<xsl:apply-templates select="nightly-builds" />
			</files>
		</downloads>
	</xsl:template>

	<xsl:template match="*[contains(name(), '-builds')]">
		<xsl:variable name="build-type" select="substring-before(name(), '-builds')" />
		<xsl:variable name="name" select="name()" />
		<xsl:variable name="num-builds" select="count(./build)" />
		<xsl:variable name="build-name" select="build[$num-builds]/@name"/>
		
		<file>
			<id><xsl:value-of select="build[$num-builds]/artifacts/artifact[@id = 'win32-bundle']/url"/></id>
			<name><xsl:value-of select="$build-name"/></name>
			<size>
				<xsl:call-template name="get-size">
					<xsl:with-param name="size" select="build[$num-builds]/artifacts/artifact[@id = 'win32-bundle']/size"/>
				</xsl:call-template>
			</size>
			<date><xsl:value-of select="build[$num-builds]/artifacts/artifact[@id = 'win32-bundle']/date"/></date>
			<md5><xsl:value-of select="build[$num-builds]/artifacts/artifact[@id = 'win32-bundle']/md5"/></md5>
			<description>
				<xsl:call-template name="get-bundle-description">
					<xsl:with-param name="build-name" select="$build-name"/>
					<xsl:with-param name="build-type" select="$build-type"/>
				</xsl:call-template>
			</description>
		</file>
	</xsl:template>
	
	<xsl:template name="get-size">
		<xsl:param name="size"/>
		<xsl:value-of select="concat(format-number($size div 1024 div 1024, '#.#'), 'MB')"/>
	</xsl:template>
	
	<xsl:template name="get-bundle-description">
		<xsl:param name="build-name"/>
		<xsl:param name="build-type"/>
		
		<xsl:variable name="eclipse-target" select="build[@name=$build-name]/eclipse-target"/>
		<xsl:variable name="webtools-target" select="build[@name=$build-name]/webtools-target"/>
		
		<xsl:choose>
			<xsl:when test="contains($build-type, '-release')">
				This is the <xsl:value-of select="$build-name"/> release for Windows including Eclipse
				<xsl:value-of select="$eclipse-target"/>,
				and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:when test="$build-type = 'nightly' or $build-type = 'integration'">
				This is the <xsl:value-of select="$build-name"/> build for Windows including Eclipse
				<xsl:value-of select="$eclipse-target"/>,
				and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>