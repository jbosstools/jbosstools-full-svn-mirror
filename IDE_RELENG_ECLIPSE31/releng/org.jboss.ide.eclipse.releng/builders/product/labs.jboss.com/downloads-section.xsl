<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	
	<xsl:param name="build-type"/>
	
	<xsl:template match="builds">
		<downloads>
			<files>
				<xsl:apply-templates select="*[name() = concat($build-type, '-builds')]/build">
					<xsl:sort select="@name" order="descending"/>
				</xsl:apply-templates>
			</files>
		</downloads>
	</xsl:template>
	
	<xsl:template match="build">
		<xsl:apply-templates select="artifacts/artifact"/>
	</xsl:template>
	
	<xsl:template match="artifact">
		<xsl:variable name="build-name" select="../../@name"/>
		
		<file>
			<id><xsl:value-of select="url"/></id>
			<name><xsl:value-of select="$build-name"/>
				<xsl:call-template name="get-name">
					<xsl:with-param name="id" select="@id"/>
				</xsl:call-template>
			</name>
			<size>
				<xsl:call-template name="get-size">
					<xsl:with-param name="size" select="size"/>
				</xsl:call-template>
			</size>
			<description>
				<xsl:call-template name="get-description">
					<xsl:with-param name="id" select="@id"/>
					<xsl:with-param name="build-name" select="$build-name"/>
				</xsl:call-template>
			</description>
			<md5><xsl:value-of select="md5"/></md5>
		</file>
	</xsl:template>
	
	<xsl:template name="get-name">
		<xsl:param name="id"/>
		<xsl:choose>
			<xsl:when test="$id = 'win32-bundle'">
				Windows Bundle
			</xsl:when>
			<xsl:when test="$id = 'linux-gtk2-bundle'">
				Linux/GTK2 Bundle
			</xsl:when>
			<xsl:when test="$id = 'all'">
				All Plugins
			</xsl:when>
			<xsl:when test="$id ='core'">
				JBoss Eclipse IDE Core Plugins
			</xsl:when>
			<xsl:when test="$id = 'hibernate-tools'">
				Hibernate Tools
			</xsl:when>
			<xsl:when test="$id = 'jbpm'">
				jBPM Designer
			</xsl:when>
			<xsl:when test="$id = 'aop'">
				JBossAOP Developer
			</xsl:when>
			<xsl:when test="$id = 'ejb3'">
				EJB3 Tools
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get-size">
		<xsl:param name="size"/>
		<xsl:value-of select="concat(format-number($size div 1024 div 1024, '#.#'), 'MB')"/>
	</xsl:template>
	
	<xsl:template name="get-description">
		<xsl:param name="id"/>
		<xsl:param name="build-name"/>
		
		<xsl:choose>
			<xsl:when test="$id = 'win32-bundle'">
				<xsl:call-template name="get-win32-bundle-description">
					<xsl:with-param name="build-name" select="$build-name"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$id = 'linux-gtk2-bundle'">
				<xsl:call-template name="get-linux-gtk2-bundle-description">
					<xsl:with-param name="build-name" select="$build-name"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$id = 'all'">
				<xsl:call-template name="get-all-description">
					<xsl:with-param name="build-name" select="$build-name"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$id ='core'">
				<xsl:call-template name="get-core-description">
					<xsl:with-param name="build-name" select="$build-name"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$id = 'hibernate-tools'">
				<xsl:call-template name="get-hibernate-tools-description">
					<xsl:with-param name="build-name" select="$build-name"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$id = 'jbpm'">
				<xsl:call-template name="get-jbpm-description">
					<xsl:with-param name="build-name" select="$build-name"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$id = 'aop'">
				<xsl:call-template name="get-aop-description">
					<xsl:with-param name="build-name" select="$build-name"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$id = 'ejb3'">
				<xsl:call-template name="get-ejb3-description">
					<xsl:with-param name="build-name" select="$build-name"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get-win32-bundle-description">
		<xsl:param name="build-name"/>
		<xsl:variable name="eclipse-target" select="//build[@name=$build-name]/eclipse-target"/>
		<xsl:variable name="webtools-target" select="//build[@name=$build-name]/webtools-target"/>
		
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
	
	<xsl:template name="get-linux-gtk2-bundle-description">
		<xsl:param name="build-name"/>
		<xsl:variable name="eclipse-target" select="//build[@name=$build-name]/eclipse-target"/>
		<xsl:variable name="webtools-target" select="//build[@name=$build-name]/webtools-target"/>
		
		<xsl:choose>
			<xsl:when test="contains($build-type, '-release')">
				This is the <xsl:value-of select="$build-name"/> release for Linux/GTK2 including Eclipse
				<xsl:value-of select="$eclipse-target"/>,
				and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:when test="$build-type = 'nightly' or $build-type = 'integration'">
				This is the <xsl:value-of select="$build-name"/> build for Linux/GTK2 including Eclipse
				<xsl:value-of select="$eclipse-target"/>,
				and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get-all-description">
		<xsl:param name="build-name"/>
		<xsl:variable name="eclipse-target" select="//build[@name=$build-name]/eclipse-target"/>
		<xsl:variable name="webtools-target" select="//build[@name=$build-name]/webtools-target"/>
		
		<xsl:choose>
			<xsl:when test="contains($build-type, '-release')">
				This zip contains all of the plugins and dependencies of the <xsl:value-of select="$build-name"/> release.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:when test="$build-type = 'nightly' or $build-type = 'integration'">
				This zip contains all of the plugins and dependencies of the <xsl:value-of select="$build-name"/> build.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get-core-description">
		<xsl:param name="build-name"/>
		<xsl:variable name="eclipse-target" select="//build[@name=$build-name]/eclipse-target"/>
		<xsl:variable name="webtools-target" select="//build[@name=$build-name]/webtools-target"/>
		
		<xsl:choose>
			<xsl:when test="contains($build-type, '-release')">
				This zip contains all of the JBossIDE Core (XDoclet, Packaging, JBossAS) plugins of the <xsl:value-of select="$build-name"/> release.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:when test="$build-type = 'nightly' or $build-type = 'integration'">
				This zip contains all of the JBossIDE Core (XDoclet, Packaging, JBossAS) plugins of the <xsl:value-of select="$build-name"/> build.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get-hibernate-tools-description">
		<xsl:param name="build-name"/>
		<xsl:variable name="eclipse-target" select="//build[@name=$build-name]/eclipse-target"/>
		<xsl:variable name="webtools-target" select="//build[@name=$build-name]/webtools-target"/>
		
		<xsl:choose>
			<xsl:when test="contains($build-type, '-release')">
				This zip contains the Hibernate Tools <xsl:value-of select="$build-name"/> release.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:when test="$build-type = 'nightly' or $build-type = 'integration'">
				This zip contains the Hibernate Tools <xsl:value-of select="$build-name"/> build.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get-jbpm-description">
		<xsl:param name="build-name"/>
		<xsl:variable name="eclipse-target" select="//build[@name=$build-name]/eclipse-target"/>
		<xsl:variable name="webtools-target" select="//build[@name=$build-name]/webtools-target"/>
		
		<xsl:choose>
			<xsl:when test="contains($build-type, '-release')">
				This zip contains the jBPM Designer <xsl:value-of select="$build-name"/> release.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:when test="$build-type = 'nightly' or $build-type = 'integration'">
				This zip contains the jBPM Designer <xsl:value-of select="$build-name"/> build.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get-aop-description">
		<xsl:param name="build-name"/>
		<xsl:variable name="eclipse-target" select="//build[@name=$build-name]/eclipse-target"/>
		<xsl:variable name="webtools-target" select="//build[@name=$build-name]/webtools-target"/>
		
		<xsl:choose>
			<xsl:when test="contains($build-type, '-release')">
				This zip contains the JBossAOP Developer <xsl:value-of select="$build-name"/> release.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:when test="$build-type = 'nightly' or $build-type = 'integration'">
				This zip contains the JBossAOP Developer <xsl:value-of select="$build-name"/> build.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get-ejb3-description">
		<xsl:param name="build-name"/>
		<xsl:variable name="eclipse-target" select="//build[@name=$build-name]/eclipse-target"/>
		<xsl:variable name="webtools-target" select="//build[@name=$build-name]/webtools-target"/>
		
		<xsl:choose>
			<xsl:when test="contains($build-type, '-release')">
				This zip contains the EJB3 Tools <xsl:value-of select="$build-name"/> release.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:when test="$build-type = 'nightly' or $build-type = 'integration'">
				This zip contains the EJB3 Tools <xsl:value-of select="$build-name"/> build.
				Built against Eclipse <xsl:value-of select="$eclipse-target"/> and Webtools <xsl:value-of select="$webtools-target"/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>