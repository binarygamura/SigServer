package de.fomad.sigserver.controller;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author boreas
 */
public class Server {
    
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    
    private final DatabaseController databaseController;
    
    private ServerSocket socket;
    
    private final Properties properties;
    
    private Server(Properties properties){
        this.properties = properties;
        databaseController = new DatabaseController();
    }
    
    private Server init() throws ClassNotFoundException, SQLException, PropertyVetoException{
        databaseController.initDatabase();
        LOGGER.info("done initializing the server.");
        return this;
    }
    
    private void start() throws IOException{
        Socket accepted;
        int localPort = Integer.parseInt(properties.getProperty("local_port"));
        socket = new ServerSocket(localPort);
        
        while(!Thread.currentThread().isInterrupted() && !socket.isClosed()){
            accepted = socket.accept();
        }
    }
    
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        try (InputStream inputStream = args.length == 0 ? Server.class.getClassLoader().getResourceAsStream("config.properties") : new FileInputStream(args[0])){

	    Properties properties = new Properties();
	    properties.load(inputStream);
	    
	    LOGGER.debug("starting server...");
	    new Server(properties).init().start();
	}
	catch (ClassNotFoundException ex) {
	    LOGGER.fatal("unable to load database driver. please check your classpath!", ex);
	}
        catch(SQLException ex){
            LOGGER.fatal("Database error, have you tried to delete the database?", ex);
        }
        catch(IOException ex){
            LOGGER.fatal("IO-Exception!", ex);
        }
        catch(PropertyVetoException ex){
            LOGGER.fatal("error while initializing the connection pool!", ex);
        }
    }
}
