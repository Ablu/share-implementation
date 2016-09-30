package de.vdua.share.impl;

import de.vdua.share.impl.entities.DataEntity;
import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.interfaces.AbstractServer;
import de.vdua.share.impl.interfaces.IServer;
import de.vdua.share.impl.mappings.ConsistentHashMap;
import de.vdua.share.impl.mappings.FinalMappingFactory;
import de.vdua.share.impl.mappings.StorageNodeCHMFactory;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by postm on 17-Aug-16.
 */
public class Server extends AbstractServer implements IServer {

    private HashSet<StorageNode> storageNodes = new HashSet<StorageNode>();
    private double stretchFactor;

    private ConsistentHashMap<ConsistentHashMap<StorageNode>> nodeMapping;

    public Server(double stretchFactor) {
        this.stretchFactor = stretchFactor;
    }

    //Expose manipulation methods

    @Override
    public synchronized void changeCapacities(HashMap<StorageNode, Double> capacities) {
        //Check invariant
        double totalCapacity = 0;
        for (Double d : capacities.values())
            totalCapacity += d;
        if (Math.abs(totalCapacity - 1.0) > 0.00001)
            throw new IllegalStateException("Expected capacity to sum up to 1! Capacity was: " + totalCapacity);
        //Update capacities
        capacities.forEach((storageNode, capacity) -> storageNode.setCapacity(capacity, getStretchFactor()));
        updateMapping();
    }

    @Override
    public synchronized StorageNode addStorageNode() {
        StorageNode newNode = new StorageNode(0.0, getStretchFactor());
        storageNodes.add(newNode);
        return newNode;
    }

    @Override
    public HashSet<StorageNode> getStorageNodes() {
        return storageNodes;
    }

    @Override
    public synchronized void setStretchFactor(double stretchFactor) {
        this.stretchFactor = stretchFactor;
        storageNodes.forEach(storageNode -> storageNode.updateInterval(stretchFactor));
        updateMapping();
    }

    private void updateMapping() {
        boolean useVerification = false;
        FinalMappingFactory factory = new FinalMappingFactory(new StorageNodeCHMFactory(this.storageNodes, useVerification), useVerification);
        this.nodeMapping = factory.createConsistentHashMap();



        fireOnMappingUpdate();
    }

    @Override
    public double getStretchFactor() {
        return stretchFactor;
    }

    @Override
    public void storeData(DataEntity entity) {
        StorageNode responsibleNode = this.nodeMapping.getElement(entity).getElement(entity);
        responsibleNode.getSubject().storeData(entity);
    }

    public void deleteData(DataEntity entity){
        StorageNode responsibleNode = this.nodeMapping.getElement(entity).getElement(entity);
        responsibleNode.getSubject().deleteData(entity);
    }


}
