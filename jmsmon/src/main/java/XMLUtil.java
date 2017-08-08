import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

/**
 * Created by Cosmin on 8/8/2017.
 */
public class XMLUtil {

    public static String formatXML(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Writer out = new StringWriter();
            tf.transform(new DOMSource(doc), new StreamResult(out));
            return out.toString();
        } catch (Exception e) {
            return input;
        }
    }

}
