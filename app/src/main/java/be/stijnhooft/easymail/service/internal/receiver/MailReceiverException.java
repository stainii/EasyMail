package be.stijnhooft.easymail.service.internal.receiver;

public class MailReceiverException extends RuntimeException {

    public MailReceiverException(String reason) {
        super(reason);
    }

    public MailReceiverException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
