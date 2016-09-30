package de.vdua.share.impl.interfaces;

import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.entities.DataEntity;

import java.util.HashMap;
import java.util.HashSet;

public interface IServer {
    void changeCapacities(HashMap<StorageNode, Double> capacities);

    StorageNode addStorageNode();

    HashSet<StorageNode> getStorageNodes();

    void setStretchFactor(double stretchFactor);

    double getStretchFactor();

    int getStorageNodeIdResponsibleForStoring(DataEntity entity);

    void addServerListener(IServerListener listener);

}
