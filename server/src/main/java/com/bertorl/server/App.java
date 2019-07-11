package com.bertorl.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.bertorl.server.IOUtils.*;
import com.bertorl.server.services.IncomingPeerConnectionService;

public class App 
{
    private static final int INIT_CONNECTION_PORT = 24600;

	public static void main( String[] args ) throws IOException {
		
		ServerSocket server = null;
    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	ConcurrentHashMap<String, String>peerTable = IOUtils.loadPeerTable();
    	
        while(true) {
        	try {
    			server = new ServerSocket(INIT_CONNECTION_PORT);
    			executor.execute(new IncomingPeerConnectionService(server.accept(), peerTable));
    		} catch (IOException e) {
    		
    			e.printStackTrace();
    		}
        	finally {
        		executor.shutdown();
        		server.close();
        	}
        }
    }
}
