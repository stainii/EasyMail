package be.stijnhooft.easymail.service.subscription;

import java.util.List;

import be.stijnhooft.easymail.model.Protocol;

public class MailSubscriptionFactory {

    private MailSubscriptionConfiguration configuration;;

    public static MailSubscriptionFactory configure() {
        return new MailSubscriptionFactory();
    }

    private MailSubscriptionFactory() {
        this.configuration = new MailSubscriptionConfiguration();
    }

    public MailSubscriptionFactory emailAddress(String emailAddress) {
        this.configuration.setEmailAddress(emailAddress);
        return this;
    }

    public MailSubscriptionFactory password(String password) {
        this.configuration.setPassword(password);
        return this;
    }

    public MailSubscriptionFactory host(String host) {
        this.configuration.setHost(host);
        return this;
    }

    public MailSubscriptionFactory port(int port) {
        this.configuration.setPort(port);
        return this;
    }

    public MailSubscriptionFactory protocol(Protocol protocol) {
        this.configuration.setProtocol(protocol);
        return this;
    }

    public MailSubscriptionFactory intervalInMilliseconds(int time) {
        this.configuration.setIntervalInMilliseconds(time);
        return this;
    }

    public MailSubscription build() {
        List<String> emptyFields = configuration.getEmptyFields();
        if (emptyFields.isEmpty()) {
            return new MailSubscription(configuration);
        } else {
            throw new MailSubscriptionException("The mail subscription is not fully configured. Please provide the following configuration: " + emptyFields);
        }
    }

}
