package be.stijnhooft.easymail.service.subscription;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MailFormatterTest {

    private MailFormatter mailFormatter;

    @Before
    public void setUp() throws Exception {
        mailFormatter = new MailFormatter();
    }

    @Test
    public void stripAwayPreviousMessagesWhenMailContainsNoPreviousMessages() {
        String mail = "\nHello!\nHow are you?\nMy email is stijnhooft@hotmail.com...\n";
        String expected = "Hello!" + System.lineSeparator() + "How are you?" + System.lineSeparator() + "My email is stijnhooft@hotmail.com...";
        assertEquals(expected, mailFormatter.stripAwayPreviousMessages(mail));
    }

    @Test
    public void stripAwayPreviousMessagesWhenMailContainsPreviousMessages() {
        String mail = "Hello!\nHow are you?\nMy email is stijnhooft@hotmail.com...\n\nFrom: Stijn Hooft <stijnhooft@hotmail.com>\nSent at: 2018-11-29 11:00:00\n\nThis is a previous message";
        String expected = "Hello!" + System.lineSeparator() + "How are you?" + System.lineSeparator() + "My email is stijnhooft@hotmail.com...";
        assertEquals(expected, mailFormatter.stripAwayPreviousMessages(mail));
    }
}