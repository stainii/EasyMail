package be.stijnhooft.easymail.frontend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import be.stijnhooft.easymail.R;
import be.stijnhooft.easymail.backend.model.Person;
import be.stijnhooft.easymail.backend.service.SendMailService;

/**
 * Required intent extras:
 * * contact: the selected contact peron (type Person)
 */
public class SendMessageActivity extends AppCompatActivity {

    private Person selectedPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        this.selectedPerson = (Person) this.getIntent().getSerializableExtra("contact");
        ((TextView)(findViewById(R.id.selected_person_name))).setText(" " + selectedPerson.getName());

        final TextView editText = findViewById(R.id.editText);
        editText.setText("");
        editText.requestFocus();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent sendMailService = new Intent(this, SendMailService.class);
            sendMailService.putExtra(SendMailService.TO, selectedPerson.getEmail());
            sendMailService.putExtra(SendMailService.MESSAGE, editText.getText().toString());
            startService(sendMailService);

            finish();
        });
    }

}
