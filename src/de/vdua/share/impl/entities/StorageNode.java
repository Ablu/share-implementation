package de.vdua.share.impl.entities;

import de.vdua.share.impl.interfaces.Server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by postm on 17-Aug-16.
 */
public class StorageNode extends AbstractEntity {

    private final int id;
    private double capacity;
    private List<Interval> intervals = new ArrayList<>();

    public StorageNode(double capacity, double stretchFactor) {
        this.id = getNextId(StorageNode.class);
        this.capacity = capacity;
        this.updateInterval(stretchFactor);
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

    public void updateInterval(double stretchFactor) {
        this.intervals = devideInterval(genInterval(capacity, stretchFactor));
    }

    private Interval genInterval(double capacity, double stretchFactor) {
        double hash = (double) this.hashCode() / Integer.MAX_VALUE; //TODO use other hashing mechanic
        System.err.println("genInterval hash=" + hash);
        return new Interval(hash, hash + (stretchFactor * capacity));
    }

    public int getId() {
        return id;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity, double stretchFactor) {
        this.capacity = capacity;
        this.updateInterval(stretchFactor);
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(Interval initialInterval) {
        this.intervals = devideInterval(initialInterval);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
