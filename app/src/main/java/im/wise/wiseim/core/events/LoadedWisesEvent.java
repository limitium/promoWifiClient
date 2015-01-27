package im.wise.wiseim.core.events;

import java.util.ArrayList;
import java.util.Collection;

import im.wise.wiseim.core.models.Wise;

public class LoadedWisesEvent {
    Collection<Wise> wises;

    public LoadedWisesEvent(Collection<Wise> wises) {
        this.wises = wises;
    }

    public Collection<Wise> getWises() {
        return wises;
    }

    public void setWises(Collection<Wise> wises) {
        this.wises = wises;
    }
}
