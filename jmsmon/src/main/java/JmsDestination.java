import javax.management.ObjectName;

/**
 * Created by Cosmin on 8/8/2017.
 */
public class JmsDestination {

    private String name;
    private String fqName;
    private String serverName;
    private ObjectName objectName;
    private Long numberOfMessages;
    private DestinationType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFqName() {
        return fqName;
    }

    public void setFqName(String fqName) {
        this.fqName = fqName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public ObjectName getObjectName() {
        return objectName;
    }

    public void setObjectName(ObjectName objectName) {
        this.objectName = objectName;
    }

    public Long getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(Long numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public DestinationType getType() {
        return type;
    }

    public void setType(DestinationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "JmsDestination{" +
                "fqName='" + fqName + '\'' +
                ", numberOfMessages=" + numberOfMessages +
                ", type=" + type +
                '}';
    }

    public static enum DestinationType {
        QUEUE, TOPIC
    }
}
