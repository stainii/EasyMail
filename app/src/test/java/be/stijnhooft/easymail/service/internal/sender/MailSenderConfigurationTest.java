package be.stijnhooft.easymail.service.internal.sender;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MailSenderConfigurationTest {

    @Test
    public void getEmptyFieldsWhenThereAreNone() {
        MailSenderConfiguration mailSenderConfiguration = new MailSenderConfiguration();
        mailSenderConfiguration.setEmailAddress("email");
        mailSenderConfiguration.setHost("host");
        mailSenderConfiguration.setPassword("psss");
        mailSenderConfiguration.setPort(9100);
        assertThat(mailSenderConfiguration.getEmptyFields(), equalTo(new ArrayList<>()));
    }

    @Test
    public void getEmptyFieldsWhenThereAreMultiple() {
        MailSenderConfiguration mailSenderConfiguration = new MailSenderConfiguration();
        assertThat(mailSenderConfiguration.getEmptyFields().size(), is(equalTo(4)));
        assertThat(mailSenderConfiguration.getEmptyFields(), hasItems("emailAddress", "host", "password", "port"));
    }


}