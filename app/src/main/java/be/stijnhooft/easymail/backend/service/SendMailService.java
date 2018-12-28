package be.stijnhooft.easymail.backend.service;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.backend.model.Mail;
import be.stijnhooft.easymail.backend.repository.MailRepository;
import be.stijnhooft.easymail.backend.repository.PersonRepository;
import be.stijnhooft.easymail.backend.repository.SettingRepository;
import be.stijnhooft.easymail.backend.service.internal.sender.MailSender;
import be.stijnhooft.easymail.backend.service.internal.sender.MailSenderFactory;

/**
 * Required intent extras:
 * * message: the content of the message that needs to be sent
 * * to: the email address of which the message should be sent to
 */
public class SendMailService extends IntentService {

    public static final String MESSAGE = "message";
    public static final String TO = "to";
    private final MailSender mailSender;
    private final PersonRepository personRepository;
    private final MailRepository mailRepository;
    private final SettingRepository settingRepository;

    public SendMailService() {
        this("SendMailService");
    }

    public SendMailService(String name) {
        super(name);
        EasyMailApplication application = EasyMailApplication.getInstance();
        this.mailRepository = new MailRepository(application);
        this.personRepository = new PersonRepository(application);
        this.settingRepository = new SettingRepository(application);

        JSONObject settings = this.settingRepository.getSettings();
        try {
            mailSender = MailSenderFactory.configure()
                    .emailAddress(settings.getString("emailAddress"))
                    .host(settings.getJSONObject("sender").getString("host"))
                    .port(settings.getJSONObject("sender").getInt("port"))
                    .password(settings.getJSONObject("sender").getString("password"))
                    .build();
        } catch (JSONException e) {
            throw new RuntimeException("Could not use settings", e);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        onHandleIntent(intent, false);
    }

    protected void onHandleIntent(Intent intent, boolean isRetrying) {
        String content = intent.getStringExtra(MESSAGE);
        String to = intent.getStringExtra(TO);

        if (!isRetrying) {
            insertIntoDatabase(content, to);
        }

        try {
            mailSender.sendMail(content, to);
        } catch (Exception e) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e1) {
                throw new RuntimeException(e1);
            }
            onHandleIntent(intent, true); //try again
        }
    }

    private void insertIntoDatabase(String content, String to) {
        Mail mail = new Mail(to, new Date().toString(), content, false, true);
        mailRepository.save(mail);
    }

}
