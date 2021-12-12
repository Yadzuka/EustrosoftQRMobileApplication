package ru.eustrosoft.androidqr.model;

import java.util.UUID;


// TODO: use in future
public abstract class AbstractModel {
    private UUID uuid;

    public AbstractModel() {

    }

    public AbstractModel(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
