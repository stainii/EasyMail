package be.stijnhooft.easymail.service.subscription;

public class MailSubscriptionException extends RuntimeException {

    public MailSubscriptionException(String reason) {
        super(reason);
    }

    public MailSubscriptionException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
