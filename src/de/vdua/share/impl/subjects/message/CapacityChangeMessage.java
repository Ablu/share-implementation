package de.vdua.share.impl.subjects.message;

import de.vdua.share.impl.entities.StorageNode;

import java.util.HashMap;

public class CapacityChangeMessage {
    public HashMap<StorageNode, Double> newCapacities;
}
