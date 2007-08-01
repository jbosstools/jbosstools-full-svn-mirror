<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
   <xsl:template match="configurations">
      <project name="Packaging Generator" default="_packaging_generation_">
         <target name="_packaging_generation_">
            <xsl:attribute name="depends"><xsl:for-each select="archive"><xsl:value-of select="generate-id()"/><xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if></xsl:for-each></xsl:attribute>
         </target>
         <xsl:for-each select="archive">
            <xsl:apply-templates select="."/>
         </xsl:for-each>
      </project>
   </xsl:template>
   <xsl:template match="archive">
      <target>
         <xsl:attribute name="name"><xsl:value-of select="generate-id()"/></xsl:attribute>
         <xsl:attribute name="description"><xsl:value-of select="@name"/></xsl:attribute>
         <xsl:if test="@used = 'true'">
            <xsl:if test="@destination != ''">
               <mkdir>
                  <xsl:attribute name="dir"><xsl:value-of select="@destination"/></xsl:attribute>
               </mkdir>
            </xsl:if>
            <xsl:choose>
               <xsl:when test="@exploded = 'true'">
                  <mkdir>
                     <xsl:attribute name="dir"><xsl:if test="@destination != ''"><xsl:value-of select="@destination"/><xsl:text>/</xsl:text></xsl:if><xsl:value-of select="@name"/></xsl:attribute>
                  </mkdir>
                  <xsl:for-each select="file">
                     <xsl:apply-templates select="." mode="exploded"/>
                  </xsl:for-each>
                  <xsl:for-each select="folder">
                     <xsl:apply-templates select="." mode="exploded"/>
                  </xsl:for-each>
               </xsl:when>
               <xsl:otherwise>
                  <jar>
                     <xsl:attribute name="destfile"><xsl:if test="@destination != ''"><xsl:value-of select="@destination"/><xsl:text>/</xsl:text></xsl:if><xsl:value-of select="@name"/></xsl:attribute>
                     <!-- Special case for the Manifest -->
                     <xsl:for-each select="file">
                        <xsl:if test="(@used = 'true') and (@name = 'MANIFEST.MF') and (@prefix = 'META-INF')">
                           <xsl:attribute name="manifest"><xsl:value-of select="@location"/></xsl:attribute>
                        </xsl:if>
                     </xsl:for-each>
                     <xsl:for-each select="file">
                        <xsl:apply-templates select="."/>
                     </xsl:for-each>
                     <xsl:for-each select="folder">
                        <xsl:apply-templates select="."/>
                     </xsl:for-each>
                  </jar>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:if>
      </target>
   </xsl:template>
   <xsl:template match="file">
      <xsl:if test="@used = 'true'">
         <!-- Skip the Manifest -->
         <xsl:if test="not((@name = 'MANIFEST.MF') and (@prefix = 'META-INF'))">
            <zipfileset>
               <xsl:choose>
                  <xsl:when test="(string-length(@location) = string-length(@name))">
                     <xsl:attribute name="dir">.</xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:attribute name="dir"><xsl:value-of select="substring(@location, 0, string-length(@location) - string-length(@name))"/></xsl:attribute>
                  </xsl:otherwise>
               </xsl:choose>
               <xsl:if test="@prefix != ''">
                  <xsl:attribute name="prefix"><xsl:value-of select="@prefix"/></xsl:attribute>
               </xsl:if>
               <include>
                  <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
               </include>
            </zipfileset>
         </xsl:if>
      </xsl:if>
   </xsl:template>
   <xsl:template match="file" mode="exploded">
      <xsl:if test="@used = 'true'">
         <copy failonerror="false">
            <xsl:attribute name="file"><xsl:value-of select="@location"/></xsl:attribute>
            <xsl:attribute name="todir"><xsl:if test="../@destination != ''"><xsl:value-of select="../@destination"/><xsl:text>/</xsl:text></xsl:if><xsl:value-of select="../@name"/><xsl:if test="@prefix != ''"><xsl:text>/</xsl:text><xsl:value-of select="@prefix"/></xsl:if></xsl:attribute>
         </copy>
      </xsl:if>
   </xsl:template>
   <xsl:template match="folder">
      <xsl:if test="@used = 'true'">
         <zipfileset>
            <xsl:attribute name="dir"><xsl:value-of select="@location"/></xsl:attribute>
            <xsl:if test="@prefix != ''">
               <xsl:attribute name="prefix"><xsl:value-of select="@prefix"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@includes != ''">
               <xsl:attribute name="includes"><xsl:value-of select="@includes"/></xsl:attribute>
            </xsl:if>
            <xsl:if test="@excludes != ''">
               <xsl:attribute name="excludes"><xsl:value-of select="@excludes"/></xsl:attribute>
            </xsl:if>
         </zipfileset>
      </xsl:if>
   </xsl:template>
   <xsl:template match="folder" mode="exploded">
      <xsl:if test="@used = 'true'">
         <copy>
            <xsl:attribute name="todir"><xsl:if test="../@destination != ''"><xsl:value-of select="../@destination"/><xsl:text>/</xsl:text></xsl:if><xsl:value-of select="../@name"/><xsl:if test="@prefix != ''"><xsl:text>/</xsl:text><xsl:value-of select="@prefix"/></xsl:if></xsl:attribute>
            <fileset>
               <xsl:attribute name="dir"><xsl:value-of select="@location"/></xsl:attribute>
               <xsl:if test="@includes != ''">
                  <xsl:attribute name="includes"><xsl:value-of select="@includes"/></xsl:attribute>
               </xsl:if>
               <xsl:if test="@excludes != ''">
                  <xsl:attribute name="excludes"><xsl:value-of select="@excludes"/></xsl:attribute>
               </xsl:if>
            </fileset>
         </copy>
      </xsl:if>
   </xsl:template>
</xsl:stylesheet>
