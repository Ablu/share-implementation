package de.vdua.share.impl.subjects;

import de.vdua.share.impl.subjects.message.*;

import java.util.HashMap;

public class StorageNodeSubject extends Subject {

    private int nodeId;
    private final HashMap<Integer, Object> storedData = new HashMap<>();

    @Override
    protected void init() {
    }

    @Override
    protected void onMessageReceived(Object message) {
        try {
            if (message instanceof StoreDataInNodeMessage) {
                StoreDataInNodeMessage storeMessage = (StoreDataInNodeMessage) message;
                synchronized (storedData) {
                    storedData.put(storeMessage.dataId, storeMessage.data);
                }
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
                synchronized (storedData) {
                    storedData.remove(deleteMessage.dataId);
                }
                emitChange();
            } else if (message instanceof MoveMessage) {
                MoveMessage moveMessage = (MoveMessage) message;

                DataForwardMessage dataForwardMessage = new DataForwardMessage();
                dataForwardMessage.dataId = moveMessage.dataId;
                dataForwardMessage.data = storedData.remove(moveMessage.dataId);
                moveMessage.target.send(dataForwardMessage);
                emitChange();
            } else if (message instanceof DataForwardMessage) {
                DataForwardMessage forwardMessage = (DataForwardMessage) message;
                synchronized (storedData) {
                    storedData.put(forwardMessage.dataId, forwardMessage.data);
                }
                emitChange();
            } else {
                throw new IllegalStateException("Unexpected message: " + message);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
