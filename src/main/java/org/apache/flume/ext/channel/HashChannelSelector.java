/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.flume.ext.channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.channel.AbstractChannelSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashChannelSelector extends AbstractChannelSelector {

  public static final String CONFIG_MULTIPLEX_HEADER_NAME = "header";
  public static final String DEFAULT_MULTIPLEX_HEADER =
      "flume.selector.header";
  public static final String CONFIG_PREFIX_MAPPING = "mapping.";

  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory
    .getLogger(HashChannelSelector.class);

  private static final List<Channel> EMPTY_LIST =
      Collections.emptyList();

  private String headerName;

  List<Channel>[] channels;
  @Override
  
  public List<Channel> getRequiredChannels(Event event) {
    String headerValue = event.getHeaders().get(headerName);
    if (headerValue == null || headerValue.trim().length() == 0) {
      return channels[0];
    }

    return channels[(headerValue.hashCode() & 0x7FFFFFFF) % channels.length];
  }

  @Override
  public List<Channel> getOptionalChannels(Event event) {
    return EMPTY_LIST;
  }

  @SuppressWarnings("unchecked")
@Override
  public void configure(Context context) {
    this.headerName = context.getString(CONFIG_MULTIPLEX_HEADER_NAME,
        DEFAULT_MULTIPLEX_HEADER);
    channels = new List[getAllChannels().size()];
    for(int i=0; i<getAllChannels().size(); i++) {
    	channels[i] = new ArrayList<Channel>();
    	channels[i].add(getAllChannels().get(i));
    }
  }
}
