//
//package de.fomad.sigserver.controller;
//
//import com.mchange.v2.c3p0.ComboPooledDataSource;
//import java.beans.PropertyVetoException;
//import java.io.File;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//
///**
// *
// * @author boreas
// */
//public class DatabaseController2 {
//    
//    private static final Logger LOGGER = LogManager.getLogger(DatabaseController2.class);
//    
//    private ComboPooledDataSource databasePool;
//    
//    public void initDatabase() throws ClassNotFoundException, SQLException, PropertyVetoException {
//
//	File dbFile = new File("~/sigthing_server.db");
//
//	databasePool = new ComboPooledDataSource();
//	databasePool.setDriverClass("org.h2.Driver");
//	databasePool.setJdbcUrl("jdbc:h2:~/sigthing_server.db");
//	databasePool.setUser("");
//	databasePool.setPassword("");
//
//	boolean doesFileExist = dbFile.exists();
//	if (!doesFileExist) {
//	    createStructure();
//	}
//	LOGGER.info("created database controller.");
//    }
//
//    private void createStructure() throws SQLException {
//	String[] initQueries = new String[]{
//	    "CREATE TABLE IF NOT EXISTS solar_systems (id INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, name VARCHAR(255) UNIQUE, added_by VARCHAR(255), comment TEXT, added TIMESTAMP DEFAULT NOW())",
//	    "CREATE TABLE IF NOT EXISTS signatures (id INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, signature VARCHAR(255), scan_group VARCHAR(255), signal_strength FLOAT, solar_system_id INT, name VARCHAR(255), added_by VARCHAR(255), comment TEXT, added TIMESTAMP DEFAULT NOW())",
//	    "CREATE TABLE IF NOT EXISTS pilots (id INT PRIMARY KEY NOT NULL, character_name VARCHAR(255) UNIQUE) "};
//	try (Connection connection = databasePool.getConnection(); Statement statement = connection.createStatement()) {
//	    for (String query : initQueries) {
//		statement.executeUpdate(query);
//	    }
//	}
//	LOGGER.info("created database structure.");
//    }
//}
