package com.bertorl.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.bertorl.server.services.IncomingPeerConnectionService;
import com.sun.corba.se.spi.activation.Server;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	int port = 24600;
    	ServerSocket server = null;
        while(true) {
        	try {
    			server = new ServerSocket(port);
    			Thread th = new Thread(new IncomingPeerConnectionService(server.accept()));
    			th.start();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	finally {
        		server.close();
        	}
        }
    }
}
