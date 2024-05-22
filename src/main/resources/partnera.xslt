<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ota="http://www.opentravel.org/OTA/2003/05"
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:oas1="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
                xmlns:oas="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
                xmlns:add="http://www.w3.org/2005/08/addressing"
                xmlns:companion="com.roomex.assessment.transformation.PartnerAStyleSheetCompanion"
        exclude-result-prefixes="ota companion">

    <xsl:output method="xml" encoding="UTF-8" />
    <xsl:strip-space  elements="*"/>

    <xsl:param name="message_id" />
    <xsl:param name="nonce" />
    <xsl:param name="created" />

    <xsl:template match="/ota:OTA_HotelAvailRQ">
        <xsl:text>&#xa;</xsl:text>
        <soapenv:Envelope>
            <xsl:apply-templates />
        </soapenv:Envelope>
    </xsl:template>

    <xsl:template match="ota:Source">
        <soapenv:Header>
            <add:MessageID>
                <xsl:value-of select="$message_id"/>
            </add:MessageID>
            <add:Action>
                <xsl:value-of select="ota:RequestorOptions/ota:RequestorOption[@Name='action']/@Value"/>
            </add:Action>
            <add:To>
                <xsl:value-of select="concat('https://', ota:RequestorOptions/ota:RequestorOption[@Name='targetHost']/@Value, '/', ota:RequestorOptions/ota:RequestorOption[@Name='endpoint']/@Value)"/>
            </add:To>
            <oas:Security>
                <oas:UsernameToken oas1:Id="UsernameToken-1">
                    <oas:Username>
                        <xsl:value-of select="ota:RequestorID/@ID"/>
                    </oas:Username>
                    <oas:Nonce EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soapenv-message-security-1.0#Base64Binary">
                        <xsl:value-of select="$nonce"/>
                    </oas:Nonce>
                    <oas:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest">
                        <xsl:variable name="password" select="//ota:RequestorID/@MessagePassword" />
                        <xsl:value-of select="companion:password($nonce, $created, $password)" />
                    </oas:Password>
                    <oas1:Created>
                        <xsl:value-of select="$created"/>
                    </oas1:Created>
                </oas:UsernameToken>
            </oas:Security>
        </soapenv:Header>
    </xsl:template>

    <xsl:template match="ota:AvailRequestSegments">
        <soapenv:Body>
            <xsl:for-each select="ota:AvailRequestSegment">
                <availabilityRequest>
                    <xsl:attribute name="inputCurrency">
                        <xsl:choose>
                            <xsl:when test="/ota:OTA_HotelAvailRQ/@RequestedCurrency = 'GBP'">
                                <xsl:text>&#163;</xsl:text>
                            </xsl:when>
                            <xsl:when test="/ota:OTA_HotelAvailRQ/@RequestedCurrency = 'EUR'">
                                <xsl:text>&#8364;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <!-- If the list of possible currencies were any longer we would call out to a java method to resolve the currency symbol -->
                                <xsl:text>?</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    <xsl:attribute name="inputLanguageCode">
                        <xsl:variable name="language-code" select="/ota:OTA_HotelAvailRQ/@PrimaryLangID" />
                        <xsl:value-of select="companion:languageCode($language-code)" />
                    </xsl:attribute>
                    <globalInputParameters>
                        <xsl:variable name="hotel-code" select="ota:HotelSearchCriteria/ota:Criterion/ota:HotelRef/@HotelCode" />
                        <xsl:variable name="chain-code" select="substring($hotel-code,1,2)" />
                        <xsl:variable name="hotel-id" select="substring($hotel-code,4)" />
                        <hotelInputDetails>
                            <xsl:attribute name="chainCode">
                                <xsl:value-of select="$chain-code"/>
                            </xsl:attribute>
                            <xsl:attribute name="hotelId">
                                <xsl:value-of select="$hotel-id"/>
                            </xsl:attribute>
                            <xsl:attribute name="locationCode">
                                <xsl:value-of select="ota:HotelSearchCriteria/ota:Criterion/ota:HotelRef/@HotelCityCode"/>
                            </xsl:attribute>
                        </hotelInputDetails>
                        <hotelStayDuration>
                            <xsl:variable name="start" select="ota:StayDateRange/@Start" />
                            <xsl:variable name="end" select="ota:StayDateRange/@End" />
                            <xsl:attribute name="numberOfNights">
                                <xsl:value-of select="companion:numberOfNights($start, $end)" />
                            </xsl:attribute>
                            <xsl:attribute name="startDate">
                                <xsl:value-of select="substring($start,1,10)"/>
                            </xsl:attribute>
                        </hotelStayDuration>
                    </globalInputParameters>
                    <xsl:apply-templates select="ota:RoomStayCandidates" />
                </availabilityRequest>
            </xsl:for-each>
        </soapenv:Body>
    </xsl:template>

    <xsl:template match="ota:RoomStayCandidates">
        <roomInputParameters>
            <rooms>
                <xsl:variable name="unique-room-groups" select="count(ota:RoomStayCandidate/ota:GuestCounts/ota:GuestCount[@AgeQualifyingCode='10'][generate-id() = generate-id(key('groups', @Count)[1])])" />
                <xsl:attribute name="uniqueRoomGroups">
                    <xsl:value-of select="$unique-room-groups" />
                </xsl:attribute>
                <xsl:attribute name="numberOfChildren">
                    <xsl:value-of select="sum(ota:RoomStayCandidate/ota:GuestCounts/ota:GuestCount[@AgeQualifyingCode='8']/@Count)" />
                </xsl:attribute>
                <xsl:apply-templates select="ota:RoomStayCandidate/ota:GuestCounts/ota:GuestCount[@AgeQualifyingCode='10'][generate-id() = generate-id(key('groups', @Count)[1])]" />
            </rooms>
        </roomInputParameters>
    </xsl:template>

    <xsl:key name="groups" match="ota:RoomStayCandidate/ota:GuestCounts/ota:GuestCount[@AgeQualifyingCode='10']" use="@Count"/>

    <xsl:template match="ota:RoomStayCandidate/ota:GuestCounts/ota:GuestCount[@AgeQualifyingCode='10']">
        <room>
            <xsl:attribute name="numberOfRooms">
                <xsl:value-of select="count(key('groups', @Count))" />
            </xsl:attribute>
            <xsl:attribute name="numberOfAdults">
                <xsl:value-of select="@Count" />
            </xsl:attribute>
        </room>
    </xsl:template>

</xsl:stylesheet>
