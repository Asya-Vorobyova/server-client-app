/**
 * Author Asya
 * Date Oct 23, 2016
 */
package ixtens.my.serverclientapp;

/**
 *
 * Interface for Client
 */
public interface IClient {
    
    /**
     * Method that makes remote method call, i.e. sends to a server a command corresponding to given arguments and
     * waits for the response.This method isn't synchronized so it can be called from different threads. 
     * 
     * @param serviceName name of a service on the server
     * @param methodName service method's name
     * @param params method's arguments
     * @return method's return value object
     */
    public Object remoteCall(String serviceName, String methodName, Object[] params);

}
