package com.bertorl.server.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class IOUtils {

	public static String readLine(InputStream in) throws IOException {

		int flag = 0;
		StringBuffer line = new StringBuffer();
		while (flag < 2) {
			char c = (char) in.read();
			if (c == '\n' || c == '\r')
				flag++;
			else
				line.append(c);
		}
		return line.toString();
	}

	public static ConcurrentHashMap<String, String> loadPeerTable() {
		
		File file = new File("data/peer-table.txt");
		String line = "";
		ConcurrentHashMap<String, String> peerTable = new ConcurrentHashMap<String, String>();
		try {
			BufferedReader inReader = new BufferedReader(new FileReader(file));
			while((line = inReader.readLine())!= null) {
				StringTokenizer st = new StringTokenizer(line, " ");
				String UUID = st.nextToken();
				String peerIP = st.nextToken();
				peerTable.put(UUID, peerIP);
			}
			inReader.close();	
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		return peerTable;
	}
	
	public static void logTableToTextFile(ConcurrentHashMap<String, String> peerTable) {
		
		File file = new File("data/peer-table-log.txt");
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file, true));
			peerTable.forEach((k, v) -> {
				try {
					out.println(k + " " + v);
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			out.println("--------------------------------");
			out.flush();
			out.close();
				
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
	}
}
