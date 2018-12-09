package be.stijnhooft.easymail.service.subscription;

import org.junit.Test;

import java.util.ArrayList;

import be.stijnhooft.easymail.model.Protocol;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MailSubscriptionConfigurationTest {

    @Test
    public void getEmptyFieldsWhenThereAreNone() {
        MailSubscriptionConfiguration mailSubscriptionConfiguration = new MailSubscriptionConfiguration();
        mailSubscriptionConfiguration.setEmailAddress("email");
        mailSubscriptionConfiguration.setHost("host");
        mailSubscriptionConfiguration.setPassword("psss");
        mailSubscriptionConfiguration.setPort(9100);
        mailSubscriptionConfiguration.setProtocol(Protocol.IMAPS);
        mailSubscriptionConfiguration.setIntervalInMilliseconds(1000);
        assertThat(mailSubscriptionConfiguration.getEmptyFields(), equalTo(new ArrayList<>()));
    }

    @Test
    public void getEmptyFieldsWhenThereAreMultiple() {
        MailSubscriptionConfiguration mailSubscriptionConfiguration = new MailSubscriptionConfiguration();
        assertThat(mailSubscriptionConfiguration.getEmptyFields().size(), is(equalTo(6)));
        assertThat(mailSubscriptionConfiguration.getEmptyFields(), hasItems("emailAddress", "host", "password", "port", "protocol", "intervalInMilliseconds"));
    }

}