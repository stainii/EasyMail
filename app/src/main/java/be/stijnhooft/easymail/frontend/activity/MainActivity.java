package be.stijnhooft.easymail.frontend.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import be.stijnhooft.easymail.R;
import be.stijnhooft.easymail.backend.model.Mail;
import be.stijnhooft.easymail.backend.model.MailViewModel;
import be.stijnhooft.easymail.backend.model.Person;
import be.stijnhooft.easymail.backend.model.PersonViewModel;
import be.stijnhooft.easymail.backend.service.CheckMailService;
import be.stijnhooft.easymail.backend.service.internal.OnSelectPersonListener;
import be.stijnhooft.easymail.backend.service.internal.receiver.MailReceiverWorkManagerFactory;
import be.stijnhooft.easymail.frontend.viewAdapter.MessageThreadViewAdapter;
import be.stijnhooft.easymail.frontend.viewAdapter.PersonViewAdapter;

public class MainActivity extends AppCompatActivity {

    private MailViewModel mailViewModel;
    private PersonViewModel personViewModel;
    private MailReceiverWorkManagerFactory mailReceiverWorkManagerFactory;
    private final OnSelectPersonListener ON_SELECT_PERSON = selectedPerson -> {
        Person previousSelectedPerson = personViewModel.getSelectedPerson();
        personViewModel.setSelectedPerson(selectedPerson);

        if (previousSelectedPerson != null) {
            markMessagesAsReadFor(previousSelectedPerson);
        }

        showMailsFor(selectedPerson);
        ((TextView) findViewById(R.id.selected_person_name)).setText(" " + selectedPerson.getName());
    };

    private LiveData<List<Mail>> messagesOfCurrentlySelectedPerson;
    private Handler recurrentTaskHandler = new Handler();
    private Runnable checkForNewMailAtAHighRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mailReceiverWorkManagerFactory = new MailReceiverWorkManagerFactory(this.getApplication());

        setContentView(R.layout.activity_main);

        this.personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        this.mailViewModel = ViewModelProviders.of(this).get(MailViewModel.class);
        registerToListOfPersons();
        initAddMessageButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRecurringCheckOnNewMails();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecurringCheckOnNewMails();
    }

    private void stopRecurringCheckOnNewMails() {
        recurrentTaskHandler.removeCallbacks(checkForNewMailAtAHighRate);
    }


    private void registerToListOfPersons() {
        this.personViewModel.getPersons()
                .observe(this, this::initPersonViewAndLoadMessages);
    }

    private void markMessagesAsReadFor(Person person) {
        messagesOfCurrentlySelectedPerson.removeObservers(this);

        personViewModel.markMessagesAsRead(person);
        if (messagesOfCurrentlySelectedPerson != null) {
            final List<Mail> previousMails = messagesOfCurrentlySelectedPerson.getValue();
            if (previousMails != null) {
                mailViewModel.markAsRead(previousMails);
            }
        }
    }

    private void showMailsFor(Person person) {
        if (person != null) {
            messagesOfCurrentlySelectedPerson = mailViewModel.getMessagesFor(person);
            messagesOfCurrentlySelectedPerson.observe(this, this::fillMessageThreadView);
        }
    }

    private void initPersonViewAndLoadMessages(List<Person> persons) {
        RecyclerView recyclerView = findViewById(R.id.person_view);

        PersonViewAdapter adapter = new PersonViewAdapter(this, persons, ON_SELECT_PERSON);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectDefaultPersonIfNecessary();
    }

    private void selectDefaultPersonIfNecessary() {
        if (personViewModel.getSelectedPerson() == null) {
            // TODO: select person with new messages
        }
    }

    private void fillMessageThreadView(List<Mail> newMails) {
        RecyclerView recyclerView = findViewById(R.id.message_thread_view);
        MessageThreadViewAdapter adapter = new MessageThreadViewAdapter(this, newMails);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setStackFromEnd(true);
        recyclerView.setLayoutManager(layout);
    }

    private void initAddMessageButton() {
        findViewById(R.id.add_mail).setOnClickListener(v -> {
            final Intent sendMessageActivity = new Intent(this, SendMessageActivity.class);
            sendMessageActivity.putExtra("contact", personViewModel.getSelectedPerson());
            startActivity(sendMessageActivity);
        });
    }

    private void startRecurringCheckOnNewMails() {
        // start service that survives the activity. This checks for new mails at a low rate
        Intent service = new Intent(this, CheckMailService.class);
        startService(service);

        // start high-rate checking that gets killed if the activity stops.
        checkForNewMailAtAHighRate = new Runnable() {
            @Override
            public void run() {
                try {
                    mailReceiverWorkManagerFactory.scheduleOneTime();
                } finally {
                    // 100% guarantee that this always happens, even if
                    // your update method throws an exception
                    recurrentTaskHandler.postDelayed(this, 30000);
                }
            }
        };
        recurrentTaskHandler.post(checkForNewMailAtAHighRate);

    }

}
