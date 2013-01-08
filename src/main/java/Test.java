import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.flume.Event;
import org.apache.flume.source.SyslogParser;

public class Test {
	public static void main(String[] args) throws IOException {
		SyslogParser parser = new SyslogParser();
		Event event = parser.parseMessage("<13>1 2013-01-02T23:49:00+00:00 proxy4.pod2.sac1.zdsys.com nginx - - [timeQuality isSynced=\"0\" tzKnown=\"1\"][file@18372.4 position=\"849463662\" size=\"849470301\" name=\"/var/log/zendesk/nginx.access.log\"] 220.244.31.98 - - [02/Jan/2013:23:49:00 +0000] \"GET /api/v1/tickets/16658 HTTP/1.1\" 200 1937 \"https://zanui.zendesk.com/agent/\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11\" \"-\" \"zanui.zendesk.com\" \"0.412\"", Charset.forName("UTF-8"));
//		Event event = parser.parseMessage("<165>1 2003-08-24T05:14:15.000003-07:00 192.0.2.1 myproc 8710 - - %% It's time to make the do-nuts.", Charset.forName("UTF-8"));
		System.out.println(event);
		System.out.write(event.getBody());
	}
}
