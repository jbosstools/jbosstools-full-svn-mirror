package org.jboss.ide.eclipse.as.openshift.internal.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class StreamUtils {

	/**
	 * Writes the content of the given input stream to the given output stream
	 * and returns and input stream that may still be used to read from.
	 * 
	 * @param outputStream
	 *            the output stream to write to
	 * @param inputStream
	 *            the input stream to read from
	 * @return a new, unread input stream
	 * @throws IOException
	 */
	public static InputStream writeTo(InputStream inputStream, OutputStream outputStream) throws IOException {
		List<Byte> data = new ArrayList<Byte>();
		for (int character = -1; (character = inputStream.read()) != -1;) {
			data.add((byte) character);
			outputStream.write(character);
		}
		byte[] byteArray = new byte[data.size()];
		for (int i = byteArray.length - 1; i >= 0; i--) {
			byteArray[i] = data.get(i);
		}
		return new ByteArrayInputStream(byteArray);
	}

	public static String readToString(InputStream inputStream) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringWriter writer = new StringWriter();
			String line = null;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
			}
			return writer.toString();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * Writes the given string to the given output stream. The stream is closed
	 * after writing all data.
	 * 
	 * @param data the data to write
	 * @param outputStream the stream to write to
	 * @throws IOException
	 */
	public static void writeTo(byte[] data, OutputStream outputStream) throws IOException {
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
	}

	public static void close(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			inputStream.close();
		}
	}

	public static void close(OutputStream outputStream) throws IOException {
		if (outputStream != null) {
			outputStream.close();
		}
	}

}
