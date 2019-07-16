package com.bertorl.client.IOUtils.FileUtiles;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class FileUtils {

	private static final int BUFF_SIZE = 10240*2;

	public static void fragmentFileInParts(int numberParts, String pathFile) {

		RandomAccessFile file;
		pathFile = "data/video.mp4";
		try {
			file = new RandomAccessFile(pathFile, "r");
			long fileSize = file.length();
			long fragmentBytesSize = fileSize / numberParts;
			long remainingBytes = fileSize % numberParts;
			long start = 0;
			System.out.println(fragmentBytesSize);
			System.out.println(remainingBytes);
			BufferedInputStream in;
			BufferedOutputStream out;
			for (int i = 1; i <= numberParts; i++) {
				out = new BufferedOutputStream(new FileOutputStream("data/fragments/fragment" + i + ".mp4"));
				file.seek(start);
				in = new BufferedInputStream(new FileInputStream(file.getFD()));
				copyData(out, in, start, fragmentBytesSize);
				start += fragmentBytesSize;

			}

			if (remainingBytes > 0) {
				in = new BufferedInputStream(new FileInputStream(file.getFD()));
				out = new BufferedOutputStream(new FileOutputStream("data/fragments/fragment-" + "remaining" + ".mp4"));
				copyData(out, in, start, fragmentBytesSize);
				out.close();
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void mountingFragmentsIntoFile(String pathFile, int numberParts) throws Exception {

		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("data/mount/videoNew.mp4"));
		int b;
		for (int i = 1; i <= numberParts; i++) {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(pathFile + "/fragment" + i + ".mp4"));
			// byte[] buff = new byte[2*1024];
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			out.flush();
			in.close();

		}
		out.close();
	}

	private static void copyData(BufferedOutputStream out, BufferedInputStream in, long start, long fragmentBytesSize)
			throws IOException {

		byte[] buffer;
		long numsReads = fragmentBytesSize / BUFF_SIZE;
		long numsReadsRemaining = fragmentBytesSize % BUFF_SIZE;

		// int b = 0, cont = 0;
		// while(cont < fragmentBytesSize && (b = in.read()) != -1) {
		// out.write(b);
		// cont++;
		// }
		for (int i = 1; i <= numsReads; i++) {
			buffer = new byte[BUFF_SIZE]; // 10KB
			in.read(buffer, 0, BUFF_SIZE);
			out.write(buffer);
		}

		if (numsReadsRemaining > 0) {
			buffer = new byte[(int) numsReadsRemaining];
			in.read(buffer, 0, (int) numsReadsRemaining);
			out.write(buffer);
		}
		out.flush();
		out.close();
	}

	public static void fileToFile(String path) throws IOException {

		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("data/final/prueba.mp4"));
		BufferedInputStream in = new BufferedInputStream(new FileInputStream("data/video.mp4"));
		int b;
		while ((b = in.read()) != -1) {
			out.write(b);
		}
		in.close();
		out.close();
	}

}
