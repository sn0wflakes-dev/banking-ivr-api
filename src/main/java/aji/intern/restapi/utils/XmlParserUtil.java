package aji.intern.restapi.utils;

import aji.intern.restapi.dto.SoapFaultResponse;
import jakarta.xml.soap.Detail;
import jakarta.xml.soap.DetailEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.Iterator;

public class XmlParserUtil {

    private static final Logger log = LogManager.getLogger(XmlParserUtil.class);

    public static SoapFaultResponse xmlFaultParser(Detail detail) {
        try {
            Iterator<?> entries = detail.getDetailEntries();

            if (!entries.hasNext()) {
                return defaultSoapFault();
            }

            DetailEntry detailEntry = (DetailEntry) entries.next();
            XPath xPath = XPathFactory.newInstance().newXPath();

            String responseCode = xPath.evaluate("//*[local-name()='responseCode']", detailEntry);
            String messageId = xPath.evaluate("//*[local-name()='messageId']", detailEntry);
            String errorOrigin = xPath.evaluate("//*[local-name()='errorOrigin']", detailEntry);
            String responseMessage = xPath.evaluate("//*[local-name()='responseMessage']", detailEntry);

            return SoapFaultResponse.builder()
                    .responseCode(responseCode)
                    .messageId(messageId)
                    .errorOrigin(errorOrigin)
                    .responseMessage(responseMessage)
                    .build();

        } catch (Exception e) {
            log.error("Failed to parse xml for detail fault string, detail error : {}", e.getMessage());
            return defaultSoapFault();
        }
    }

    private static SoapFaultResponse defaultSoapFault() {
        return SoapFaultResponse.builder()
                .responseCode("N/A")
                .messageId("N/A")
                .errorOrigin("N/A")
                .responseMessage("N/A")
                .build();
    }
}
