package be.stijnhooft.easymail.service.subscription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import be.stijnhooft.easymail.repository.SettingRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MailFormatterTest {

    private MailFormatter mailFormatter;

    @Mock
    private SettingRepository settingRepository;

    @Mock
    private JSONObject settings;

    @Mock
    private JSONArray responsePatterns;

    @Before
    public void init() throws JSONException {
        doReturn(settings).when(settingRepository).getSettings();
        doReturn(responsePatterns).when(settings).getJSONArray("responsePatterns");
        doReturn(1).when(responsePatterns).length();
        doReturn("<(.+)@(.+)>").when(responsePatterns).getString(0);

        mailFormatter = new MailFormatter(settingRepository);
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