package be.stijnhooft.easymail.service.subscription;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import be.stijnhooft.easymail.model.Protocol;

public class MailSubscriptionConfiguration {

    private String emailAddress;
    private String password;
    private String host;
    private int port;
    private Protocol protocol;
    private long intervalInMilliseconds;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public long getIntervalInMilliseconds() {
        return intervalInMilliseconds;
    }

    public void setIntervalInMilliseconds(long intervalInMilliseconds) {
        this.intervalInMilliseconds = intervalInMilliseconds;
    }

    public List<String> getEmptyFields() {
        List<String> emptyFields = new ArrayList<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!field.getName().startsWith("$")) {
                try {
                    if (field.get(this) == null || field.get(this).equals(0) || field.get(this).equals(0L)) {
                        emptyFields.add(field.getName());
                    }
                } catch (IllegalAccessException e) {
                    throw new MailSubscriptionException("Something went horribly wrong checking which fields are not filled in in the mail subscription configuration", e);
                }
            }
        }
        return emptyFields;
    }
}
