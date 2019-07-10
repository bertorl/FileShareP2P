package com.bertorl.server.IOUtils;

import java.io.IOException;
import java.io.InputStream;

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
}
