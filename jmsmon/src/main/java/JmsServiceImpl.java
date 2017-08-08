import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cosmin on 8/8/2017.
 */
public class JmsServiceImpl implements JmsService {

    // JMX Attributes & Operations
    public static final String MOVE_MESSAGES = "moveMessages";
    public static final String DELETE_MESSAGES = "deleteMessages";
    public static final String GET_MESSAGE = "getMessage";
    public static final String GET_ITEMS = "getItems";
    public static final String GET_CURSOR_SIZE = "getCursorSize";
    public static final String GET_MESSAGES = "getMessages";
    public static final String DESTINATION_INFO = "DestinationInfo";
    public static final String QUEUE_DESTINATION_TYPE = "Queue";
    public static final String DESTINATION_TYPE = "DestinationType";
    public static final String MESSAGES_CURRENT_COUNT = "MessagesCurrentCount";
    public static final String QUEUE_NAME = "Name";
    public static final String SERVER_NAME = "Name";
    public static final String DESTINATIONS = "Destinations";
    public static final String JMS_SERVERS = "JMSServers";
    public static final String JMS_RUNTIME = "JMSRuntime";
    public static final String SERVER_RUNTIME = "ServerRuntime";
    public static final String SERVER_RUNTIMES = "ServerRuntimes";
    public static final Long MAX_NUMBER_OF_ITEMS = 5000l; // maximum supported by t3 protocol
    // END - JMX Attributes & Operations
    public static final Integer MESSAGES_STATE_MASK = 511; // all states
    private static final Logger LOG = LoggerFactory.getLogger(JmsServiceImpl.class);

    private static boolean isAdmin;

    /* ---- Business methods ---- */

    private static MBeanServerConnection getConnection() {
        String protocol = System.getProperty("protocol");
        if (protocol == null) {
            protocol = "t3";
        }
        String host = System.getProperty("host");
        Integer port = Integer.valueOf(System.getProperty("port"));
        String user = System.getProperty("user");
        String pass = System.getProperty("pass");
        isAdmin = Boolean.valueOf(System.getProperty("isAdmin"));
        return JMXHelper.getRemoteMBeanServerConnection(protocol, host, port, user, pass, isAdmin);
    }

    private static ObjectName getService() {
        return isAdmin ? JMXHelper.getDomainRuntimeService() : JMXHelper.getRuntimeService();
    }

    private static ObjectName getRuntimeService(MBeanServerConnection mbsc, String serverName) {
        ObjectName drs = getService();

        try {
            if (!isAdmin) {
                return (ObjectName) mbsc.getAttribute(drs, SERVER_RUNTIME);
            }

            ObjectName[] servers = (ObjectName[]) mbsc.getAttribute(drs, SERVER_RUNTIMES);
            for (ObjectName server : servers) {
                String srvrName = (String) mbsc.getAttribute(server, SERVER_NAME);
                if (srvrName.equalsIgnoreCase(serverName)) {
                    return server;
                }
            }
        } catch (AttributeNotFoundException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (InstanceNotFoundException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (MBeanException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (ReflectionException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (IOException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        }
        return null;
    }

    private static String getSimpleName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        String retName = null;
        // SystemModuleCluster!JMSServerCluster@ms1@DistributedQueue1
        if (name.contains("@")) {
            retName = name.substring(name.lastIndexOf("@") + 1);
        }
        if (!StringUtils.isBlank(retName)) {
            name = retName;
        }
        // SystemModuleStandalone!Queue1
        if (name.contains("!")) {
            retName = name.substring(name.lastIndexOf("!") + 1);
        }

        if (StringUtils.isBlank(retName)) {
            return name;
        }
        return retName;
    }

    private static void fillDestinations(MBeanServerConnection mbsc, List<JmsDestination> destinations, ObjectName server) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {

        LOG.debug("Server " + server);
        ObjectName jmsRuntime = (ObjectName) mbsc.getAttribute(server, JMS_RUNTIME);
        String serverName = (String) mbsc.getAttribute(server, SERVER_NAME);
        ObjectName[] jmsServers = (ObjectName[]) mbsc.getAttribute(jmsRuntime, JMS_SERVERS);
        for (ObjectName jmsServer : jmsServers) {
            LOG.debug("JMS Server " + jmsServer);
            ObjectName[] jmsQueues = (ObjectName[]) mbsc.getAttribute(jmsServer, DESTINATIONS);

            for (ObjectName jmsQueue : jmsQueues) {
                LOG.debug("JMS Destination " + jmsQueue);
                JmsDestination destination = new JmsDestination();
                destination.setObjectName(jmsQueue);

                String destinationFQName = (String) mbsc.getAttribute(jmsQueue, QUEUE_NAME);
                destination.setFqName(destinationFQName);

                String destinationName = getSimpleName(destinationFQName);
                destination.setName(destinationName);

                Long numberOfMessages = (Long) mbsc.getAttribute(jmsQueue, MESSAGES_CURRENT_COUNT);
                destination.setNumberOfMessages(numberOfMessages);

                destination.setServerName(serverName);

                String destinationType = (String) mbsc.getAttribute(jmsQueue, DESTINATION_TYPE);
                if (QUEUE_DESTINATION_TYPE.equals(destinationType)) { // we don't check yet for "Topic"
                    destination.setType(JmsDestination.DestinationType.QUEUE);
                } else {
                    LOG.warn("Only Queues are processed. " + destinationType + " \"" + destinationName + "\" was skipped");
                    continue;
                }

                destinations.add(destination);
            }
        }
    }

    private static JmsDestination getDestinationByName(MBeanServerConnection mbsc, String serverName, String name) {
        JmsDestination destination = null;

        try {
            ObjectName server = getRuntimeService(mbsc, serverName);

            LOG.debug("Server " + server);
            ObjectName jmsRuntime = (ObjectName) mbsc.getAttribute(server, JMS_RUNTIME);
            ObjectName[] jmsServers = (ObjectName[]) mbsc.getAttribute(jmsRuntime, JMS_SERVERS);
            for (ObjectName jmsServer : jmsServers) {
                LOG.debug("JMS Server " + jmsServer);
                ObjectName[] jmsQueues = (ObjectName[]) mbsc.getAttribute(jmsServer, DESTINATIONS);

                for (ObjectName jmsQueue : jmsQueues) {
                    LOG.debug("JMS Destination " + jmsQueue);
                    String destinationFQName = (String) mbsc.getAttribute(jmsQueue, QUEUE_NAME);
                    String destinationName = getSimpleName(destinationFQName);
                    if (!name.equals(destinationName)) {
                        continue;
                    }

                    destination = new JmsDestination();
                    destination.setServerName(serverName);
                    destination.setObjectName(jmsQueue);
                    destination.setFqName(destinationFQName);
                    destination.setName(destinationName);

                    Long numberOfMessages = (Long) mbsc.getAttribute(jmsQueue, MESSAGES_CURRENT_COUNT);
                    destination.setNumberOfMessages(numberOfMessages);

                    String destinationType = (String) mbsc.getAttribute(jmsQueue, DESTINATION_TYPE);
                    if (QUEUE_DESTINATION_TYPE.equals(destinationType)) { // we don't check yet for "Topic"
                        destination.setType(JmsDestination.DestinationType.QUEUE);
                    } else {
                        LOG.warn("Only Queues are processed. " + destinationType + " \"" + destinationName + "\" was skipped");
                        continue;
                    }
                }

            }

        } catch (AttributeNotFoundException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (InstanceNotFoundException e) {
            LOG.error("InstanceNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (MBeanException e) {
            LOG.error("MBeanException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (ReflectionException e) {
            LOG.error("ReflectionException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (IOException e) {
            LOG.error("IOException " + e.getMessage());
            throw new JMSMonException("", e);
        }
        return destination;
    }

    public List<JmsDestination> getDestinations() {

        MBeanServerConnection mbsc = getConnection();
        if (mbsc == null) {
            return null;
        }

        ObjectName drs = getService();

        List<JmsDestination> destinations = new ArrayList<>();
        try {
            if (isAdmin) {
                ObjectName[] servers = (ObjectName[]) mbsc.getAttribute(drs, SERVER_RUNTIMES);
                for (ObjectName server : servers) {
                    fillDestinations(mbsc, destinations, server);
                }
            } else {
                ObjectName server = (ObjectName) mbsc.getAttribute(drs, SERVER_RUNTIME);
                fillDestinations(mbsc, destinations, server);
            }
        } catch (AttributeNotFoundException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (InstanceNotFoundException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (MBeanException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (ReflectionException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (IOException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        }
        return destinations;
    }

    public JmsDestination getDestinationByName(String serverName, String name) {
        MBeanServerConnection mbsc = getConnection();
        if (mbsc == null) {
            return null;
        }

        JmsDestination destination = getDestinationByName(mbsc, serverName, name);
        destination.setObjectName(null); // this is needed only in BackEnd
        return destination;
    }

    public Long countMessagesInQueue(String serverName, String queueSource, String selector) {
        MBeanServerConnection mbsc = getConnection();
        if (mbsc == null) {
            return null;
        }

        Integer timeout = new Integer(0);

        JmsDestination destination = getDestinationByName(mbsc, serverName, queueSource);

        if (destination == null) {
            return null;
        }

        ObjectName destinationON = destination.getObjectName();

        String messageCursor = null;
        Long totalAmountOfMessages = null;

        try {
            messageCursor = (String) mbsc.invoke(destinationON, GET_MESSAGES,
                    new Object[]{selector, timeout, MESSAGES_STATE_MASK},
                    new String[]{String.class.getName(), Integer.class.getName(), Integer.class.getName()});

            totalAmountOfMessages = (Long) mbsc.invoke(destinationON, GET_CURSOR_SIZE, new Object[]{messageCursor},
                    new String[]{String.class.getName()});
        } catch (InstanceNotFoundException e) {
            LOG.error("InstanceNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (MBeanException e) {
            LOG.error("MBeanException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (ReflectionException e) {
            LOG.error("ReflectionException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (IOException e) {
            LOG.error("IOException " + e.getMessage());
            throw new JMSMonException("", e);
        }

        return totalAmountOfMessages;
    }

    public List<JmsMessage> findMessagesInQueue(String serverName, String queueSource, Long begin, Long numberOfItems, String selector) {

        if (begin < 0) {
            LOG.warn(" BEGIN (" + begin + ") was smaller than 0, now it was transformed to 0");
            begin = 0l;
        }
        if (numberOfItems < 0) {
            LOG.warn(" NUMBER_OF_ITEMS (" + numberOfItems + ") was smaller than 0, now it was transformed to 0");
            numberOfItems = 0l;
        }
        if (numberOfItems == 0) {
            LOG.warn(" NUMBER_OF_ITEMS was 0, returning NULL ");
            return null; // returns no items
        }
        if (numberOfItems > MAX_NUMBER_OF_ITEMS) {
            LOG.warn(" NUMBER_OF_ITEMS (" + numberOfItems + ") was greater than the maximum threshold, now it was transformed to " + MAX_NUMBER_OF_ITEMS);
            numberOfItems = MAX_NUMBER_OF_ITEMS;
        }

        MBeanServerConnection mbsc = getConnection();
        if (mbsc == null) {
            return null;
        }

        List<JmsMessage> jmsMessageList = null;

        Integer timeout = new Integer(0);

        JmsDestination destination = getDestinationByName(mbsc, serverName, queueSource);

        if (destination == null) {
            return null;
        }

        ObjectName destinationON = destination.getObjectName();

        String messageCursor = null;
        CompositeData[] allMessageMetaData = null;

        try {
            messageCursor = (String) mbsc.invoke(destinationON, GET_MESSAGES,
                    new Object[]{selector, timeout, MESSAGES_STATE_MASK},
                    new String[]{String.class.getName(), Integer.class.getName(), Integer.class.getName()});

            Long totalAmountOfMessages = (Long) mbsc.invoke(destinationON, GET_CURSOR_SIZE, new Object[]{messageCursor},
                    new String[]{String.class.getName()});

            if (new Long(0l).equals(totalAmountOfMessages)) {
                LOG.warn(" TOTAL_AMOUNT_OF_MESSAGES is " + totalAmountOfMessages + ", returning NULL ");
                return null;
            }
            if (begin > totalAmountOfMessages) {
                LOG.warn(" BEGIN (" + begin + ") was greater than TOTAL_AMOUNT_OF_MESSAGES (" + totalAmountOfMessages + "), returning NULL ");
                throw new JMSMonException("", new RuntimeException("Number of messages changed. Please search again!"));
            }

            LOG.info("begin: " + begin);
            LOG.info("numberOfItems: " + numberOfItems);
            LOG.info("totalAmountOfMessages: " + totalAmountOfMessages);
            if (numberOfItems > totalAmountOfMessages - begin) {
                numberOfItems = totalAmountOfMessages - begin;
            }
            if (numberOfItems == 0) {
                LOG.warn(" NUMBER_OF_ITEMS was 0, returning NULL ");
                throw new JMSMonException("", new RuntimeException("Number of messages changed. Please search again!"));
            }
            LOG.info("numberOfItems corrected: " + numberOfItems);

            jmsMessageList = new ArrayList<JmsMessage>(numberOfItems.intValue());

            allMessageMetaData = (CompositeData[]) mbsc.invoke(destinationON, GET_ITEMS,
                    new Object[]{messageCursor, begin, numberOfItems.intValue()},
                    new String[]{String.class.getName(), Long.class.getName(), Integer.class.getName()});

        } catch (InstanceNotFoundException e) {
            LOG.error("InstanceNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (MBeanException e) {
            LOG.error("MBeanException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (ReflectionException e) {
            LOG.error("ReflectionException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (IOException e) {
            LOG.error("IOException " + e.getMessage());
            throw new JMSMonException("", e);
        }

        for (CompositeData messageMetaData : allMessageMetaData) {
            JmsMessage message = JMSMessageUtil.convertMessage(messageMetaData);
            jmsMessageList.add(message);
        }
        return jmsMessageList;
    }

    public JmsMessage getMessageWithBody(String serverName, String queueSource, String messageId) {

        MBeanServerConnection mbsc = getConnection();
        if (mbsc == null) {
            return null;
        }

        ObjectName destinationON = getDestinationByName(mbsc, serverName, queueSource).getObjectName();
        CompositeData messageDataDetails = null;
        try {
            messageDataDetails = (CompositeData) mbsc.invoke(destinationON, GET_MESSAGE, new Object[]{messageId}, new String[]{String.class.getName()});
        } catch (InstanceNotFoundException e) {
            LOG.error("InstanceNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (MBeanException e) {
            LOG.error("MBeanException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (ReflectionException e) {
            LOG.error("ReflectionException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (IOException e) {
            LOG.error("IOException " + e.getMessage());
            throw new JMSMonException("", e);
        }
        JmsMessage jmsMessage = JMSMessageUtil.convertMessage(messageDataDetails);
        if (jmsMessage != null) {
            jmsMessage.setBody(XMLUtil.formatXML(jmsMessage.getBody()));
        }
        return jmsMessage;
    }

    public Integer moveMessages(String serverName, String queueSource, String queueTarget, String selector) {

        MBeanServerConnection mbsc = getConnection();
        if (mbsc == null) {
            return null;
        }

        ObjectName sourceON = getDestinationByName(mbsc, serverName, queueSource).getObjectName();
        ObjectName targetON = getDestinationByName(mbsc, serverName, queueTarget).getObjectName();
        CompositeData queueTargetCD = null;
        Integer numberOfItemsMoved = null;

        try {
            queueTargetCD = (CompositeData) mbsc.getAttribute(targetON, DESTINATION_INFO);
            numberOfItemsMoved = (Integer) mbsc.invoke(sourceON, MOVE_MESSAGES, new Object[]{selector, queueTargetCD}, new String[]{String.class.getName(), CompositeData.class.getName()});
        } catch (AttributeNotFoundException e) {
            LOG.error("AttributeNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (InstanceNotFoundException e) {
            LOG.error("InstanceNotFoundException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (MBeanException e) {
            LOG.error("MBeanException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (ReflectionException e) {
            LOG.error("ReflectionException " + e.getMessage());
            throw new JMSMonException("", e);
        } catch (IOException e) {
            LOG.error("IOException " + e.getMessage());
            throw new JMSMonException("", e);
        }
        return numberOfItemsMoved;
    }

    public Integer removeMessages(String serverName, String destinationSource, String selector) {
        MBeanServerConnection mbsc = getConnection();

        if (mbsc == null) {
            return null;
        }

        ObjectName destinationON = getDestinationByName(mbsc, serverName, destinationSource).getObjectName();
        Integer numberOfItemsRemoved;
        try {
            numberOfItemsRemoved = (Integer) mbsc.invoke(destinationON, DELETE_MESSAGES, new Object[]{selector}, new String[]{String.class.getName()});
        } catch (InstanceNotFoundException e) {
            LOG.error("InstanceNotFoundException " + e.getMessage());
            throw new JMSMonException("2", e);
        } catch (MBeanException e) {
            LOG.error("MBeanException " + e.getMessage());
            throw new JMSMonException("3", e);
        } catch (ReflectionException e) {
            LOG.error("ReflectionException " + e.getMessage());
            throw new JMSMonException("4", e);
        } catch (IOException e) {
            LOG.error("IOException " + e.getMessage());
            throw new JMSMonException("5", e);
        }
        return numberOfItemsRemoved;
    }

}
