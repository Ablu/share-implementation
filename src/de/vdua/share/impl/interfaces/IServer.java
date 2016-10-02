package de.vdua.share.impl.interfaces;

import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.entities.DataEntity;

import java.util.HashMap;
import java.util.HashSet;

public interface IServer {
    void changeCapacities(HashMap<StorageNode, Double> capacities);

    void setStretchFactor(double stretchFactor);
    double getStretchFactor();

    void registerStorageNode(StorageNode storageNode);
    void unregisterStorageNode(StorageNode storageNode);
    HashSet<StorageNode> getStorageNodes();
    StorageNode getStorageNodeResponsibleForStoring(DataEntity entity);
    StorageNode getStorageNodeResponsibleForStoring(int dataId);

    void registerStorageLocation(DataEntity entity, StorageNode responsibleNode);
    void unregisterStorageLocation(DataEntity entity);

    void addServerListener(IServerListener listener);
}
