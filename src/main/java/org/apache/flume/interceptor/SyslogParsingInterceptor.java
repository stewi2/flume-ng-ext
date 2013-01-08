package org.apache.flume.interceptor;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.source.SyslogParser;

import com.google.common.collect.Lists;

public class SyslogParsingInterceptor implements Interceptor {

	SyslogParser parser;
	
	@Override
	public void initialize() {
		parser = new SyslogParser();
	}

	@Override
	public Event intercept(Event event) {
		Event result = parser.parseMessage(new String(event.getBody(),Charset.forName("US-ASCII")), Charset.forName("UTF-8"));
		event.setBody(result.getBody());
		event.getHeaders().putAll(result.getHeaders());
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

		@Override
		public void configure(Context context) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Interceptor build() {
			return new SyslogParsingInterceptor();
		}
	}

}
