/**
 * Author Asya
 * Date Oct 20, 2016
 */
package ixtens.my.serverclientapp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ixtens.my.serverclientapp.domain.Command;
import ixtens.my.serverclientapp.domain.Response;

/**
 * Server that gets clients requests and generates responses. 
 * Responses are resulting objects of calling services classes methods.  
 *
 */
public class Server implements Runnable {

    protected int          serverPort   = 8080;
    protected static ServerSocket serverSocket = null;

    public Server(int port){
        this.serverPort = port;
    }

    private class WorkerRunnable implements Runnable {

        private Socket clientSocket = null;
        private ExecutorService threadPool = Executors.newCachedThreadPool();
        
        public WorkerRunnable(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                while (true) {
                    InputStream input = clientSocket.getInputStream();
                    ObjectInputStream in = new ObjectInputStream(input);
                    //try to read a next command
                    Command command = (Command) in.readObject();
                    System.out.println("Command to perform: " + command);
                    //open a new thread in a pool to work with command
                    this.threadPool.execute(new CommandThread(clientSocket, command));
                }
            } catch (IOException e) {
                System.out.println("Connection reset");
                return;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    
    private class CommandThread implements Runnable {
        private Socket clientSocket = null;
        private Command command;
        
        /**
         * @param clientSocket
         * @param command
         */
        CommandThread(Socket clientSocket, Command command) {
            this.clientSocket = clientSocket;
            this.command = command;
        }

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            //generate response
            Response r = ServerUtils.generateResponse(command);
            System.out.println("Response processed: " + r);
            //write in socket synchronized response 
            synchronized (clientSocket) {
                try {
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    out.writeObject(r);
                    out.flush();
                } catch (IOException e) {
                    System.out.println("Connection reset");
                    return;
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }
            }
        }
        
    }
    
    public void run() {
        //open server socket
        openServerSocket();
        Socket clientSocket = null;
        while (true) {
            System.out.println("Server listening...") ;
            
            try {
                clientSocket = serverSocket.accept();
                //run new thread to make deal with new connection
                new Thread(
                        new WorkerRunnable(clientSocket)
                        ).start();
            } catch (IOException e) {
                if (serverSocket.isClosed()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
        }
    }

    private void openServerSocket() {
        try {
            serverSocket = new ServerSocket(this.serverPort);
            serverSocket.setReuseAddress(true);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }
    
    public static void main(String[] args) throws IOException {
        if (args.length == 0)
            throw new IllegalArgumentException("Arguments are absent");
        int port = Integer.parseInt(args[0]);
        Server server = new Server(port);
        new Thread(server).start();
        Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
            try {
                serverSocket.close();
                System.out.println("The server is shut down!");
            } catch (IOException e) { /* failed */ }
        }});
    }
}
