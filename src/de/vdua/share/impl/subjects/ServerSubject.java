package de.vdua.share.impl.subjects;

import de.vdua.share.impl.entities.DataEntity;
import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.interfaces.IServer;
import de.vdua.share.impl.subjects.message.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ServerSubject extends Subject {

    private IServer server;
    private Map<Integer, StorageNodeSubject> storageNodeSubjects = new HashMap<>();

    public ServerSubject(IServer server) {
        this.server = server;
        this.server.addServerListener((sourceStorageNodeId, targetStorageNodeId, dataId) -> {
            MoveMessage moveMessage = new MoveMessage();
            moveMessage.dataId = dataId;
            moveMessage.target = storageNodeSubjects.get(targetStorageNodeId);
            storageNodeSubjects.get(sourceStorageNodeId).send(moveMessage);
        });
    }

    @Override
    protected void init() {
    }

    @Override
    protected void onMessageReceived(Object message) {
        try {
            if (message instanceof StorageNodeJoinMessage) {
                StorageNodeJoinMessage storageNodeJoinMessage = (StorageNodeJoinMessage) message;

                boolean isFirstNode = server.getStorageNodes().isEmpty();
                final double initialCapacity = isFirstNode ? 1.0 : 0.0;

                StorageNode newNode = new StorageNode(initialCapacity, server.getStretchFactor());
                storageNodeSubjects.put(newNode.getId(), storageNodeJoinMessage.subject);

                final ConfirmJoinMessage joinMessage = new ConfirmJoinMessage();
                joinMessage.nodeId = newNode.getId();
                storageNodeJoinMessage.subject.send(joinMessage);

                HashMap<StorageNode, Double> newCapacities = new HashMap<>();
                for (StorageNode node : server.getStorageNodes()) {
                    newCapacities.put(node, node.getCapacity());
                }
                newCapacities.put(newNode, initialCapacity);

                server.registerStorageNode(newNode);
                server.changeCapacities(newCapacities);
                emitChange();
            } else if (message instanceof CapacityChangeMessage) {
                CapacityChangeMessage capacityChange = (CapacityChangeMessage) message;
                server.changeCapacities(capacityChange.newCapacities);
                emitChange();
            } else if (message instanceof StretchFactorUpdateMessage) {
                StretchFactorUpdateMessage stretchFactorUpdate = (StretchFactorUpdateMessage) message;
                server.setStretchFactor(stretchFactorUpdate.stretchFactor);
                emitChange();
            } else if (message instanceof StoreDataMessage) {
                StoreDataMessage storeData = (StoreDataMessage) message;
                DataEntity data = new DataEntity(storeData.data);
                StorageNode node = server.getStorageNodeResponsibleForStoring(data);

                StoreDataInNodeMessage storeDataInNodeMessage = new StoreDataInNodeMessage();
                storeDataInNodeMessage.dataId = data.getId();
                storeDataInNodeMessage.data = data.getData();
                storageNodeSubjects.get(node.getId()).send(storeDataInNodeMessage);

                server.registerStorageLocation(data, node);
            } else if (message instanceof DeleteMessage) {
                DeleteMessage deleteMessage = (DeleteMessage) message;
                StorageNode node = server.getStorageNodeResponsibleForStoring(deleteMessage.dataId);

                DeleteDataFromNodeMessage deleteDataFromNodeMessage = new DeleteDataFromNodeMessage();
                deleteDataFromNodeMessage.dataId = deleteMessage.dataId;
                storageNodeSubjects.get(node.getId()).send(deleteDataFromNodeMessage);

                server.unregisterStorageLocation(deleteMessage.dataId);
            } else if (message instanceof StorageNodeLeaveMessage) {
                StorageNodeLeaveMessage storageNodeLeave = (StorageNodeLeaveMessage) message;
                final int leavingNodeId = storageNodeLeave.nodeId;

                HashMap<StorageNode, Double> newCapacities = new HashMap<>();
                for (StorageNode node : server.getStorageNodes()) {
                    final double newCapacity = node.getId() == leavingNodeId ? 0.0 : node.getCapacity();
                    newCapacities.put(node, newCapacity);
                }
                server.changeCapacities(newCapacities);

                StorageNode nodeToDelete = null;
                for (StorageNode node : server.getStorageNodes()) {
                    if (node.getId() == leavingNodeId) {
                        nodeToDelete = node;
                    }
                }
                server.unregisterStorageNode(nodeToDelete);

                StorageNodeSubject leavingSubject = storageNodeSubjects.get(leavingNodeId);
                leavingSubject.send(new ConfirmLeaveMessage());

                storageNodeSubjects.remove(leavingNodeId);
                emitChange();
            } else {
                final String errorMessage = "Unexpected message: " + message;
                throw new IllegalStateException(errorMessage);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    @Override
    protected void onTimeout() {

    }

    public HashSet<StorageNode> getStorageNodes() {
        return server.getStorageNodes();
    }

    public double getStretchFactor() {
        return server.getStretchFactor();
    }
}
