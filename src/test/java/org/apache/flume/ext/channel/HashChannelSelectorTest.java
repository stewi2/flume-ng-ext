package org.apache.flume.ext.channel;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flume.Channel;
import org.apache.flume.ChannelSelector;
import org.apache.flume.Event;
import org.apache.flume.channel.ChannelSelectorFactory;
import org.junit.Before;
import org.junit.Test;

public class HashChannelSelectorTest {

	private List<Channel> channels = new ArrayList<Channel>();
	private ChannelSelector selector;

	@Before
	public void setUp() throws Exception {
		channels.clear();
		channels.add(MockChannel.createMockChannel("ch1"));
		channels.add(MockChannel.createMockChannel("ch2"));
		channels.add(MockChannel.createMockChannel("ch3"));
		channels.add(MockChannel.createMockChannel("ch4"));
		channels.add(MockChannel.createMockChannel("ch5"));

		Map<String, String> config = new HashMap<String, String>();
		config.put("type", "org.apache.flume.ext.channel.HashChannelSelector");
		config.put("header", "myheader");

		selector = ChannelSelectorFactory.create(channels, config);
	}

	  @Test
	public void testGetRequiredChannels() {
		MockEvent event = new MockEvent();
		assertEquals(1, selector.getRequiredChannels(event).size());
		assertEquals(channels.get(0), selector.getRequiredChannels(event).get(0));

		event.setHeader("myheader", "foo");
		assertEquals(1, selector.getRequiredChannels(event).size());
		assertEquals(channels.get(("foo".hashCode() & 0x7FFFFFFF)%channels.size()), selector.getRequiredChannels(event).get(0));

		event.setHeader("myheader", "baz");
		assertEquals(1, selector.getRequiredChannels(event).size());
		assertEquals(channels.get(("baz".hashCode() & 0x7FFFFFFF)%channels.size() ), selector.getRequiredChannels(event).get(0));	  
	  }

	@Test
	public void testGetOptionalChannels() {
		Event event = new MockEvent();
		assertEquals(0, selector.getOptionalChannels(event).size());
	}

}
