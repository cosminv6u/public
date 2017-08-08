import java.util.List;

/**
 * Created by Cosmin on 8/8/2017.
 */
public interface JmsService {

    List<JmsDestination> getDestinations();

    JmsDestination getDestinationByName(String serverName, String name);

    Long countMessagesInQueue(String serverName, String queueSource, String selector);

    List<JmsMessage> findMessagesInQueue(String serverName, String queueSource, Long begin, Long numberOfItems, String selector);

    JmsMessage getMessageWithBody(String serverName, String queueSource, String messageId);

    Integer moveMessages(String serverName, String queueSource, String queueTarget, String selector);

    public Integer removeMessages(String serverName, String destinationSource, String selector);

}
