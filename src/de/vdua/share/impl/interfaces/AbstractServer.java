package de.vdua.share.impl.interfaces;

import java.util.LinkedList;

public abstract class AbstractServer implements IServer {

    private LinkedList<IServerListener> listeners = new LinkedList<>();

    @Override
    public void addServerListener(IServerListener listener) {
        if (!this.listeners.contains(listener))
            this.listeners.add(listener);
    }

    protected final void fireOnIssueMove(int sourceStorageNode, int targetStorageNode, int dataId) {
        for (IServerListener listener : this.listeners) {
            listener.onIssueMove(sourceStorageNode, targetStorageNode, dataId);
        }
    }
}
