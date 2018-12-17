package be.stijnhooft.easymail.service.internal.sender;

public class MailSenderException extends RuntimeException {

    public MailSenderException(String reason) {
        super(reason);
    }

    public MailSenderException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
