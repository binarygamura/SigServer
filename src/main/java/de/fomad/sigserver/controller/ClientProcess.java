package de.fomad.sigserver.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.fomad.siglib.dto.AuthRequest;
import de.fomad.siglib.dto.ErrorResponse;
import de.fomad.siglib.network.NetworkException;
import de.fomad.siglib.network.NetworkMessage;
import de.fomad.siglib.network.NetworkReader;
import de.fomad.siglib.network.NetworkWriter;
import de.fomad.siglib.network.NetworkWriterQueue;
import de.fomad.siglib.entities.Pilot;
import java.net.Socket;
import java.util.Observable;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author boreas
 */
public class ClientProcess extends Observable implements Runnable{

    private static final Logger LOGGER = LogManager.getLogger(ClientProcess.class);
    
    private final Properties properties;
    
    private final Socket accepted;
    
    private Pilot pilot;
    
    private final Gson gson = new GsonBuilder().create();
    
    private NetworkWriterQueue queue;
    
    private ExecutorService threadPool;
    
    public ClientProcess(Properties properties, Socket accepted, ExecutorService threadPool){
        this.accepted = accepted;
        this.properties = properties;
        this.threadPool = threadPool;
    }
    
    @Override
    public void run() {
        try(NetworkReader reader = new NetworkReader(accepted.getInputStream());
            NetworkWriter writer = new NetworkWriter(accepted.getOutputStream())){
            
            
            //read auth credentials.
            NetworkMessage read = reader.readMessage(), response = new NetworkMessage();
            AuthRequest authRequest = gson.fromJson(read.getContent(), AuthRequest.class);
            
            
            if(authRequest.getPassword().equals(properties.getProperty("password"))){
                
                queue = new NetworkWriterQueue(writer);
                threadPool.submit(queue);
                
                pilot = authRequest.getCharacter();
                response.setResource("OKAY");
                queue.addToQueue(response);
                
                boolean keepAlive = true;
                while(keepAlive){
                    try{
                        //read message
                        read = reader.readMessage();
                        //routing
                        switch(read.getResource()){
                            case "EXIT":
                                keepAlive = false;
                                break;
                            case "SYSTEM_CHANGE":
                                break;
                            case "UPDATE":
                                break;
                        }
                        //send response
                    }
                    catch(NetworkException ex){
                        LOGGER.warn(ex);
                        response = new NetworkMessage();
                        ErrorResponse error = new ErrorResponse();
                        error.setReason(ex.getMessage());
                        response.setContent(gson.toJson(error));
                        queue.addToQueue(response);
                    }
                }
            }
            else {
                response = new NetworkMessage();
                response.setResource("ERROR");
                writer.writeMessage(response);
            }
        }
        catch(Exception ex){
            LOGGER.fatal("client process threw an exception.", ex);
        }
        finally{
            LOGGER.info("client process terminated.");
//            if(queue != null){
//                queue.close();
//            }
        }
    }
}
