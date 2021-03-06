package de.vdua.share.impl;

import de.vdua.share.impl.entities.DataEntity;
import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.interfaces.AbstractServer;
import de.vdua.share.impl.interfaces.DoubleHashable;
import de.vdua.share.impl.interfaces.IServer;
import de.vdua.share.impl.mappings.ConsistentHashMap;
import de.vdua.share.impl.mappings.FinalMappingFactory;
import de.vdua.share.impl.mappings.StorageNodeCHMFactory;

import java.util.*;

public class Server extends AbstractServer implements IServer {

    private final HashSet<StorageNode> storageNodes = new HashSet<StorageNode>();
    private double stretchFactor;

    private ConsistentHashMap<ConsistentHashMap<StorageNode>> nodeMapping;
    private HashMap<Integer, StorageNode> allStoredDataMappings = new HashMap<>();

    public Server(double stretchFactor) {
        this.stretchFactor = stretchFactor;
    }

    @Override
    public synchronized void changeCapacities(HashMap<StorageNode, Double> capacities) {
        if (capacities.isEmpty()) {
            return;
        }

        double totalCapacity = 0;
        for (Double d : capacities.values()) {
            totalCapacity += d;
        }

        if (totalCapacity == 0.0) {
            return;
        }

        final double frozenTotalCapacity = totalCapacity;

        capacities.forEach((storageNode, capacity) -> {
            storageNode.setCapacity(capacity / frozenTotalCapacity, getStretchFactor());
        });
        updateMapping();
        moveDataEntitiesAccordingToNewMapping();
    }

    public synchronized void registerStorageNode(StorageNode storageNode) {
        synchronized (storageNodes) {
            storageNodes.add(storageNode);
        }
    }

    public synchronized void unregisterStorageNode(StorageNode storageNode) {
        synchronized (storageNodes) {
            storageNodes.remove(storageNode);
        }
    }

    @Override
    public HashSet<StorageNode> getStorageNodes() {
        return storageNodes;
    }

    @Override
    public synchronized void setStretchFactor(double stretchFactor) {
        this.stretchFactor = stretchFactor;
        synchronized (storageNodes) {
            storageNodes.forEach(storageNode -> storageNode.updateInterval(stretchFactor));
        }
        updateMapping();
    }

    private void updateMapping() {
        boolean useVerification = false;
        FinalMappingFactory factory;
        synchronized (storageNodes) {
            factory = new FinalMappingFactory(new StorageNodeCHMFactory(this.storageNodes, useVerification), useVerification);
        }
        this.nodeMapping = factory.createConsistentHashMap();
    }

    @Override
    public double getStretchFactor() {
        return stretchFactor;
    }

    public synchronized void storeData(DataEntity entity) {
        StorageNode responsibleNode = this.nodeMapping.getElement(entity).getElement(entity);
        registerStorageLocation(entity, responsibleNode);
    }

    public void registerStorageLocation(DataEntity entity, StorageNode responsibleNode) {
        this.allStoredDataMappings.put(entity.getId(), responsibleNode);
    }

    public void unregisterStorageLocation(int dataId) {
        this.allStoredDataMappings.remove(dataId);
    }

    public StorageNode getStorageNodeResponsibleForStoring(DoubleHashable entity) {
        ConsistentHashMap<StorageNode> mappingToNode = this.nodeMapping.getElement(entity);
        if (mappingToNode != null)
            return mappingToNode.getElement(entity);
        else
            return null;
    }

    public StorageNode getStorageNodeResponsibleForStoring(int dataId) {
        DoubleHashable fakeDataEntity = new DoubleHashable() {
            @Override
            public int hashCode() {
                return dataId;
            }
        };
        return getStorageNodeResponsibleForStoring(fakeDataEntity);
    }

    private void moveDataEntitiesAccordingToNewMapping() {
        List<Integer> dataIdsToBeMigrated = new LinkedList<>();
        HashMap<Integer, StorageNode> newMapping = new HashMap<>();
        for (Integer dataEntityId : this.allStoredDataMappings.keySet()) {
            StorageNode oldResponsibleNode = this.allStoredDataMappings.get(dataEntityId);
            StorageNode newResponsibleNode = getStorageNodeResponsibleForStoring(dataEntityId);
            if (oldResponsibleNode != null && !oldResponsibleNode.equals(newResponsibleNode)) {
                dataIdsToBeMigrated.add(dataEntityId);
                newMapping.put(dataEntityId, newResponsibleNode);
            } else {
                newMapping.put(dataEntityId, oldResponsibleNode);
            }
        }
        for (Integer movingDataId : dataIdsToBeMigrated) {
            StorageNode oldResponsibleNode = this.allStoredDataMappings.get(movingDataId);
            StorageNode newResponsibleNode = newMapping.get(movingDataId);
            if (newResponsibleNode != null) {
                super.fireOnIssueMove(oldResponsibleNode.getId(), newResponsibleNode.getId(), movingDataId);
            }
        }
        this.allStoredDataMappings = newMapping;
    }

}
