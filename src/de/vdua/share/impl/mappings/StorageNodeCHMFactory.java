package de.vdua.share.impl.mappings;

import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.entities.Interval;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by postm on 06-Sep-16.
 */
public class StorageNodeCHMFactory extends ConsistentHashMapFactory<LinkedList<ResponsibilityIntervalStorageMapping>> {

    public StorageNodeCHMFactory(HashSet<StorageNode> storageNodesP, boolean useVerification) {
        super(useVerification);
        addStorageNodes(storageNodesP);
    }

    private void addStorageNodes(HashSet<StorageNode> storageNodesP) {
        HashSet<StorageNode> storageNodes = new HashSet<>(storageNodesP);
        //Determine bagIntervals
        TreeSet<Double> borders = genStorageNodeIntervalBorderSet(storageNodes);
        Interval[] bagIntervals = genBagIntervalsFromBorderSet(borders);
        LinkedList<ResponsibilityIntervalStorageMapping>[] bags = new LinkedList[bagIntervals.length];

        //Mapping of intervals to their represented bags
        for (StorageNode node : storageNodes) {
            for (Interval nodeInterval : node.getIntervals()) {
                SortedSet<Integer> allBagsWithinNodeIndeces = nodeInterval.getAllContainedIntervals(bagIntervals);
                allBagsWithinNodeIndeces.forEach(bagIndex -> {
                    if (bags[bagIndex] == null) {
                        bags[bagIndex] = new LinkedList<ResponsibilityIntervalStorageMapping>();
                    }
                    bags[bagIndex].add(new ResponsibilityIntervalStorageMapping(nodeInterval, node));
                });
            }
        }
        for (int i = 0; i < bagIntervals.length; i++) {
            super.addMapping(bagIntervals[i], bags[i]);
        }
    }

    @Override
    public boolean addMapping(Interval interval, LinkedList<ResponsibilityIntervalStorageMapping> element) {
        //Cant add mappings to this factory without using constructor
        return false;
    }

    private static TreeSet<Double> genStorageNodeIntervalBorderSet(HashSet<StorageNode> storageNodes) {
        TreeSet<Double> intervalBorders = new TreeSet<Double>((d1, d2) -> d1.compareTo(d2));
        for (StorageNode node : storageNodes) {
            for (Interval i : node.getIntervals()) {
                intervalBorders.add(i.getStart());
                intervalBorders.add(i.getEnd());
            }
        }
        return intervalBorders;
    }

}
