package org.apache.flume.ext.interceptor;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.flume.ext.source.SyslogParser;

import com.google.common.collect.Lists;

import static org.apache.flume.ext.interceptor.SyslogParsingInterceptor.Constants.*;

public class SyslogParsingInterceptor implements Interceptor {

	SyslogParser parser;
	
	private boolean setParsable;
	
	public SyslogParsingInterceptor(boolean setParsable) {
		this.setParsable = setParsable;
	}
	
	@Override
	public void initialize() {
		parser = new SyslogParser();
	}

	@Override
	public Event intercept(Event event) {
		try {
			Event result = parser.parseMessage(new String(event.getBody(),Charset.forName("US-ASCII")), Charset.forName("UTF-8"));
			event.setBody(result.getBody());
			event.getHeaders().putAll(result.getHeaders());
		} catch(IllegalArgumentException e) {
			if (this.setParsable == true)
				event.getHeaders().put("parsable", "false");
			else return null;
		}
		
		return event;
	}

	@Override
	public List<Event> intercept(List<Event> events) {
	    List<Event> intercepted = Lists.newArrayListWithCapacity(events.size());
	    for (Event event : events) {
	      Event interceptedEvent = intercept(event);
	      if (interceptedEvent != null) {
	        intercepted.add(interceptedEvent);
	      }
	    }
	    return intercepted;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	public static class Builder implements Interceptor.Builder {

		private boolean setParsable  = SET_PARSABLE_DFLT;
		
		@Override
		public void configure(Context context) {
			this.setParsable = context.getBoolean(SET_PARSABLE, SET_PARSABLE_DFLT);
		}

		@Override
		public Interceptor build() {			
			return new SyslogParsingInterceptor(setParsable);
		}
	}
	
	public static class Constants {
		public static String SET_PARSABLE = "setParsable";
		public static boolean SET_PARSABLE_DFLT = false;
	}

}
