<?xml version="1.0" encoding="UTF-8"?>
<!--
    Розбиває список препаратів на підсписки за групою (group):
    <MedicineByGroup>
        <ANTIBIOTIC> <drug/> <drug/> </ANTIBIOTIC>
        <VITAMIN> <drug/> </VITAMIN>
    </MedicineByGroup>
    Групування за Мюнхенським методом (XSLT 1.0).
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" standalone="yes"/>
    <xsl:strip-space elements="*"/>

    <!-- індекс: група -> усі препарати цієї групи -->
    <xsl:key name="drugsByGroup" match="drug" use="group"/>

    <xsl:template match="/Medicine">
        <MedicineByGroup>
            <!-- перший препарат кожної групи = перебір унікальних груп -->
            <xsl:for-each select="drug[generate-id() = generate-id(key('drugsByGroup', group)[1])]">
                <xsl:sort select="group"/>
                <xsl:element name="{group}">
                    <xsl:copy-of select="key('drugsByGroup', group)"/>
                </xsl:element>
            </xsl:for-each>
        </MedicineByGroup>
    </xsl:template>
</xsl:stylesheet>
