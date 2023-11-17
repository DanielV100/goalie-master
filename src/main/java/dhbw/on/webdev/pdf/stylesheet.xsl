<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template match="/ObjectNode">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="goalie-master">
                    <fo:region-body margin="2cm" margin-top="3cm"/>
                    <fo:region-before extent="2cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="goalie-master">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block padding-top="0.5cm" padding-start="1cm" font-size="12pt" text-align="left">
                        GoalieMaster
                    </fo:block>
                    <fo:block padding-top="0.5cm" padding-start="1cm" font-size="16pt" text-align="center" font-weight="bold">
                        Trainingseinheit
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="14pt" font-weight="bold" margin-top="5pt" margin-bottom="5pt">
                        Metadaten
                    </fo:block>
                    <fo:block font-size="14pt" font-family="sans-serif">
                        Trainingseinheit-ID: <xsl:value-of select="id"/>
                    </fo:block>
                    <fo:block font-size="12pt" wrap-option="wrap">
                        Titel: <xsl:value-of select="title"/>
                    </fo:block>
                    <fo:block font-size="12pt">
                        Datum:
                        <xsl:for-each select="date">
                            <xsl:value-of select="."/>
                            <xsl:if test="position() != last()">.</xsl:if>
                        </xsl:for-each>
                    </fo:block>

                    <fo:block>
                        <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="2pt"/>
                    </fo:block>

                    <fo:block font-size="14pt" font-weight="bold" margin-top="5pt" margin-bottom="5pt">
                        Torhüter im Überblick
                    </fo:block>
                    <fo:table width="100%" border="solid 1pt" table-layout="fixed">
                        <fo:table-column column-width="20%"/>
                        <fo:table-column column-width="40%"/>
                        <fo:table-column column-width="40%"/>
                        <fo:table-header>
                            <fo:table-row>
                                <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                    <fo:block font-weight="bold">Id</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                    <fo:block font-weight="bold">Vorname</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                    <fo:block font-weight="bold">Nachname</fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <!-- Daten für jeden Torhüter -->
                            <xsl:for-each select="goalkeepers">
                                <fo:table-row>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="id"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="firstname"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="lastname"/></fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>


                    <fo:block font-size="14pt" font-weight="bold" margin-top="5pt" margin-bottom="5pt">
                        Übungen im Überblick
                    </fo:block>
                    <fo:table width="100%" border="solid 1pt" table-layout="fixed">
                        <fo:table-column column-width="20%"/>
                        <fo:table-column column-width="40%"/>
                        <fo:table-column column-width="20%"/>
                        <fo:table-column column-width="20%"/>
                        <fo:table-header>
                            <fo:table-row>
                                <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                    <fo:block font-weight="bold">Id</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                    <fo:block font-weight="bold">Titel</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                    <fo:block font-weight="bold">Intensität</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                    <fo:block font-weight="bold">Dauer</fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <!-- Daten für jeden Torhüter -->
                            <xsl:for-each select="exercises">
                                <fo:table-row>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="id"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="title"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="intensity"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="duration"/></fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>
                    <fo:block font-size="14pt" font-weight="bold" margin-top="5pt" margin-bottom="5pt">
                        Notizen
                    </fo:block>
                    <fo:block font-size="12pt" wrap-option="wrap">
                        <xsl:value-of select="notes"/>
                    </fo:block>
                    <fo:block>
                        <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="2pt"/>
                    </fo:block>

                    <xsl:for-each select="exercises">
                        <fo:block font-size="14pt" font-weight="bold" margin-top="10pt">
                            <xsl:value-of select="concat(categoryGroup, ': ', title)"/>
                        </fo:block>

                        <!-- Basisinformationen-Tabelle -->
                        <fo:table width="100%" border="solid 1pt" table-layout="fixed">
                            <!-- Tabellenkopf mit Kategorien -->
                            <fo:table-header>
                                <fo:table-row>
                                    <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                        <fo:block font-weight="bold">Kategorie</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                        <fo:block font-weight="bold">Anz. TH</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                        <fo:block font-weight="bold">Dauer</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                        <fo:block font-weight="bold">Intensität</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="category"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="numberOfGoalkeepers"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="duration"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt">
                                        <fo:block><xsl:value-of select="intensity"/></fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                        <fo:table width="100%" border="solid 1pt" table-layout="fixed">
                            <fo:table-header>
                                <fo:table-row>
                                    <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                        <fo:block font-weight="bold">Anzahl</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                        <fo:block font-weight="bold">Material</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>
                            <fo:table-body>
                                <xsl:if test="materials">
                                <xsl:for-each select="materials">
                                    <xsl:variable name="currentPosition" select="position()" />
                                    <xsl:variable name="materialNumber" select="../numbersOfMaterial[$currentPosition]" />
                                    <fo:table-row>
                                        <fo:table-cell border="solid 1pt">
                                            <fo:block>
                                                <xsl:value-of select="$materialNumber"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border="solid 1pt">
                                            <fo:block>
                                                <xsl:value-of select="."/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                                </xsl:if>

                            </fo:table-body>
                        </fo:table>
                        <xsl:if test="descriptionSteps">
                            <fo:table width="100%" border="solid 1pt" table-layout="fixed">
                                <fo:table-column column-width="20%"/>
                                <fo:table-column column-width="80%"/>
                                <fo:table-header>
                                    <fo:table-row>
                                        <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                            <fo:block font-weight="bold">Nummer</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border="solid 1pt" background-color="#5fbf81">
                                            <fo:block font-weight="bold">Beschreibung</fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </fo:table-header>
                                <fo:table-body>
                                    <xsl:for-each select="descriptionSteps">
                                        <xsl:variable name="currentPosition" select="position()" />
                                        <fo:table-row>
                                            <fo:table-cell border="solid 1pt">
                                                <fo:block>
                                                    <xsl:value-of select="$currentPosition"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell border="solid 1pt">
                                                <fo:block>
                                                    <xsl:value-of select="."/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:for-each>
                                </fo:table-body>
                            </fo:table>
                        </xsl:if>
                        <fo:block>
                            <xsl:variable name="imageData" select="dataUrl"/>
                            <fo:external-graphic src="{$imageData}"/>
                        </fo:block>
                    </xsl:for-each>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>
