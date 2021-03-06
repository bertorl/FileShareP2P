package com.bertorl.client.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
			while ((line = inReader.readLine()) != null) {
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

	public static String getPeerUUID() throws IOException {

		File file = new File("data/peer-UUID.txt");
		String UUID = "";
		if (!file.exists()) {
			file.createNewFile();
			UUID = generateUUID(file);
		} else {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			UUID = reader.readLine();
			reader.close();
		}

		return UUID;
	}

	private static String generateUUID(File  file) throws FileNotFoundException {

		PrintWriter writer= new PrintWriter(file);
		String peerUUID = UUID.randomUUID().toString();
		writer.print(peerUUID);
		writer.close();
		return peerUUID;

	}
	
	public static String zipSharedFile(String filePath) throws Exception {

		String zipFilePath = filePath+".zip";
		FileOutputStream fileOut = new FileOutputStream(zipFilePath);
		ZipOutputStream zipOut = new ZipOutputStream(fileOut);
		
		zipOut.putNextEntry(new ZipEntry(filePath));
		BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(filePath));
		byte[] bytes = new byte[1024]; //1KB
		while(buffIn.read(bytes) >-1) {
			zipOut.write(bytes, 0, bytes.length);
		}
		buffIn.close();
		zipOut.closeEntry();
		zipOut.close();
		
		return zipFilePath;
		
	}
}
