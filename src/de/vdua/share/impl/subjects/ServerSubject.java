package de.vdua.share.impl.subjects;

import de.vdua.share.impl.interfaces.IServer;
import de.vdua.share.impl.interfaces.IServerListener;
import de.vdua.share.impl.subjects.message.DeleteMessage;
import de.vdua.share.impl.subjects.message.MoveMessage;
import de.vdua.share.impl.subjects.message.StorageNodeIntroductionMessage;
import de.vdua.share.impl.subjects.message.StoreMessage;

import java.util.HashMap;
import java.util.Map;

public class ServerSubject extends Subject {

    private IServer server;
    private Map<Integer, StorageNodesSubject> storageNodeSubjects = new HashMap<>();

    public ServerSubject(IServer server) {
        this.server = server;
        this.server.addServerListener(new IServerListener() {
            @Override
            public void onMappingUpdate(IServer eventServer) {
            }

            @Override
            public void onIssueStore(int storageNodeId, int dataId, Object data) {
                StoreMessage storeMessage = new StoreMessage();
                storeMessage.dataId = dataId;
                storeMessage.data = data;
                storageNodeSubjects.get(storageNodeId).send(storeMessage);
            }

            @Override
            public void onIssueDelete(int storageNodeId, int dataId) {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.dataId = dataId;
                storageNodeSubjects.get(storageNodeId).send(deleteMessage);
            }

            @Override
            public void onIssueMove(int sourceStorageNodeId, int targetStorageNodeId, int dataId) {
                MoveMessage moveMessage = new MoveMessage();
                moveMessage.dataId = dataId;
                moveMessage.target = storageNodeSubjects.get(targetStorageNodeId);
                storageNodeSubjects.get(sourceStorageNodeId).send(moveMessage);
            }
        });
    }

    @Override
    protected void init() {
    }

    @Override
    protected void onMessageReceived(Object message) {
        if (message instanceof StorageNodeIntroductionMessage) {
            StorageNodeIntroductionMessage storageNodeIntroductionMessage = (StorageNodeIntroductionMessage) message;
            storageNodeSubjects.put(storageNodeIntroductionMessage.id, storageNodeIntroductionMessage.subject);
        }
    }

    @Override
    protected void onTimeout() {

    }
}
