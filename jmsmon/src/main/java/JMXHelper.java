import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

/**
 * Created by Cosmin on 8/8/2017.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class JMXHelper {

    private static final Logger LOG = LoggerFactory.getLogger(JMXHelper.class);

    /**
     * JNDI for DomainRuntimeMBean, when connecting to the AdminServer
     */
    private static final  String REMOTE_DOMAIN_RUNTIME_MBEANSERVER_JNDI = "/jndi/weblogic.management.mbeanservers.domainruntime";

    /**
     * Object Name for DomainRuntimeMBean<br />
     * Use this to navigate through the Weblogic Server MBean hierarchy
     */
    private static final  String DOMAIN_RUNTIME_SERVICE_OBJECTNAME = "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean";

    /**
     * JNDI for RuntimeServiceMBean, when connecting directly to the ManagedServer instead of via AdminServer
     */
    private static final String REMOTE_RUNTIME_MBEANSERVER_JNDI = "/jndi/weblogic.management.mbeanservers.runtime";

    /**
     * Object Name for RuntimeServiceMBean<br />
     * Use this to navigate through the WebLogic Server MBean hierarchy
     */
    private static final String RUNTIME_SERVICE_OBJECTNAME = "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean";

    private static final String JMX_CONNECTION_TIMEOUT = "jmx.remote.x.request.waiting.timeout";

    private static final Long JMX_CONNECTION_TIMEOUT_PERIOD = new Long(15 * 60 * 1000); // 15 minutes

    private static final String JMX_REMOTE_PROTOCOL_PROVIDER = "weblogic.management.remote";

    /* ---- Business methods ---- */

    /**
     * Runtime MBean
     *
     * @return
     */
    public static ObjectName getRuntimeService() {
        ObjectName drs = null;
        try {
            drs = new ObjectName(RUNTIME_SERVICE_OBJECTNAME);
        } catch (MalformedObjectNameException e) {
            LOG.error("MalformedObjectNameException " + e.getMessage());
            throw new JMSMonException("x", e);
        }
        return drs;
    }

    /**
     * Domain Runtime MBean
     *
     * @return
     */
    public static ObjectName getDomainRuntimeService() {
        try {
            return new ObjectName(DOMAIN_RUNTIME_SERVICE_OBJECTNAME);
        } catch (MalformedObjectNameException e) {
            throw new JMSMonException("Failed to get domain runtime mbean service object name", e);
        }
    }

    /**
     * Remotely connect to an Admin Server / Managed Server depending on the isAdmin boolean flag
     * @param protocol t3
     * @param host
     * @param port
     * @param user
     * @param pass
     * @param isAdmin if true then returns a Domain MBean, otherwise a MBean
     * @return
     */
    public static MBeanServerConnection getRemoteMBeanServerConnection(String protocol, String host, Integer port, String user, String pass, boolean isAdmin) {
        JMXServiceURL serviceURL;

        try {
            serviceURL = new JMXServiceURL(protocol, host, port, isAdmin ? REMOTE_DOMAIN_RUNTIME_MBEANSERVER_JNDI : REMOTE_RUNTIME_MBEANSERVER_JNDI);

            Hashtable ctx = new Hashtable();
            ctx.put(Context.SECURITY_PRINCIPAL, user);
            ctx.put(Context.SECURITY_CREDENTIALS, pass);
            ctx.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, JMX_REMOTE_PROTOCOL_PROVIDER);
            ctx.put(JMX_CONNECTION_TIMEOUT, JMX_CONNECTION_TIMEOUT_PERIOD);

            JMXConnector connector = JMXConnectorFactory.connect(serviceURL, ctx);
            return connector.getMBeanServerConnection();
        } catch (MalformedURLException e) {
            LOG.error("MalformedURLException " + e.getMessage());
            throw new JMSMonException("y", e);
        } catch (IOException e) {
            LOG.error("IOException " + e.getMessage());
            return null;
        }

    }

}
