<xsl:transform version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan">
	<xsl:output method="html" indent="yes" xalan:indent-amount="4" />
	<xsl:template match="/projects">

		<xsl:variable name="baseurl">
			<xsl:value-of select="@baseurl" />
		</xsl:variable>

		<table cellspacing="1" cellpadding="0" border="0" align="left"
			style="width: 100%;" class="simpletablestyle">
			<thead>
				<tr class="header">
					<th>Download</th>
					<th>Size</th>
					<th>Description</th>
				</tr>
			</thead>
			<tbody>
				<xsl:for-each select="project">
					<xsl:variable name="rowClass">
						<xsl:if test="position()  mod 2 = 1">
							oddRow
						</xsl:if>
						<xsl:if test="position()  mod 2 = 0">
							evenRow
						</xsl:if>
					</xsl:variable>
					<xsl:variable name="rowColor">
						<xsl:if test="contains(@name,'All')">
							#CCEECC
						</xsl:if>
						<xsl:if test="contains(@name,'Sources')">
							#EECCCC
						</xsl:if>
					</xsl:variable>

					<tr class="{$rowClass}" bgcolor="{$rowColor}">
						<td class="rowLine">
							<xsl:for-each select=".">
								<b>
									<a href="{$baseurl}/{download/@url}"
										onclick="javascript:pageTracker._trackPageview('/downloads/{@url}');turnOnModal(this); return false;">
										<xsl:value-of select="@name" />
									</a>
								</b>
								<br />
							</xsl:for-each>
						</td>
						<td class="rowLine">
							<xsl:for-each select=".">
								<xsl:value-of select="download/@size" />
							</xsl:for-each>
						</td>
						<td class="rowLine">
							<xsl:value-of select="@name" />
							-
							<xsl:value-of select="description" />
							<xsl:if test="string(a) != ''">
								<xsl:copy-of select="a" />
							</xsl:if>
						</td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
</xsl:transform>