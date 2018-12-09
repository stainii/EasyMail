package be.stijnhooft.easymail.service.sender;

public class MailSenderException extends RuntimeException {

    public MailSenderException(String reason) {
        super(reason);
    }

    public MailSenderException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
