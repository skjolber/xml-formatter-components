<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0"
    xmlns:e="http://xmlns.greenbird.com/schema/logger"
>
    <xsl:output method="xml" encoding="UTF-8" indent="no" />
	<xsl:param name="statusCode"/>

    <xsl:template match="/*">
    	<xsl:element name="e:performLogMessageResponse">
    		<xsl:element name="e:status"><xsl:value-of select="$statusCode"/></xsl:element>
    	</xsl:element>
    </xsl:template>
</xsl:stylesheet>
