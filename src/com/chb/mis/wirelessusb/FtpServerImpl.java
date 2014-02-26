package com.chb.mis.wirelessusb;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by hempel on 14-2-18.
 */
public class FtpServerImpl {
	private static FtpServerImpl instance = null;
	private static FtpServer server;

	public final static int ftpServerPort = 3190;

	public static FtpServerImpl getInstance() {
		if (instance == null) {
			instance = new FtpServerImpl();
		}
		return instance;
	}

	public static void startFtpServer(String confFile) {
		//      Now, let's configure the port on which the default listener waits for connections.

		FtpServerFactory serverFactory = new FtpServerFactory();

		ListenerFactory factory = new ListenerFactory();

		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();

		String[] str = {"mkdir", confFile};
		try {
			Process ps = Runtime.getRuntime().exec(str);
			try {
				ps.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String filename = confFile + "users.properties";//"/sdcard/users.properties";
		File files = new File(filename);

		userManagerFactory.setFile(files);
		serverFactory.setUserManager(userManagerFactory.createUserManager());
		// set the port of the listener
		factory.setPort(ftpServerPort);

		// replace the default listener
		serverFactory.addListener("default", factory.createListener());

		// start the server
		stopFtpServer();
		server = serverFactory.createServer();
		try {
			server.start();
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}

	public static void startFtpServerSsl(String confFile) {
		//      Now, let's make it possible for a client to use FTPS (FTP over SSL) for the default listener.


		FtpServerFactory serverFactory = new FtpServerFactory();

		ListenerFactory factory = new ListenerFactory();

		// set the port of the listener
		factory.setPort(ftpServerPort);

		// define SSL configuration
		SslConfigurationFactory ssl = new SslConfigurationFactory();
		ssl.setKeystoreFile(new File(confFile + "ftpserver.jks"));
		ssl.setKeystorePassword("password");

		// set the SSL configuration for the listener
		factory.setSslConfiguration(ssl.createSslConfiguration());
		factory.setImplicitSsl(true);

		// replace the default listener
		serverFactory.addListener("default", factory.createListener());

		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(new File(confFile + "users.properties"));

		serverFactory.setUserManager(userManagerFactory.createUserManager());

		// start the server
		stopFtpServer();
		server = serverFactory.createServer();
		try {
			server.start();
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}

	public static void stopFtpServer() {
		if (server != null) {
			server.stop();
			server = null;
		}
	}
}
