/**
 * Author Asya
 * Date Oct 20, 2016
 */
package ixtens.my.serverclientapp;

import java.io.*;
import java.net.Socket;

import ixtens.my.serverclientapp.domain.Command;
import ixtens.my.serverclientapp.domain.Response;

/**
 * Client that makes requests on server
 *
 */
public class Client implements IClient {

    private static Socket clientSocket;
    
    private ClientService clientService;
    
    public Client(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        clientService = new ClientService(clientSocket);
        new Thread(clientService).start();
    }
    
    /**
     * @return the connection state of the socket
     */
    public boolean isConnected() {
        return clientSocket.isConnected();
    }
    
    /**
     * Closes this socket
     */
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /* (non-Javadoc)
     * @see ixtens.my.serverclientapp.IClient#remoteCall(java.lang.String, java.lang.String, java.lang.Object[])
     */
    public Object remoteCall(String serviceName, String methodName, Object[] params) {
        Command newCommand = new Command(serviceName, methodName, params);
        //logging command
        System.out.println("Command: " + newCommand);
        Response response = new Response();
        try {
            clientService.submitCommand(newCommand);
            response = newCommand.waitForResponse();
            switch (response.getBadResponse()) {
            case OK:
                System.out.println("Command " + newCommand.getId() + ": response is " + (response.isReturnsVoid() ? "void result" : response.getResponseData()));
                break;
            default:
                throw new ResponseException(response.getBadResponse().getReason());
            }
            return response.getResponseData();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        } 
        catch (ResponseException e) {
            System.out.println(e.toString());
        }
        return response.getResponseData();
    }

}
