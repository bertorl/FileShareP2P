package com.bertorl.client.IOUtils.FileUtiles;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class FileUtils {

	private static final int BUFF_SIZE = 10240;

	public static void fragmentFileInParts(int numberParts, String pathFile) {

		RandomAccessFile file;
		pathFile = "data/video.mp4";
		try {
			file = new RandomAccessFile(pathFile, "r");
			long fileSize = file.length();
			long fragmentBytesSize = fileSize / numberParts;
			long remainingBytes = fileSize % numberParts;
			long start = 0;
			for (int i = 1; i <= numberParts; i++) {
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream("data/fragments/fragment" + i + ".mp4"));
				copyData(out, file, start, 0, fragmentBytesSize);
				start += fragmentBytesSize;
				out.close();
				System.out.println(start);
			}

			if (remainingBytes > 0) {
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("data/fragments/fragment-" +"remaining"+ ".mp4"));
				copyData(out, file, start, remainingBytes, fragmentBytesSize);
				out.close();
				System.out.println(remainingBytes);
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void mountingFragmentsIntoFile() {
		
	}

	private static void copyData(BufferedOutputStream out, RandomAccessFile file, long start, long remaining,
			long fragmentBytesSize) throws IOException {

		file.seek(start);
		byte[] buffer;
		long numsReads = fragmentBytesSize / BUFF_SIZE;
		long numsReadsRemaining = fragmentBytesSize % BUFF_SIZE;

		if (remaining > 0) {
			buffer = new byte[(int) remaining]; // 10KB
			file.read(buffer, 0, (int) remaining);
		} else {
			for (int i = 1; i <= numsReads; i++) {
				buffer = new byte[BUFF_SIZE]; // 10KB
				file.read(buffer, 0, BUFF_SIZE);
				out.write(buffer);
				out.flush();
			}
			if(numsReadsRemaining>0) {
				
			}
		}

		out.flush();
	}

}
