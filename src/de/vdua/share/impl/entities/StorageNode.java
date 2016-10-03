package de.vdua.share.impl.entities;

import de.vdua.share.impl.interfaces.DoubleHashable;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by postm on 17-Aug-16.
 */
public class StorageNode extends AbstractEntity implements DoubleHashable {

    private final int id;
    private double capacity;
    private List<Interval> intervals = new ArrayList<>();

    public StorageNode(double capacity, double stretchFactor) {
        this.id = getNextId(StorageNode.class);
        this.capacity = capacity;
        this.updateInterval(stretchFactor);
    }

    private static List<Interval> devideInterval(Interval initialInterval) {
        List<Interval> devide = new ArrayList<>(10);
        BigDecimal end = new BigDecimal(initialInterval.getEnd());
        devide.add(new Interval(initialInterval.getStart(), Math.min(1.0, end.doubleValue())));
        end = end.subtract(new BigDecimal(1));
        end = end.setScale(9, BigDecimal.ROUND_HALF_UP);
        while (end.doubleValue() > 0) {
            devide.add(new Interval(0, Math.min(1.0, end.doubleValue())));
            end = end.subtract(new BigDecimal(1));
        }
        return devide;
    }

    public void updateInterval(double stretchFactor) {
        List<Interval> newIntervals = devideInterval(genInterval(capacity, stretchFactor));
        for (int i = 0; i < newIntervals.size() && i < this.intervals.size(); i++) {
            newIntervals.get(i).setHashCode(this.intervals.get(i).hashCode());
        }
        this.intervals = newIntervals;
    }

    private Interval genInterval(double capacity, double stretchFactor) {
        double hash = this.getHashAsDouble(); //TODO use other hashing mechanic
        double extendedHash = hash + (stretchFactor * capacity);
        System.err.println("genInterval(" + hash + ", " + extendedHash + " )");
        return new Interval(hash, extendedHash);
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
        return this.id;
    }
}
