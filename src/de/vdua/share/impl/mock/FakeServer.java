package de.vdua.share.impl.mock;

import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.entities.DataEntity;
import de.vdua.share.impl.interfaces.AbstractServer;
import de.vdua.share.impl.interfaces.IServer;

import java.util.HashMap;
import java.util.HashSet;

public class FakeServer extends AbstractServer implements IServer {
    private final HashSet<StorageNode> storageNodes;

    public FakeServer() {
        storageNodes = new HashSet<>();
        storageNodes.add(new StorageNode(0.5, getStretchFactor()));
        storageNodes.add(new StorageNode(0.5, getStretchFactor()));
    }

    @Override
    public void changeCapacities(HashMap<StorageNode, Double> capacities) {

    }

    @Override
    public StorageNode addStorageNode() {
        return null;
    }

    @Override
    public HashSet<StorageNode> getStorageNodes() {
        return storageNodes;
    }

    @Override
    public void setStretchFactor(double stretchFactor) {

    }

    @Override
    public double getStretchFactor() {
        return 0.5;
    }

    @Override
    public void storeData(DataEntity entity) {

    }
}
