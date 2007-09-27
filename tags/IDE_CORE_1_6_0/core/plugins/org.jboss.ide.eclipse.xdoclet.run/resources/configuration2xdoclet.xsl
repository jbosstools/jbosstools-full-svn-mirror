<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:param name="project.jars" select="''"/>
   <xsl:param name="xdoclet.basedir" select="''"/>
   <xsl:param name="eclipse.home" select="''"/>
   <xsl:param name="jboss.net.version" select="''"/>
   <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
   <xsl:template match="configurations">
      <project name="XDoclet Generator" default="_xdoclet_generation_">
         <property file="xdoclet-build.properties"/>
         <property name="eclipse.home">
            <xsl:attribute name="value">
               <xsl:value-of select="$eclipse.home"/>
            </xsl:attribute>
         </property>
         <property name="xdoclet.basedir">
            <xsl:attribute name="value">
               <xsl:value-of select="$xdoclet.basedir"/>
            </xsl:attribute>
         </property>
         <property name="jboss.net.version">
            <xsl:attribute name="value">
               <xsl:value-of select="$jboss.net.version"/>
            </xsl:attribute>
         </property>
         <path id="project.classpath">
            <xsl:value-of select="$project.jars" disable-output-escaping="yes"/>
         </path>
         <path id="xdoclet.classpath">
            <path refid="project.classpath" />
            <fileset>
               <xsl:attribute name="dir">
                  <xsl:text>${xdoclet.basedir}</xsl:text>
               </xsl:attribute>
               <include name="*.jar"/>
               <exclude name="xdoclet-module-jboss-net-*.jar"/>
            </fileset>
            <fileset>
               <xsl:attribute name="dir">
                  <xsl:text>${xdoclet.basedir}</xsl:text>
               </xsl:attribute>
               <include>
                  <xsl:attribute name="name">
                     <xsl:text>xdoclet-module-jboss-net-${jboss.net.version}*.jar</xsl:text>
                  </xsl:attribute>
               </include>
            </fileset>
         </path>
         <target name="_xdoclet_generation_">
            <xsl:attribute name="depends"><xsl:for-each select="configuration"><xsl:value-of select="generate-id()"/><xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if></xsl:for-each></xsl:attribute>
         </target>
         <xsl:for-each select="configuration">
            <xsl:apply-templates select="."/>
         </xsl:for-each>
      </project>
   </xsl:template>
   <xsl:template match="configuration">
      <target>
         <xsl:attribute name="name"><xsl:value-of select="generate-id()"/></xsl:attribute>
         <xsl:attribute name="description"><xsl:value-of select="@name"/></xsl:attribute>
         <xsl:if test="@used = 'true'">
            <xsl:for-each select="task">
               <xsl:if test="@used = 'true'">
                  <xsl:apply-templates select="."/>
               </xsl:if>
            </xsl:for-each>
         </xsl:if>
      </target>
   </xsl:template>
   <xsl:template match="task">
      <taskdef name="xdoclet" classname="xdoclet.DocletTask" classpathref="xdoclet.classpath">
         <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
         <xsl:attribute name="classname"><xsl:value-of select="@className"/></xsl:attribute>
      </taskdef>
      <xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:for-each select="attribute">
         <xsl:if test="@used = 'true'">
            <xsl:text disable-output-escaping="yes"><![CDATA[ ]]></xsl:text>
            <xsl:value-of select="@name"/>
            <xsl:text disable-output-escaping="yes"><![CDATA[="]]></xsl:text>
            <xsl:value-of select="@value"/>
            <xsl:text disable-output-escaping="yes"><![CDATA[" ]]></xsl:text>
         </xsl:if>
      </xsl:for-each>
      <xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>
      <xsl:apply-templates/>
      <xsl:text disable-output-escaping="yes"><![CDATA[</]]></xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>
   </xsl:template>
   <xsl:template match="element">
      <xsl:if test="@used = 'true'">
         <xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>
         <xsl:value-of select="@name"/>
         <xsl:for-each select="attribute">
            <xsl:if test="@used = 'true'">
               <xsl:text disable-output-escaping="yes"><![CDATA[ ]]></xsl:text>
               <xsl:value-of select="@name"/>
               <xsl:text disable-output-escaping="yes"><![CDATA[="]]></xsl:text>
               <xsl:value-of select="@value"/>
               <xsl:text disable-output-escaping="yes"><![CDATA[" ]]></xsl:text>
            </xsl:if>
         </xsl:for-each>
         <xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>
         <xsl:apply-templates/>
         <xsl:text disable-output-escaping="yes"><![CDATA[</]]></xsl:text>
         <xsl:value-of select="@name"/>
         <xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>
      </xsl:if>
   </xsl:template>
</xsl:stylesheet>
