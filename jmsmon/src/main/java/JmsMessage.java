import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cosmin on 8/8/2017.
 */
public class JmsMessage {

    private String id;
    private Date timestamp;
    private String body;
    private Map<String, Object> properties = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "JmsMessage{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", properties=" + properties +
                '}';
    }
}
