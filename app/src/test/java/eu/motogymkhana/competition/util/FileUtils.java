package eu.motogymkhana.competition.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {

	public static String readFile(Context context, String name) throws IOException {

		InputStreamReader reader = new InputStreamReader(context.getAssets().open(name));

		BufferedReader in = null;
		try {
			in = new BufferedReader(reader);
			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null) {
				buffer.append(line).append('\n');
			}
			// Chomp the last newline
			buffer.deleteCharAt(buffer.length() - 1);
			return buffer.toString();
		} catch (IOException e) {
			return "";
		} finally {
			closeStream(in);
		}
	}

	/**
	 * Closes the specified stream.
	 *
	 * @param stream
	 *            The stream to close.
	 */
	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				// Ignore
			}
		}
	}
}
