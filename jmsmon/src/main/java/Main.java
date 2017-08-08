/**
 * Created by Cosmin on 8/8/2017.
 */
public class Main {

    public static void main(String[] args) {
        JmsService jmsService = new JmsServiceImpl();
        System.out.println("getDestinations=" + jmsService.getDestinations());
        System.out.println("countMessagesInQueue1=" + jmsService.countMessagesInQueue("ms3", "Queue1", "1=1"));
        System.out.println("findMessagesInQueue1=" + jmsService.findMessagesInQueue("ms3", "Queue1", 0l, 100l, "1=1"));
        JmsMessage message = jmsService.getMessageWithBody("ms3", "Queue1", "ID:<171455.1502228718548.0>");
        System.out.println("getMessageWithBody=" + message);
        if (message != null) {
            System.out.println("body=" + message.getBody());
        }
        jmsService.moveMessages("ms3", "Queue1", "Queue1_DLQ", "JMSMessageID = 'ID:<171455.1502228718548.0>'");
        System.out.println("move called");
        System.out.println("findMessagesInQueue1=" + jmsService.findMessagesInQueue("ms3", "Queue1", 0l, 100l, "1=1"));
        System.out.println("findMessagesInQueue1_DLQ=" + jmsService.findMessagesInQueue("ms3", "Queue1_DLQ", 0l, 100l, "1=1"));
        jmsService.removeMessages("ms3", "Queue1_DLQ", "JMSMessageID = 'ID:<171455.1502228718548.0>'");
        System.out.println("remove called");
        System.out.println("findMessagesInQueue1_DLQ=" + jmsService.findMessagesInQueue("ms3", "Queue1_DLQ", 0l, 100l, "1=1"));
    }

}
