package com.bertorl.client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bertorl.client.p2p.Peer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {	
    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	try {
			executor.execute(new Peer());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
