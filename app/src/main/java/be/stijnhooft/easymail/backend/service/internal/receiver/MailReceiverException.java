package be.stijnhooft.easymail.backend.service.internal.receiver;

public class MailReceiverException extends RuntimeException {

    public MailReceiverException(String reason) {
        super(reason);
    }

    public MailReceiverException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
