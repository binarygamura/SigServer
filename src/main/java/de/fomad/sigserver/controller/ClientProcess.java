package de.fomad.sigserver.controller;

import java.net.Socket;
import java.util.Observable;
import java.util.Properties;
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
    
    public ClientProcess(Properties properties, Socket accepted){
        this.accepted = accepted;
        this.properties = properties;
    }
    
    @Override
    public void run() {
        try{
            boolean keepAlive = true;
            while(keepAlive){
                //read message
                //write response
            }
        }
        catch(Exception ex){
            LOGGER.fatal("client process threw an exception.", ex);
        }
        finally{
            LOGGER.info("client process terminated.");
        }
    }
}
