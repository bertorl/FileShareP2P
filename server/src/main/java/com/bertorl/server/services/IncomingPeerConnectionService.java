package com.bertorl.server.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import com.bertorl.server.IOUtils.IOUtils;

public class IncomingPeerConnectionService implements Runnable {

	private Socket socket;
	private Map<String, String>peerTable;
	
	public IncomingPeerConnectionService(Socket socket) {
		this.socket = socket;
		this.peerTable = new ConcurrentHashMap<String,String>();
	}

	@Override
	public void run() {
		String peerUUID = "";
		String line = "";
		try {
			InputStream in = socket.getInputStream();
			peerUUID = IOUtils.readLine(in);
			in.close();
			InputStream inTable = new FileInputStream(new File("data/peer-table.txt"));
			
			while (!(line = IOUtils.readLine(inTable)).equals("")) {
				StringTokenizer st = new StringTokenizer(line, " ");
				String hashKey = st.nextToken();
				String peerIP = st.nextToken();
				peerTable.put(hashKey, peerIP);
			}
			inTable.close();
			
			if(peerTable.containsKey(peerUUID)) {
				System.out.println(peerTable.containsKey(peerUUID));
				Socket s = new Socket("localhost", 24601);
				OutputStream out = s.getOutputStream();
				
				peerTable.forEach((k,v)->{try {
					out.write((k+" "+v+"\r\n").getBytes());
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}});
				out.write("\r\n".getBytes());
				out.flush();
				out.close();
				s.close();
			}
			
			else {
				System.out.println(peerTable.containsKey(peerUUID));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
