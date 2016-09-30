package de.vdua.share.impl;

import de.vdua.share.impl.entities.DataEntity;
import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.interfaces.AbstractServer;
import de.vdua.share.impl.interfaces.DoubleHashable;
import de.vdua.share.impl.interfaces.IServer;
import de.vdua.share.impl.interfaces.IServerListener;
import de.vdua.share.impl.mappings.ConsistentHashMap;
import de.vdua.share.impl.mappings.FinalMappingFactory;
import de.vdua.share.impl.mappings.StorageNodeCHMFactory;

import java.util.*;

/**
 * Created by postm on 17-Aug-16.
 */
public class Server extends AbstractServer implements IServer {

    private HashSet<StorageNode> storageNodes = new HashSet<StorageNode>();
    private double stretchFactor;

    private ConsistentHashMap<ConsistentHashMap<StorageNode>> nodeMapping;
    private HashMap<Integer, StorageNode> allStoredDataMappings = new HashMap<>();

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
        moveDataEntitiesAccordingToNewMapping();
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
    public synchronized void storeData(DataEntity entity) {
        StorageNode responsibleNode = this.nodeMapping.getElement(entity).getElement(entity);
        this.allStoredDataMappings.put(entity.getId(), responsibleNode);
        super.fireOnIssueStore(responsibleNode.getId(), entity.getId(), entity.getData());
    }

    public synchronized void deleteData(DataEntity entity) {
        StorageNode responsibleNode = getResponsibleNode(entity);
        this.allStoredDataMappings.remove(entity.getId());
        responsibleNode.getSubject().deleteData(entity);
        super.fireOnIssueDelete(responsibleNode.getId(), entity.getId());
    }

    private StorageNode getResponsibleNode(DataEntity entity) {
        return this.nodeMapping.getElement(entity).getElement(entity);
    }

    private StorageNode getResponsibleNode(Integer hashValue) {
        DoubleHashable fakeDataEntity = new DoubleHashable() {
            @Override
            public int hashCode() {
                return hashValue;
            }
        };
        return this.nodeMapping.getElement(fakeDataEntity).getElement(fakeDataEntity);
    }

    private void moveDataEntitiesAccordingToNewMapping() {
        List<Integer> dataIdsToBeMigrated = new ArrayList<>();
        HashMap<Integer, StorageNode> newMapping = new HashMap<>();
        synchronized (this.allStoredDataMappings) {
            for (Integer dataEntityId : this.allStoredDataMappings.keySet()) {
                StorageNode oldResponsibleNode = this.allStoredDataMappings.get(dataEntityId);
                StorageNode newResponsibleNode = getResponsibleNode(dataEntityId);
                if (!oldResponsibleNode.equals(newResponsibleNode)) {
                    dataIdsToBeMigrated.add(dataEntityId);
                    newMapping.put(dataEntityId, newResponsibleNode);
                } else {
                    newMapping.put(dataEntityId, oldResponsibleNode);
                }
            }
        }
        for (Integer movingDataId : dataIdsToBeMigrated) {
            StorageNode oldResponsibleNode = this.allStoredDataMappings.get(movingDataId);
            StorageNode newResponsibleNode = newMapping.get(movingDataId);
            super.fireOnIssueMove(oldResponsibleNode.getId(), newResponsibleNode.getId(), movingDataId);
        }
        this.allStoredDataMappings = newMapping;
    }

}
