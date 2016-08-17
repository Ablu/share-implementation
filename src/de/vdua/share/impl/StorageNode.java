package de.vdua.share.impl;

import de.vdua.share.impl.entities.AbstractEntity;
import de.vdua.share.impl.entities.DataEntity;
import de.vdua.share.impl.entities.Interval;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by postm on 17-Aug-16.
 */
public class StorageNode extends AbstractEntity {

    private final int id;
    private double capacity;
    private Server server;
    private List<Interval> intervals;

    public StorageNode(double capacity, Server server) {
        this.id = getNextId(StorageNode.class);
        this.capacity = capacity;
        this.server = server;
        this.updateInterval();
    }

    private static List<Interval> devideInterval(Interval initialInterval) {
        LinkedList<Interval> devide = new LinkedList<Interval>();
        double end = initialInterval.getEnd();
        devide.add(new Interval(initialInterval.getStart(), Math.min(1.0, end)));
        end -= 1.0;
        while (end > 0) {
            devide.add(new Interval(0, Math.min(1.0, end)));
            end -= 1.0;
        }
        return devide;
    }

    public void storeData(DataEntity data) {
        System.out.println("StorageNode.storeData: id=" + id + " data={id=" + data.getId() + ", object=" + data.getData() + "}");
    }

    public void updateInterval() {
        devideInterval(genInterval(capacity, server.getStretchFactor()));
    }

    private Interval genInterval(double capacity, double stretchFactor) {
        double hash = this.hashCode() / Integer.MAX_VALUE;
        System.err.println("genInterval hash=" + hash);
        return new Interval(hash, hash + (server.getStretchFactor() * capacity));
    }

    public int getId() {
        return id;
    }

    public double getCapacity() {
        return capacity;
    }

    void setCapacity(double capacity) {
        this.capacity = capacity;
        this.updateInterval();
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(Interval initialInterval) {
        this.intervals = devideInterval(initialInterval);
    }
}
