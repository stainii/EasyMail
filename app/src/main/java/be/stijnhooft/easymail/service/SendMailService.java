package be.stijnhooft.easymail.service;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.model.Mail;
import be.stijnhooft.easymail.model.Person;
import be.stijnhooft.easymail.repository.MailRepository;
import be.stijnhooft.easymail.repository.PersonRepository;
import be.stijnhooft.easymail.repository.SettingRepository;
import be.stijnhooft.easymail.service.internal.sender.MailSender;
import be.stijnhooft.easymail.service.internal.sender.MailSenderFactory;

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
        Person person = personRepository.findByEmail(to);
        Mail mail = new Mail(person, new Date().toString(), content, false);
        mailRepository.save(mail);
    }

}
