package be.stijnhooft.easymail.service.subscription;

import org.junit.Test;

import be.stijnhooft.easymail.model.Protocol;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MailSubscriptionFactoryTest {

    @Test
    public void buildSuccessfully() {
        assertThat(MailSubscriptionFactory.configure()
                        .emailAddress("email")
                        .host("gmail")
                        .password("pass")
                        .port(1000)
                        .protocol(Protocol.IMAPS)
                        .intervalInMilliseconds(1000)
                        .build(),
                is(notNullValue()));
    }

    @Test(expected = MailSubscriptionException.class)
    public void buildWithoutCompleteConfiguration() {
        MailSubscriptionFactory.configure()
                .emailAddress("email")
                .host("gmail")
                .password("pass")
                //.port(1000)
                //.intervalInMilliseconds(1000)
                .protocol(Protocol.IMAPS)
                .build();
    }

}