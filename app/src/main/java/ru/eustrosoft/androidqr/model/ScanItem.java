package ru.eustrosoft.androidqr.model;

import java.util.Date;
import java.util.UUID;

public class ScanItem {

    private UUID id;
    private Date date;
    private Date time;
    private String text;

    public ScanItem() {
        this(UUID.randomUUID());
    }

    public ScanItem(UUID id) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
