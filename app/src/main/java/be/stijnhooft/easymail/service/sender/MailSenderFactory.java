package be.stijnhooft.easymail.service.sender;

import java.util.List;

public class MailSenderFactory {

    private MailSenderConfiguration configuration;

    public static MailSenderFactory configure() {
        return new MailSenderFactory();
    }

    private MailSenderFactory() {
        this.configuration = new MailSenderConfiguration();
    }

    public MailSenderFactory emailAddress(String emailAddress) {
        this.configuration.setEmailAddress(emailAddress);
        return this;
    }

    public MailSenderFactory password(String password) {
        this.configuration.setPassword(password);
        return this;
    }

    public MailSenderFactory host(String host) {
        this.configuration.setHost(host);
        return this;
    }

    public MailSenderFactory port(int port) {
        this.configuration.setPort(port);
        return this;
    }

    public MailSender build() {
        List<String> emptyFields = configuration.getEmptyFields();
        if (emptyFields.isEmpty()) {
            return new MailSender(configuration);
        } else {
            throw new MailSenderException("The mail sender is not fully configured. Please provide the following configuration: " + emptyFields);
        }
    }

}
