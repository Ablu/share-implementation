package de.vdua.share.impl.interfaces;

import java.util.LinkedList;

/**
 * Created by postm on 18-Sep-16.
 */
public abstract class AbstractServer implements IServer{

    private LinkedList<IServerListener> listeners = new LinkedList<>();

    @Override
    public void addServerListener(IServerListener listener) {
        if(!this.listeners.contains(listener))
            this.listeners.add(listener);
    }

    protected void fireOnMappingUpdate(){
        for(IServerListener listener : this.listeners){
            listener.onMappingUpdate(this);
        }
    }
}
