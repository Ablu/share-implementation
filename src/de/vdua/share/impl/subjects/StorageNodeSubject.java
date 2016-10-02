package de.vdua.share.impl.subjects;

import de.vdua.share.impl.subjects.message.ConfirmJoinMessage;
import de.vdua.share.impl.subjects.message.ConfirmLeaveMessage;
import de.vdua.share.impl.subjects.message.DeleteDataFromNodeMessage;
import de.vdua.share.impl.subjects.message.StoreDataInNodeMessage;

import java.util.HashMap;

public class StorageNodeSubject extends Subject {

    private int nodeId;
    private final HashMap<Integer, Object> storedData = new HashMap<>();

    private ServerSubject serverSubject;

    @Override
    protected void init() {
    }

    @Override
    protected void onMessageReceived(Object message) {
        if (message instanceof StoreDataInNodeMessage) {
            StoreDataInNodeMessage storeMessage = (StoreDataInNodeMessage) message;
            storedData.put(storeMessage.dataId, storeMessage.data);
            emitChange();
        } else if (message instanceof ConfirmLeaveMessage) {
            if (!storedData.isEmpty()) {
                throw new IllegalStateException("still holding data while leave was confirmed!");
            }
            stopSubject();
        } else if (message instanceof ConfirmJoinMessage) {
            ConfirmJoinMessage confirmMessage = (ConfirmJoinMessage) message;
            nodeId = confirmMessage.nodeId;
            emitChange();
        } else if (message instanceof DeleteDataFromNodeMessage) {
            DeleteDataFromNodeMessage deleteMessage = (DeleteDataFromNodeMessage) message;
            storedData.remove(deleteMessage.dataId);
            emitChange();
        } else {
            throw new IllegalStateException("Unexpected message: " + message);
        }
    }

    @Override
    protected void onTimeout() {

    }

    public int getNodeId() {
        return nodeId;
    }

    public HashMap<Integer, Object> getStoredData() {
        return storedData;
    }
}
