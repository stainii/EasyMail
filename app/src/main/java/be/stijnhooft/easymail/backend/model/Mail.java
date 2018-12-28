package be.stijnhooft.easymail.backend.model;

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

    private boolean read;

    public Mail() {
    }

    public Mail(@NonNull Person sender, @NonNull String date, @NonNull String message, boolean incoming, boolean read) {
        this(sender.getEmail(), date, message, incoming, read);
    }

    public Mail(@NonNull String email, @NonNull String date, @NonNull String message, boolean incoming, boolean read) {
        this.email = email;
        this.date = date;
        this.message = message;
        this.incoming = incoming;
        this.read = read;
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

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
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
