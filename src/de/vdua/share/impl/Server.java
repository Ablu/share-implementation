package de.vdua.share.impl;

import de.vdua.share.impl.entities.DataEntity;
import de.vdua.share.impl.mappings.ConsistentHashMap;
import de.vdua.share.impl.mappings.FinalMappingFactory;
import de.vdua.share.impl.mappings.StorageNodeCHMFactory;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by postm on 17-Aug-16.
 */
public class Server {

    private HashSet<StorageNode> storageNodes = new HashSet<StorageNode>();
    private double stretchFactor;

    private ConsistentHashMap<ConsistentHashMap<StorageNode>> nodeMapping;

    public Server(double stretchFactor) {
        this.stretchFactor = stretchFactor;
    }

    //Expose manipulation methods

    public synchronized boolean changeCapacities(HashMap<StorageNode, Double> capacities) {
        //Check invariant
        double totalCapacity = 0;
        for (Double d : capacities.values())
            totalCapacity += d;
        if (totalCapacity != 1.0)
            return false;
        //Update capacities
        capacities.forEach((storageNode, capacity) -> storageNode.setCapacity(capacity));
        updateMapping();
        return true;
    }

    public synchronized StorageNode addStorageNode() {
        StorageNode newNode = new StorageNode(0.0, this);
        storageNodes.add(newNode);
        return newNode;
    }

    public synchronized void setStretchFactor(double stretchFactor) {
        this.stretchFactor = stretchFactor;
        storageNodes.forEach(storageNode -> storageNode.updateInterval());
        updateMapping();
    }

    private void updateMapping() {
        boolean useVerification = false;
        FinalMappingFactory factory = new FinalMappingFactory(new StorageNodeCHMFactory(this.storageNodes, useVerification), useVerification);
        this.nodeMapping = factory.createConsistentHashMap();
    }

    public double getStretchFactor() {
        return stretchFactor;
    }

    public void storeData(DataEntity entity) {
        StorageNode responsibleNode = this.nodeMapping.getElement(entity).getElement(entity);
        responsibleNode.storeData(entity);
    }

}
