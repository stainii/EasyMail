package be.stijnhooft.easymail.backend.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Person implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String email;
    private int image;
    private boolean hasNewMessages;

    public Person(String name, String email, int image) {
        this.name = name;
        this.email = email;
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(email, person.email) &&
                Objects.equals(image, person.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, image);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean hasNewMessages() {
        return hasNewMessages;
    }

    public void setNewMessages(boolean hasNewMessages) {
        this.hasNewMessages = hasNewMessages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
