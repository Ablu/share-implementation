package de.vdua.share.impl.interfaces;

import java.util.LinkedList;

/**
 * Created by postm on 18-Sep-16.
 */
public abstract class AbstractServer implements IServer {

    private LinkedList<IServerListener> listeners = new LinkedList<>();

    @Override
    public void addServerListener(IServerListener listener) {
        if (!this.listeners.contains(listener))
            this.listeners.add(listener);
    }

    protected final void fireOnMappingUpdate() {
        for (IServerListener listener : this.listeners) {
            listener.onMappingUpdate();
        }
    }


    protected final void fireOnIssueMove(int sourceStorageNode, int targetStorageNode, int dataId) {
        for (IServerListener listener : this.listeners) {
            listener.onIssueMove(sourceStorageNode, targetStorageNode, dataId);
        }
    }


    protected final void fireOnIssueDelete(int storageNodeId, int dataId) {
        for (IServerListener listener : this.listeners) {
            listener.onIssueDelete(storageNodeId, dataId);
        }
    }

    protected final void fireOnIssueStore(int storageNodeId, int dataId, Object storedObject) {
        for (IServerListener listener : this.listeners) {
            listener.onIssueStore(storageNodeId, dataId, storedObject);
        }
    }

}
