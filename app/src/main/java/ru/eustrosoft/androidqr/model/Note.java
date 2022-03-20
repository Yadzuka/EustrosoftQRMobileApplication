package ru.eustrosoft.androidqr.model;

import java.util.Date;
import java.util.UUID;

public class Note {
    private UUID id;
    private Date date;
    private Date time;
    private String title;
    private String text;

    public Note() {
        this(UUID.randomUUID());
    }

    public Note(UUID id) {
        this.id = id;
        date = new Date();
        time = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
