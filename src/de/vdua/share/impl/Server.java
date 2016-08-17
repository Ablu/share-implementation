package de.vdua.share.impl;

import de.vdua.share.impl.entities.Interval;

import java.util.*;

/**
 * Created by postm on 17-Aug-16.
 */
public class Server {

    private HashSet<StorageNode> storageNodes = new HashSet<StorageNode>();
    private double stretchFactor;

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
        //Determine intervals
        ArrayList<Interval> intervals = genIntervals();


        return true;
    }

    private TreeSet<Double> genIntervalBorders() {
        TreeSet<Double> intervalBorders = new TreeSet<Double>((d1, d2) -> d1.compareTo(d2));
        for (StorageNode node : storageNodes) {
            for (Interval i : node.getIntervals()) {
                intervalBorders.add(i.getStart());
                intervalBorders.add(i.getEnd());
            }
        }
        return intervalBorders;
    }

    private ArrayList<Interval> genIntervals() {
        double lastBorder = 0;
        TreeSet<Double> borders =genIntervalBorders();
        //Assure that 0.0 is remove as lastBorder covers that side of the interval.
        borders.remove(0.0);
        //Assure that 1.0 is present so that side of the interval is covert.
        //borders is a Set so multiples are not an issue.
        borders.add(1.0);
        ArrayList<Interval> intervals = new ArrayList<Interval>(borders.size()+1);
        for (Double nextBorder : borders) {
            intervals.add(new Interval(lastBorder, nextBorder));
            lastBorder = nextBorder;
        }
        return intervals;
    }

    public synchronized StorageNode addStorageNode() {
        StorageNode newNode = new StorageNode(0.0, this);
        storageNodes.add(newNode);
        return newNode;
    }

    public synchronized void setStretchFactor(double stretchFactor) {
        this.stretchFactor = stretchFactor;
        storageNodes.forEach(storageNode -> storageNode.updateInterval());
    }

    public double getStretchFactor() {
        return stretchFactor;
    }


}
