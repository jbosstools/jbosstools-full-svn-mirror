<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0">
	<!-- TODO: ? make p2.mirror step a single operation w/ a long list of IUs 
		pulled from a list of sites, rather than from each site 
	-->
	<xsl:output method="xml" indent="yes" />
	<xsl:decimal-format decimal-separator="."
		grouping-separator="," />

	<xsl:param name="verbose" select="'false'" />
	<xsl:param name="destination" select="'file:${eclipse.install.dir}'" />

	<xsl:variable name="platformFilter"
		select="concat(/target/environment/os/text(), ',', /target/environment/ws/text(), ',', /target/environment/arch/text())" />

	<xsl:template match="target">
		<project name="Install target platform" default="install.target.platform">
			<!-- use followStrict="true" to prevent downloading all requirements not 
				included in the target platform or followStrict="false" to fetch everything -->
			<target name="init" unless="eclipse.install.dir">
				<fail>Must set -Declipse.install.dir=/path/to/install/</fail>
			</target>
			<target name="install.target.platform" depends="init"
				description="Install from target platform definition" if="eclipse.install.dir">
				<echo>Install features/plugins into ${eclipse.install.dir}</echo>
				<xsl:apply-templates />
			</target>
		</project>
	</xsl:template>

	<xsl:template match="location">
		<xsl:variable name="locationUrl" select="./repository/@location" />
		<p2.mirror destination="{$destination}" verbose="{$verbose}">
			<slicingOptions includeFeatures="true" followStrict="false" />
			<source>
				<repository location="{$locationUrl}" />
			</source>
			<xsl:apply-templates />
		</p2.mirror>
	</xsl:template>

	<xsl:template match="unit">
		<iu id="{@id}" version="{@version}" />
	</xsl:template>

	<!-- ignore anything else -->
	<xsl:template match="environment|targetJRE|launcherArgs|includeBundles" />

</xsl:stylesheet>