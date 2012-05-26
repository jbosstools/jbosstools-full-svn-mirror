<xsl:transform version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan">
	<xsl:output method="html" indent="yes" xalan:indent-amount="4" />
	<xsl:template match="/projects">

		<xsl:variable name="baseurl">
			<xsl:value-of select="@baseurl" />
		</xsl:variable>

		<html>
			<head>
				<title>JBossTools Build Results</title>
				<link rel="stylesheet" type="text/css" href="buildResults.css" />
			</head>
			<body>
				<div id="header-blue">
					<table width="100%" height="100%">
						<tr>
							<td>
								<span id="title-blue">JBossTools Build Results</span>
								<br />
								<span id="buildName-blue">
									${build-type} build
									${jbds-build-name}
									<span id="buildName-blue" style="font-size: x-small;">
										[Hudson Build
										<a id="buildName-blue" style="font-size: x-small;"
											href="http://hudson.qa.jboss.com/hudson/view/DevStudio/job/${hudson-job-name}/${hudson-build-number}/">#${hudson-build-number},
											${hudson-build-id}</a>
										]
									</span>
									<span id="buildName-blue" style="font-size: x-small; font-style: italic;">
										<a id="buildName-blue"
											style="font-size: x-small; font-style: italic; color: black;"
											href="http://hudson.jboss.org/hudson/view/JBossTools/job/${hudson-job-name}/">Public
											Hudson Mirror</a>
									</span>
								</span>
							</td>
							<td align="right">
								<span id="buildDate-blue">${date}</span>
								<br />
								<span id="buildExecTime-blue">${build-time}</span>
								<br />
								<a id="buildLog-blue"
									href="http://download.jboss.org/jbosstools/builds/staging/${hudson-job-name}/logs/">Build Logs</a>
							</td>
						</tr>
					</table>
				</div>

				<div id="downloads-blue">
					<table id="links-blue" width="100%">
						<tr>
							<td>
								<a class="blue" href="http://www.jboss.org/tools">JBossTools</a>
								|
								<a class="blue" href="http://www.redhat.com/jbds">JBDS</a>
								|
								<a class="blue" href="http://tools.hibernate.org">Hibernate Tools</a>
								|
								<a class="blue"
									href="http://www.jboss.com/index.html?module=bb&amp;op=viewforum&amp;f=201">User
									Forum</a>
								|
								<a class="blue"
									href="http://jboss.com/index.html?module=bb&amp;op=viewforum&amp;f=162">Development
									Forum</a>
								|
								<a class="blue"
									href="http://jira.jboss.com/jira/secure/BrowseProject.jspa?id=10020">JIRA
									/ Bugs</a>
								| #jbosstools on irc.freenode.net
							</td>
							<td align="right">
								<a class="blue" href="http://www.redhat.com">
									<img src="images/redhat.gif" border="0" />
								</a>
								<a class="blue" href="http://www.jboss.org">
									<img src="images/jboss.gif" border="0" />
								</a>
								<a class="blue" href="http://www.hibernate.org">
									<img src="images/hibernate.gif" border="0" />
								</a>
								<a class="blue" href="http://www.eclipse.org">
									<img src="images/eclipse.gif" border="0" />
								</a>
							</td>
						</tr>
					</table>

					<div id="downloadLinks">

						<table>
							<tr>
								<td colspan="2">
									First time here ? Read
									<a href="http://www.jboss.org/tools/download/installation.html">How
										to install JBoss Tools.</a>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<h1>p2 Repo / Update Site Zips:</h1>
								</td>
							</tr>

							<xsl:for-each select="project">
								<xsl:variable name="rowColor">
									<xsl:if test="position() mod 2 = 1">
										#CCCCEE
									</xsl:if>
									<xsl:if test="position() mod 2 = 0">
										#FFFFFF
									</xsl:if>
									<xsl:if test="contains(@name,'All')">
										#CCEECC
									</xsl:if>
									<xsl:if test="contains(@name,'Source')">
										#EECCCC
									</xsl:if>
								</xsl:variable>

									<tr bgcolor="{$rowColor}">
									<td>
										<img src="images/OK-small.png" />
									</td>
									<td class="downloadInfo">
										<xsl:for-each select=".">
											<b>
												<acronym title="{@name} :: {description}">
													<a class="allLink-blue" href="{$baseurl}/{download/@url}">
														<xsl:value-of select="concat(@name,'  ',@version)" />
													</a>
												</acronym>
												<br />
												<a class="md5" href="{$baseurl}/{download/@url}.MD5">MD5</a>
												| filesize:
												<xsl:value-of select="download/@size" />
											</b>
											<br />
										</xsl:for-each>
									</td>
								</tr>
							</xsl:for-each>
						</table>

					</div>

					<div id="rightFrame">
						<div id="buildDrivers-blue">
							<b>SVN Revisions</b>
							<br />
							See
							<a
								href="http://download.jboss.org/jbosstools/builds/staging/jbosstools-3.2_stable_branch.aggregate/logs/SVN_REVISION.txt">SVN_REVISION.txt</a>
						</div>
						<div id="buildDrivers-blue">
							<b>Target Platform</b>
							<table>
								<tr>
									<td rowspan="3">
										Eclipse Helios 3.6.1 +
										<br />
										Web Tools 3.2.2 +
										<br />
										TPTP + BIRT + DTP ...
									</td>
									<td>
										<a class="blue"
											href="http://download.jboss.org/jbosstools/updates/target-platform/latest/">Target Platform Update Site</a>
									</td>
								</tr>
								<tr>
									<td>
										<a class="blue"
											href="http://download.jboss.org/jbosstools/updates/target-platform/e361-wtp322.target.zip">Target Platform Update Zip</a>
									</td>
								</tr>
								<tr>
									<td>
										<a class="blue"
											href="http://anonsvn.jboss.org/repos/jbosstools/trunk/build/target-platform/unified.target">Target Platform Definition File</a>
									</td>
								</tr>
							</table>
						</div>
						<div id="unitTestResults-blue">
							<b>Unit Test Results</b>
							<br />

							See
							<a
								href="http://hudson.qa.jboss.com/hudson/view/DevStudio_Stable_Branch/job/jbosstools-3.2_stable_branch.tests/">jbosstools-3.2_stable_branch.tests</a>
							<!-- <table> <tr> <td> <img src="images/test.gif" /> Tests </td> <td 
								align="right">??? </td> </tr> <tr> <td> <img src="images/testerr.gif" /> 
								Errors </td> <td align="right">???</td> </tr> <tr> <td> <img src="images/testfail.gif" 
								/> Failures </td> <td align="right">???</td> </tr> <tr> <td colspan="2"> 
								<b id="testPassPercentageAmount">%</b> tests passed <div id="testPercentage"> 
								<div id="testPassPercentage"> </div> <div id="testFailPercentage"> </div> 
								</div> </td> </tr> </table> -->

							<script>
								<!-- var tests = 2228; var failures = 0; var errors = 0; var passPercent 
									= Math.floor(((tests-(failures+errors))/tests) * 100); var failPercent = 
									100 - passPercent; document.getElementById("testPassPercentage").style.width 
									= passPercent + '%'; document.getElementById("testFailPercentage").style.width 
									= failPercent + '%'; document.getElementById("testPassPercentageAmount").innerHTML 
									= passPercent + '%'; -->
							</script>

							<!-- <div align="right"> <a class="blue" style="align: right;" href="tests/test-results/org.jboss.ide.eclipse.tests.html">See 
								full unit test results</a> </div> <div align="right"> <a class="blue" style="align: 
								right;" href="tests/test-results/test-workspace.log.txt">See unit test workspace 
								log</a> </div> <div id="coverageResults-blue"> <b>Test Coverage Results</b> 
								<br /> <div align="right"> <a class="blue" style="align: right;" href="tests/test-results/emma-coverage/index.html">See 
								test coverage results</a> </div> <div align="right"> <a class="blue" style="align: 
								right;" href="tests/test-results/emma-coverage/coverage.es">See coverage 
								session file</a> </div> </div> -->
						</div>

						<div id="buildInfo">
							<p>
								<b>WARNING</b>
							</p>
							<p>This build has been deemed by the JBossTools team to be in
								good working condition.
								Please note that this build may be a
								developer release, and if so
								should be treated
								with slightly more
								understanding and care than a stable release.
								Please see the
								release
								notes on JIRA and/or Sourceforge for further
								information.
								(This
								release is much less likely to
								set your
								computer aflame.)</p>

							<p>Any suggestions/issues should be posted on the relevant
								product
								forums and should clearly indicate that you are using an
								integration
								build. Please take care in making sure that your
								issue hasn't
								already been addressed when posting on forums or
								opening new
								JIRA issues.</p>
						</div>

					</div>

				</div>

			</body>
		</html>



	</xsl:template>
</xsl:transform>