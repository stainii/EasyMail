package be.stijnhooft.easymail.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import be.stijnhooft.easymail.R;
import be.stijnhooft.easymail.model.Mail;
import be.stijnhooft.easymail.model.MailViewModel;
import be.stijnhooft.easymail.model.PersonViewModel;
import be.stijnhooft.easymail.service.CheckMailService;
import be.stijnhooft.easymail.service.SendMailService;
import be.stijnhooft.easymail.service.internal.OnSelectPersonListener;
import be.stijnhooft.easymail.service.internal.receiver.MailReceiverWorkManagerFactory;
import be.stijnhooft.easymail.viewAdapter.MessageThreadViewAdapter;
import be.stijnhooft.easymail.viewAdapter.PersonViewAdapter;

public class MainActivity extends AppCompatActivity {

    private MailViewModel mailViewModel;
    private PersonViewModel personViewModel;
    private MailReceiverWorkManagerFactory mailReceiverWorkManagerFactory;
    private final OnSelectPersonListener ON_SELECT_PERSON = selectedPerson -> {
        personViewModel.getSelectedPerson().setNewMessages(false);
        personViewModel.setSelectedPerson(selectedPerson);
        showMailsForCurrentlySelectedPerson();
        ((TextView) findViewById(R.id.selected_person_name)).setText(selectedPerson.getName());
    };
    private Handler recurrentTaskHandler = new Handler();
    private Runnable checkForNewMailAtAHighRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mailReceiverWorkManagerFactory = new MailReceiverWorkManagerFactory(this.getApplication());

        setContentView(R.layout.activity_main);

        initViewModels(savedInstanceState);
        initPersonView();
        initAddMessageButton();
        showMailsForCurrentlySelectedPerson();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRecurringCheckOnNewMails();
    }

    @Override
    protected void onPause() {
        super.onPause();
        recurrentTaskHandler.removeCallbacks(checkForNewMailAtAHighRate);
    }

    private void showMailsForCurrentlySelectedPerson() {
        if (personViewModel.getSelectedPerson() != null) {
            mailViewModel.getMessagesFor(personViewModel.getSelectedPerson())
                    .observe(this, this::fillMessageThreadView);
        }
    }

    private void initViewModels(Bundle savedInstanceState) {
        if (savedInstanceState == null || savedInstanceState.getSerializable("personViewModel") == null) {
            this.personViewModel = new PersonViewModel(this.getApplication());
            this.personViewModel.setSelectedPerson(this.personViewModel.getPersons().get(0));
            this.mailViewModel = ViewModelProviders.of(this).get(MailViewModel.class);
        } else {
            this.personViewModel = (PersonViewModel) savedInstanceState.getSerializable("personViewModel");
            this.mailViewModel = ViewModelProviders.of(this).get(MailViewModel.class);
        }
    }

    private void initPersonView() {
        RecyclerView recyclerView = findViewById(R.id.person_view);

        PersonViewAdapter adapter = new PersonViewAdapter(this, personViewModel.getPersons(), ON_SELECT_PERSON);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: select person with new messages
        ON_SELECT_PERSON.onSelectPerson(personViewModel.getSelectedPerson());
    }

    private void fillMessageThreadView(List<Mail> mails) {
        RecyclerView recyclerView = findViewById(R.id.message_thread_view);
        MessageThreadViewAdapter adapter = new MessageThreadViewAdapter(this, mails);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setStackFromEnd(true);
        recyclerView.setLayoutManager(layout);
    }

    private void initAddMessageButton() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = promptView.findViewById(R.id.editText);
        final DialogInterface.OnClickListener ON_SEND_MESSAGE = (dialog, id) -> {
            Intent sendMailService = new Intent(this, SendMailService.class);
            sendMailService.putExtra(SendMailService.TO, personViewModel.getSelectedPerson().getEmail());
            sendMailService.putExtra(SendMailService.MESSAGE, editText.getText().toString());
            startService(sendMailService);
        };

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Verstuur", ON_SEND_MESSAGE)
                .setNegativeButton("Teruggaan", (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();

        // create an alert dialog
        findViewById(R.id.add_mail).setOnClickListener(v -> {
            editText.setText("");
            alert.show();
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setText("Verstuur naar " + personViewModel.getSelectedPerson().getName());

            editText.requestFocus();

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
