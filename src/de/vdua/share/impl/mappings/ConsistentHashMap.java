package de.vdua.share.impl.mappings;

import de.vdua.share.impl.entities.Interval;
import de.vdua.share.impl.interfaces.DoubleHashable;

import java.util.LinkedList;

/**
 * Created by postm on 06-Sep-16.
 */
public class ConsistentHashMap<E> {


    private Interval[] intervals;
    private E[] mappedElements;

    private double inverseSize;
    private LinkedList<Integer>[] array;

    ConsistentHashMap(Interval[] intervals, E[] mappedElements, LinkedList<Integer>[] array) {
        this.intervals = intervals;
        this.mappedElements = mappedElements;
        this.inverseSize = 1 / (double) mappedElements.length;
        this.array = array;
    }

    public int getSize() {
        return this.intervals.length;
    }

    public Interval getInterval(int index) {
        return this.intervals[index];
    }

    public E getElement(int index) {
        if(index == -1){
            return null;
        }
        return this.mappedElements[index];
    }

    public E getElement(DoubleHashable hashAble) {
        return getElement(getElementIndex(hashAble));
    }

    public E getElement(double hash) {
        return getElement(getElementIndex(hash));
    }

    public int getElementIndex(DoubleHashable hashAble) {
        return getElementIndex(hashAble.getHashAsDouble());
    }

    public int getElementIndex(double hash) {
        double aboveBorder = hash % this.inverseSize;
        double border = hash - aboveBorder;
        int borderIndex = (int) Math.round(border / this.inverseSize);
        if (borderIndex >= array.length)
            throw new IllegalStateException("borderIndex out of bounds");
        LinkedList<Integer> possibleIndices = this.array[borderIndex];
        for (Integer possibleIndex : possibleIndices) {
            if(intervals[possibleIndex].contains(hash)){
                return possibleIndex;
            }
        }
        throw new IllegalStateException("unable to find interval which includes hash position");
    }
}
