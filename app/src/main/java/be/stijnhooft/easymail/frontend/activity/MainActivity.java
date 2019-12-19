package be.stijnhooft.easymail.frontend.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
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
import be.stijnhooft.easymail.backend.service.internal.listener.BluetoothService;
import be.stijnhooft.easymail.backend.service.internal.receiver.MailReceiverWorkManagerFactory;
import be.stijnhooft.easymail.constants.Permissions;
import be.stijnhooft.easymail.frontend.viewAdapter.MessageThreadViewAdapter;
import be.stijnhooft.easymail.frontend.viewAdapter.PersonViewAdapter;

/**
 * Optional intent extras:
 * * PERSON_TO_SHOW: the email address of the person to be selected.
 *                   If not provided, a person with unread messages will be shown.
 *                   If all messages are read, the first person of the list is selected.
 */
public class MainActivity extends AppCompatActivity {

    public static final String PERSON_TO_SELECT = "PERSON_TO_SELECT";

    private final BluetoothService bluetoothService = new BluetoothService();
    private PersonViewModel personViewModel;
    private final OnSelectPersonListener ON_SELECT_PERSON = selectedPerson -> {
        Person previousSelectedPerson = personViewModel.getSelectedPerson();
        personViewModel.setSelectedPerson(selectedPerson);

        if (previousSelectedPerson != null) {
            markMessagesAsReadFor(previousSelectedPerson);
        }

        this.selectedPerson = selectedPerson;
        showMailsFor(selectedPerson);
        ((TextView) findViewById(R.id.selected_person_name)).setText(" " + selectedPerson.getName());
    };
    private MailViewModel mailViewModel;
    private MailReceiverWorkManagerFactory mailReceiverWorkManagerFactory;
    private LiveData<List<Mail>> messagesOfCurrentlySelectedPerson;
    private Handler recurrentTaskHandler = new Handler();
    private Runnable checkForNewMailAtAHighRate;
    private Person selectedPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mailReceiverWorkManagerFactory = new MailReceiverWorkManagerFactory(this.getApplication());

        setContentView(R.layout.activity_main);

        this.personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        this.mailViewModel = ViewModelProviders.of(this).get(MailViewModel.class);
        registerToListOfPersons();
        initAddMessageButton();
        requestPermissions();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                Permissions.ALL, Permissions.REQUEST_CODE);
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
        if (selectedPerson != null) {
            markMessagesAsReadFor(selectedPerson);
        }
    }

    private void stopRecurringCheckOnNewMails() {
        recurrentTaskHandler.removeCallbacks(checkForNewMailAtAHighRate);
    }


    private void registerToListOfPersons() {
        this.personViewModel.getPersons()
                .observe(this, this::initPersonViewAndLoadMessages);
    }

    private void markMessagesAsReadFor(Person person) {
        if (messagesOfCurrentlySelectedPerson != null) {
            messagesOfCurrentlySelectedPerson.removeObservers(this);

            if (mailViewModel.isThereAnyUnreadMail()) {
                personViewModel.markMessagesAsRead(person);
                final List<Mail> previousMails = messagesOfCurrentlySelectedPerson.getValue();
                if (previousMails != null) {
                    mailViewModel.markAsRead(previousMails);
                }

                if (mailViewModel.hasEverythingBeenRead()) {
                    bluetoothService.onEverythingRead();
                }
            }
        }
    }

    private void showMailsFor(Person person) {
        if (person != null) {
            messagesOfCurrentlySelectedPerson = mailViewModel.getMailFor(person);
            messagesOfCurrentlySelectedPerson.observe(this, this::fillMessageThreadView);
        }
    }

    private void initPersonViewAndLoadMessages(List<Person> persons) {
        selectDefaultPersonIfNecessary(persons);

        RecyclerView recyclerView = findViewById(R.id.person_view);
        PersonViewAdapter adapter = new PersonViewAdapter(this, persons, ON_SELECT_PERSON);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void selectDefaultPersonIfNecessary(List<Person> persons) {
        if (personViewModel.getSelectedPerson() == null) {
            if (persons != null && !persons.isEmpty()) {
                Person personThatShouldBeSelectedAsDefault = persons.get(0);
                for (Person p : persons) {
                    if (getIntent().getStringExtra(PERSON_TO_SELECT) != null ||
                            (getIntent().getStringExtra(PERSON_TO_SELECT) == null && p.hasNewMessages())) {
                        personThatShouldBeSelectedAsDefault = p;
                    }
                }
                ON_SELECT_PERSON.onSelectPerson(personThatShouldBeSelectedAsDefault);
            }
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
