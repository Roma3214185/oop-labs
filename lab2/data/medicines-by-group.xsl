<?xml version="1.0" encoding="UTF-8"?>
<!--
    Splits the flat list of drugs into sub-lists by drug group.
    Uses Muenchian grouping (XSLT 1.0).
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="drugs-by-group" match="drug" use="group"/>

    <xsl:template match="/Medicine">
        <MedicineByGroup>
            <xsl:for-each select="drug[generate-id()
                    = generate-id(key('drugs-by-group', group)[1])]">
                <xsl:sort select="group"/>
                <group name="{group}"
                       count="{count(key('drugs-by-group', group))}">
                    <xsl:apply-templates
                            select="key('drugs-by-group', group)">
                        <xsl:sort select="name"/>
                    </xsl:apply-templates>
                </group>
            </xsl:for-each>
        </MedicineByGroup>
    </xsl:template>

    <!-- Drug without the group element: it is now the parent's name. -->
    <xsl:template match="drug">
        <drug>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="*[not(self::group)]" mode="copy"/>
        </drug>
    </xsl:template>

    <!-- Copies an element without inherited namespace declarations. -->
    <xsl:template match="*" mode="copy">
        <xsl:element name="{local-name()}">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="node()" mode="copy"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="text()" mode="copy">
        <xsl:value-of select="."/>
    </xsl:template>
</xsl:stylesheet>
