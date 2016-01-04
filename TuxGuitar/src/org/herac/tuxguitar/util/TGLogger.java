package org.herac.tuxguitar.util;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TGLogger {

    public static final String DEFAULT_LOG_FILE = "/var/tmp/tuxguitar.log";


    public static void log(String message) {
        log(message, DEFAULT_LOG_FILE);
    }

    public static void log(Throwable throwable) {
        log(throwable, DEFAULT_LOG_FILE);
    }

    public static void log(Throwable throwable, String filename) {
        if (throwable == null) {
            throwable = new NullPointerException("Throwable passed to logger is null");
        }
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        log(stringWriter.toString(), filename);
    }

    public static void log(String message, String filename) {
        FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(filename, true);

			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]: ");
			String dateString = sdf.format(cal.getTime());

			byte bytes[] = (dateString + message + "\n").getBytes();
			stream.write(bytes, 0, bytes.length);

		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			}
			catch (Exception e) { }
		}
    }

}
