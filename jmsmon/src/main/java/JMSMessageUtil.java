import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.management.openmbean.CompositeData;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Cosmin on 8/8/2017.
 */
public class JMSMessageUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JMSMessageUtil.class);

    private static final String BODY = "Body";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String PROPERTY = "property";
    private static final String PROPERTIES = "Properties";
    private static final String JMS_TIMESTAMP = "JMSTimestamp";
    private static final String JMS_MESSAGE_ID = "JMSMessageID";
    private static final String HEADER = "Header";
    private static final String MESSAGE_XML_TEXT = "MessageXMLText";

    public static JmsMessage convertMessage(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }

        JmsMessage jmsMessage = new JmsMessage();

        String xmlMessage = String.valueOf(compositeData.get(MESSAGE_XML_TEXT));

        Document xmlDocument = parseXML(xmlMessage);
        if (xmlDocument == null) {
            return null;
        }

        Element root = xmlDocument.getDocumentElement();
        Element header = getChildElement(root, HEADER);
        Element jmsMessageId = getChildElement(header, JMS_MESSAGE_ID);
        String id = jmsMessageId.getTextContent();
        jmsMessage.setId(id);
        jmsMessage.getProperties().put(JMS_MESSAGE_ID, id);

        Element jmsTimestamp = getChildElement(header, JMS_TIMESTAMP);
        String timestampStr = jmsTimestamp.getTextContent();
        Date timestamp = new Date(Long.parseLong(timestampStr));
        jmsMessage.setTimestamp(timestamp);

        // TODO find a better way
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        jmsMessage.getProperties().put(JMS_TIMESTAMP, dateFormat.format(timestamp));

        Element propertiesRoot = getChildElement(header, PROPERTIES);
        List<Element> properties = getChildElements(propertiesRoot, PROPERTY);
        for (Element property : properties) {
            String key = property.getAttribute(ATTRIBUTE_NAME);
            Element firstChildElement = getFirstChildElement(property);
            String value = null;
            if (firstChildElement != null) {
                value = firstChildElement.getTextContent();
            }
            jmsMessage.getProperties().put(key, value);
        }

        Element jmsBody = getChildElement(root, BODY);
        if (jmsBody != null) {
            jmsMessage.setBody(StringUtils.trim(jmsBody.getTextContent()));
        }
        return jmsMessage;
    }

    private static Document parseXML(String xml) {
        if (StringUtils.isBlank(xml)) {
            return null;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            factory.setNamespaceAware(true);
            InputSource is = new InputSource(new StringReader(xml));
            return builder.parse(is);
        } catch (ParserConfigurationException e) {
            LOG.error("ParserConfigurationException " + e.getMessage());
            throw new JMSMonException("a", e);
        } catch (SAXException e) {
            LOG.error("SAXException " + e.getMessage());
            throw new JMSMonException("b", e);
        } catch (IOException e) {
            LOG.error("IOException " + e.getMessage());
            throw new JMSMonException("c", e);
        }

    }

    private static Element getChildElement(Element parent, String name) {
        if (parent == null || StringUtils.isBlank(name)) {
            return null;
        }

        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            if (parent.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) parent.getChildNodes().item(i);
                String localName = child.getLocalName();
                if (localName == null) {
                    localName = child.getNodeName();
                    if (localName != null && localName.contains(":")) {
                        localName = localName.substring(localName.indexOf(":") + 1);
                    }
                }
                if (localName.equals(name)) {
                    return child;
                }
            }
        }
        return null;
    }

    private static List<Element> getChildElements(Element parent, String name) {
        if (parent == null || StringUtils.isBlank(name)) {
            return null;
        }

        List<Element> childElements = new ArrayList<Element>();
        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            if (parent.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) parent.getChildNodes().item(i);
                String localName = child.getLocalName();
                if (localName == null) {
                    localName = child.getNodeName();
                    if (localName != null && localName.contains(":")) {
                        localName = localName.substring(localName.indexOf(":") + 1);
                    }
                }
                if (localName.equals(name)) {
                    childElements.add(child);
                }
            }
        }
        return childElements;
    }

    private static Element getFirstChildElement(Element parent) {
        if (parent == null) {
            return null;
        }

        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            if (parent.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) parent.getChildNodes().item(i);
                return child;
            }
        }
        return null;
    }

}
