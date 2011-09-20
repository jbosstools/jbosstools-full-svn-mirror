<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan"
	xmlns:saxon="http://saxon.sf.net/" xmlns="http://www.w3.org/1999/xhtml"
	extension-element-prefixes="saxon">
	<xsl:output method="html" indent="yes" />
	<xsl:template match="/site">
		<br />
			<table cellspacing="2" cellpadding="0" border="0">
				<tr style="background-color:#DDDDDD">
					<th style="font-size:small">Feature</th>
					<th style="font-size:small">Version</th>
					<th style="font-size:small">
						Feature Categor(ies)
					</th>
				</tr>
				<!-- JBoss features -->
				<xsl:for-each select="feature[contains(@id,'jboss')]">
					<xsl:sort select="@id" />
					<xsl:variable name="rowCol">
						<xsl:if test="position() mod 2 = 1">
							#EEEEEE
						</xsl:if>
						<xsl:if test="position() mod 2 = 0">
							#FFFFFF
						</xsl:if>
					</xsl:variable>

					<tr style="background-color:{$rowCol}">
						<td class="rowLine">
							<a href="{@url}" style="font-size:x-small">
								<xsl:value-of select="@id" />
							</a>
						</td>
						<td>
							<span style="font-size:x-small">
								<xsl:value-of select="@version" />
							</span>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="category">
									<xsl:for-each select="category">
										<span style="font-size:x-small">
											|
											<xsl:value-of select="@name" />
										</span>
									</xsl:for-each>
								</xsl:when>
							</xsl:choose>
						</td>
					</tr>
				</xsl:for-each>
				<tr style="background-color:#DDDDDD">
					<th style="font-size:small">Feature</th>
					<th style="font-size:small">Version</th>
					<th style="font-size:small">
						Feature Categor(ies)
					</th>
				</tr>
				<!-- other features -->
				<xsl:for-each select="feature[not(contains(@id,'jboss'))]">
					<xsl:sort select="@id" />
					<xsl:variable name="rowCol">
						<xsl:if test="position() mod 2 = 1">
							#EEEEEE
						</xsl:if>
						<xsl:if test="position() mod 2 = 0">
							#FFFFFF
						</xsl:if>
					</xsl:variable>

					<tr style="background-color:{$rowCol}">
						<td class="rowLine">
							<a href="{@url}" style="font-size:x-small">
								<xsl:value-of select="@id" />
							</a>
						</td>
						<td>
							<span style="font-size:x-small">
								<xsl:value-of select="@version" />
							</span>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="category">
									<xsl:for-each select="category">
										<span style="font-size:x-small">
											|
											<xsl:value-of select="@name" />
										</span>
									</xsl:for-each>
								</xsl:when>
							</xsl:choose>
						</td>
					</tr>
				</xsl:for-each>
				<tr style="background-color:#DDDDDD">
					<th colspan="1" style="font-size:small">Metadata</th>
					<th colspan="1" style="font-size:small"></th>
					<th colspan="1" style="font-size:small"></th>
				</tr>
				<tr style="background-color:#EEEEEE">
					<td class="rowLine">
						<a href="site.xml" style="font-size:x-small">site.xml</a>
						::
						<a href="artifacts.jar" style="font-size:x-small">artifacts.jar</a>
						::
						<a href="content.jar" style="font-size:x-small">content.jar</a>
					</td>
					<td class="rowLine">
					</td>
					<td class="rowLine">
					</td>
				</tr>
			</table>
		<br />
	</xsl:template>
</xsl:stylesheet>
