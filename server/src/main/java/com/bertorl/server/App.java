package com.bertorl.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.bertorl.server.IOUtils.*;
import com.bertorl.server.services.IncomingPeerConnectionService;

public class App 
{
    private static final int INIT_CONNECTION_PORT = 24600;

	public static void main( String[] args ) throws IOException {
		
		ServerSocket server = new ServerSocket(INIT_CONNECTION_PORT);;
    	ExecutorService executor = Executors.newFixedThreadPool(1);
    	ConcurrentHashMap<String, String>peerTable = IOUtils.loadPeerTable();
    	
        while(true) {
        	try {
        		Socket s = server.accept();
    			executor.execute(new IncomingPeerConnectionService(s, peerTable));
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
    }
}
