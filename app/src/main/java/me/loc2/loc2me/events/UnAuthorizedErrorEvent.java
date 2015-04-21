package me.loc2.loc2me.events;

import java.io.Serializable;

public class UnAuthorizedErrorEvent {
    private Serializable cause;

    public UnAuthorizedErrorEvent(Serializable cause) {
        this.cause = cause;
    }

    public Serializable getCause() {
        return cause;
    }
}
