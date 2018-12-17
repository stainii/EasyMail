package be.stijnhooft.easymail.service.internal.sender;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MailSenderFactoryTest {

    @Test
    public void buildSuccessfully() {
        assertThat(MailSenderFactory.configure()
                        .emailAddress("email")
                        .host("gmail")
                        .password("pass")
                        .port(1000)
                        .build(),
                is(notNullValue()));
    }

    @Test(expected = MailSenderException.class)
    public void buildWithoutCompleteConfiguration() {
        MailSenderFactory.configure()
                .emailAddress("email")
                .host("gmail")
                .password("pass")
                //.port(1000)
                .build();
    }

}