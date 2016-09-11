package de.vdua.share.impl.mappings;

import de.vdua.share.impl.entities.Interval;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by postm on 06-Sep-16.
 */
public class ConsistentHashMapFactory<E> {

    private final boolean useVerification;

    private ArrayList<Interval> intervals;
    private ArrayList<E> mappedElements;

    public ConsistentHashMapFactory(boolean useVerification) {
        this.useVerification = useVerification;
        this.intervals = new ArrayList<>();
        this.mappedElements = new ArrayList<E>();
    }

    public ConsistentHashMap<E> createConsistentHashMap() {
        Interval[] intervals = this.intervals.toArray(new Interval[]{});
        E[] elements = (E[]) this.mappedElements.toArray(); //TODO check cast
        LinkedList<Integer>[] array = createArray(intervals);
        return new ConsistentHashMap<E>(intervals, elements, array);
    }

    private LinkedList<Integer>[] createArray(Interval[] intervals) {
        LinkedList<Integer>[] array = new LinkedList[intervals.length];
        double inverseSize = 1 / intervals.length;
        for (int i = 0; i < intervals.length; i++) {
            array[i] = new LinkedList<>();
            Interval arrayInterval = new Interval(i * inverseSize, (i + 1) * inverseSize);
            for (int j = 0; j < intervals.length; j++) {
                if (arrayInterval.contains(intervals[j])) {
                    array[i].add(j);
                }
            }
        }

        return array;
    }

    public boolean addMapping(Interval interval, E element) {
        if (useVerification) {
            for (int i = 0; i < intervals.size(); i++) {
                if (interval.intersects(intervals.get(i))) {
                    return false;
                }
            }
        }
        this.intervals.add(interval);
        this.mappedElements.add(element);
        return true;
    }


    protected static Interval[] genBagIntervalsFromBorderSet(Set<Double> borders) {
        double lastBorder = 0.0;
        //Assure that 0.0 is remove as lastBorder covers that side of the interval.
        borders.remove(lastBorder);
        //Assure that 1.0 is present so that side of the interval is covert.
        //borders is a Set so multiples are not an issue.
        borders.add(1.0);
        ArrayList<Interval> intervals = new ArrayList<Interval>(borders.size() + 1);
        for (Double nextBorder : borders) {
            intervals.add(new Interval(lastBorder, nextBorder));
            lastBorder = nextBorder;
        }
        return intervals.toArray(new Interval[]{});
    }
}
