<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0"
    xmlns:log="http://xmlns.greenbird.com/schema/logger"
>
    <xsl:output method="xml" encoding="UTF-8" indent="no" />

    <xsl:template match="/log:performLogMessageRequest">
    	<xsl:element name="log:performLogMessageResponse">
    		<xsl:apply-templates select="@*|node()"/>
    	</xsl:element>
    </xsl:template>
    
    <xsl:template match="@* | text() | processing-instruction() | comment()">
        <xsl:copy/>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy copy-namespaces="no">
            <xsl:for-each-group group-by="local-name()" select="descendant-or-self::*/namespace::*">
                <xsl:copy-of select="."/>
            </xsl:for-each-group>
            <xsl:apply-templates select="@* , node()"/>
        </xsl:copy>
    </xsl:template>    
<!-- 
    <xsl:template match="@*|node()" name="copy">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
      -->   
</xsl:stylesheet>
