package im.wise.wiseim.core.events;

import im.wise.wiseim.core.models.Wise;

public class NewWiseEvent {
    private Wise wise;

    public NewWiseEvent(Wise wise) {
        this.wise = wise;
    }

    public Wise getWise() {
        return wise;
    }

    public void setWise(Wise wise) {
        this.wise = wise;
    }
}
