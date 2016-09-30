package de.vdua.share.impl.entities;

import de.vdua.share.impl.interfaces.DoubleHashable;
import de.vdua.share.impl.subjects.StorageNodesSubject;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by postm on 17-Aug-16.
 */
public class StorageNode extends AbstractEntity implements DoubleHashable {

    private final int id;
    private double capacity;
    private List<Interval> intervals = new ArrayList<>();

    private StorageNodesSubject subject;

    public StorageNode(double capacity, double stretchFactor) {
        this(capacity, stretchFactor, new StorageNodesSubject());
    }

    public StorageNode(double capacity, double stretchFactor, StorageNodesSubject subject) {
        this.id = getNextId(StorageNode.class);
        this.capacity = capacity;
        this.updateInterval(stretchFactor);
        this.subject = subject;
    }

    private static List<Interval> devideInterval(Interval initialInterval) {
        LinkedList<Interval> devide = new LinkedList<Interval>();
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
        this.intervals = devideInterval(genInterval(capacity, stretchFactor));
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

    public Map<Integer, DataEntity> getStoredData() {
        return this.subject.getStoredData();
    }

    public Collection<DataEntity> getStoredDataEntities() {
        return this.subject.getStoredData().values();
    }

    public StorageNodesSubject getSubject(){
        return subject;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
