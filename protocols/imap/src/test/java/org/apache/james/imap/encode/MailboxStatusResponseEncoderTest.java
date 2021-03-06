/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.imap.encode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.james.imap.api.ImapMessage;
import org.apache.james.imap.encode.base.ByteImapResponseWriter;
import org.apache.james.imap.encode.base.ImapResponseComposerImpl;
import org.apache.james.imap.message.response.MailboxStatusResponse;
import org.apache.james.mailbox.MessageUid;
import org.junit.Before;
import org.junit.Test;

public class MailboxStatusResponseEncoderTest  {

    MailboxStatusResponseEncoder encoder;

    ImapEncoder mockNextEncoder;

    ByteImapResponseWriter writer = new ByteImapResponseWriter();
    ImapResponseComposer composer = new ImapResponseComposerImpl(writer);

    @Before
    public void setUp() throws Exception {
        mockNextEncoder = mock(ImapEncoder.class);
        encoder = new MailboxStatusResponseEncoder(mockNextEncoder);
    }

    @Test
    public void testIsAcceptable() {
        assertThat(encoder.isAcceptable(new MailboxStatusResponse(null, null, null,
                null, null, null, "mailbox"))).isTrue();
        assertThat(encoder.isAcceptable(mock(ImapMessage.class))).isFalse();
        assertThat(encoder.isAcceptable(null)).isFalse();
    }

    @Test
    public void testDoEncode() throws Exception {
        final Long messages = new Long(2);
        final Long recent = new Long(3);
        final MessageUid uidNext = MessageUid.of(5);
        final Long uidValidity = new Long(7);
        final Long unseen = new Long(11);
        final String mailbox = "A mailbox named desire";

        encoder.encode(new MailboxStatusResponse(messages, recent, uidNext,
                null, uidValidity, unseen, mailbox), composer, new FakeImapSession());
        assertEquals("* STATUS \"A mailbox named desire\" (MESSAGES 2 RECENT 3 UIDNEXT 5 UIDVALIDITY 7 UNSEEN 11)\r\n", writer.getString());
    }
}
