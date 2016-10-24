/**
 * Author Asya
 * Date Oct 20, 2016
 */
package ixtens.my.serverclientapp.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * This class describes Command entity. It contains the following fields:
 * identifier id, String serviceName, String methodName, array of parameters
 * corresponding to method arguments.
 * Also it contains some inner container aka serverResponse for storing of appropriate response.
 */
public class Command implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5851811778770019418L;

    private static transient AtomicInteger uniqueId = new AtomicInteger();

    private int id;

    private String serviceName;

    private String methodName;

    private Object[] params;
    
    private transient BlockingQueue<Response> serverResponse = new ArrayBlockingQueue<>(1);

    /**
     * @param serviceName
     * @param methodName
     * @param params
     */
    public Command(String serviceName, String methodName, Object[] params) {
        super();
        this.id = uniqueId.incrementAndGet();
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.params = params;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @return the params
     */
    public Object[] getParams() {
        return params;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Command [id=" + id + ", serviceName=" + serviceName + ", methodName=" + methodName + ", params="
                + Arrays.toString(params) + "]";
    }
    
    /**
     * Returns response waiting if it's necessary
     * 
     * @return
     * @throws InterruptedException
     */
    public Response waitForResponse() throws InterruptedException {
        return this.serverResponse.take();
    }
    
    /**
     * Inserts a response waiting if it's necessary
     * 
     * @param r
     * @throws InterruptedException
     */
    public void putResponse(Response r) throws InterruptedException {
        this.serverResponse.put(r);
    }
}
