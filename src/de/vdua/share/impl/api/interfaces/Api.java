package de.vdua.share.impl.api.interfaces;

import de.vdua.share.impl.entities.StateEntity;

public interface Api {
    void initalize();

    void notifyClientsAboutUpdate(StateEntity state);
}
