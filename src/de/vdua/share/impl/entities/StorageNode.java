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

    private List<DataEntity> storedData = new ArrayList<>();

    public StorageNode(double capacity, double stretchFactor) {
        this.id = getNextId(StorageNode.class);
        this.capacity = capacity;
        this.updateInterval(stretchFactor);
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

    public void storeData(DataEntity data) {
        System.out.print("StorageNode.storeData: id=" + id + " data={id=" + data.getId() + ", object=" + data.getData() + "}");
        if (!this.storedData.contains(data)) {
            this.storedData.add(data);
            System.out.println(" finished");
        } else {
            System.out.println(" failed");
        }
    }

    public void deleteData(DataEntity data) {
        System.out.println("StorageNode.deleteData: id=" + id + " data={id=" + data.getId() + ", object=" + data.getData() + "}");
        if (this.storedData.contains(data)) {
            this.storedData.remove(data);
            System.out.println(" finished");
        } else {
            System.out.println(" failed");
        }
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

    public List<DataEntity> getStoredData() {
        return Collections.unmodifiableList(this.storedData);
    }
    @Override
    public int hashCode() {
        return this.id;
    }
}
