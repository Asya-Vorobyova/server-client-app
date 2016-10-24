/**
 * Author Asya
 * Date Oct 23, 2016
 */
package ixtens.my.serverclientapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ixtens.my.serverclientapp.domain.Command;
import ixtens.my.serverclientapp.domain.Response;

/**
 * Service method to make multithreading write-read socket operations.
 * 
 * It runs using two threads: 
 * a first (main) thread performs retrieving commands from queue and writing  it to socket;
 * a second thread performs socket data reading and adding responses to appropriate commands. 
 *
 */
public class ClientService implements Runnable {
    private Socket socket;
    
    /**
     * Map of all unperformed commands
     */
    private Map<Integer, Command> commandsMap = new HashMap<>();
    
    /**
     * Queue of already unperformed commands
     */
    private BlockingQueue<Command> commandsQueue = new LinkedBlockingQueue<>();
    
    public ClientService(Socket socket) {
        this.socket = socket;
    }

    /**
     * Reads response from server and adds a result in appropriate command
     */
    private void readAndFill() {
        while (true) {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Response response = (Response) in.readObject();
                Command command = commandsMap.get(response.getCommandId());
                command.putResponse(response);
                synchronized (socket) {
                    commandsMap.remove(response.getCommandId());
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                System.out.println("Connection with server reset");
                return;
            }
            
        }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        //run a special thread for reading responses and putting it to commands
        new Thread(() -> readAndFill()).start();
        
        while (true) {
            try {
                // wait for the next command
                Command currentCommand = commandsQueue.take();
                // write to socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(currentCommand);
                out.flush();
            } catch (InterruptedException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that adds a next command
     * 
     * @param command
     * @throws InterruptedException
     */
    public void submitCommand(Command command) throws InterruptedException {
        commandsMap.put(command.getId(), command);
        commandsQueue.put(command);
    }
}
