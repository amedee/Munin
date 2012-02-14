/**
 *
 */
package be.amedee.munin.mail.mailage;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * @author amedee
 *
 */
public class MailAge {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Session mailSession = Session.getInstance(new Properties());
		InputStream source = System.in;
		try {
			MimeMessage message = new MimeMessage(mailSession, source);
			try {
				System.out.println(getDateHeader(message));
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Date getDateHeader(javax.mail.Message msg)
			throws MessagingException {
		Date today = new Date();
		String[] received = msg.getHeader("received");
		if (received != null)
			for (int i = 0; i < received.length; i++) {
				String dateStr = null;
				try {
					dateStr = getDateString(received[i]);
					if (dateStr != null) {
						Date msgDate = parseDate(dateStr);
						if (!msgDate.after(today))
							return msgDate;
					}
				} catch (ParseException ex) {
				}
			}

		String[] dateHeader = msg.getHeader("date");
		if (dateHeader != null) {
			String dateStr = dateHeader[0];
			try {
				Date msgDate = parseDate(dateStr);
				if (!msgDate.after(today))
					return msgDate;
			} catch (ParseException ex) {
			}
		}

		return today;
	}

	private static String getDateString(String text) {
		String[] daysInDate = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		int startIndex = -1;
		for (int i = 0; i < daysInDate.length; i++) {
			startIndex = text.lastIndexOf(daysInDate[i]);
			if (startIndex != -1)
				break;
		}
		if (startIndex == -1) {
			return null;
		}

		return text.substring(startIndex);
	}

	private static Date parseDate(String dateStr) throws ParseException {
		SimpleDateFormat dateFormat;
		try {
			dateFormat = new SimpleDateFormat("EEE, d MMM yy HH:mm:ss Z");
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			try {
				dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
				return dateFormat.parse(dateStr);
			} catch (ParseException ex) {
				dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm:ss Z");
				return dateFormat.parse(dateStr);
			}
		}
	}
}
