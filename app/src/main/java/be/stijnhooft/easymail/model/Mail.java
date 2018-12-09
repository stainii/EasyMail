package be.stijnhooft.easymail.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Mail implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String email;

    @NonNull
    private String date;

    @NonNull
    private String message;

    private boolean incoming;

    public Mail() {
    }

    public Mail(Person sender, String date, String message, boolean incoming) {
        this.email = sender.getEmail();
        this.date = date;
        this.message = message;
        this.incoming = incoming;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return id == mail.id &&
                incoming == mail.incoming &&
                Objects.equals(email, mail.email) &&
                Objects.equals(date, mail.date) &&
                Objects.equals(message, mail.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, date, message, incoming);
    }

    @Override
    public String toString() {
        return "Mail{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                ", incoming=" + incoming +
                '}';
    }
}
