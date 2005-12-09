<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="text"/>
	<xsl:template match="testsuites">
junit.errorCount=<xsl:value-of select="sum(testsuite[*]/@errors)"/>
junit.failureCount=<xsl:value-of select="sum(testsuite[*]/@failures)"/>
	</xsl:template>
</xsl:stylesheet>