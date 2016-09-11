package de.vdua.share.impl.mappings;

import de.vdua.share.impl.entities.Interval;

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
        this.inverseSize = 1 / mappedElements.length;
        this.array = array;
    }

    public int getSize() {
        return this.intervals.length;
    }

    public Interval getInterval(int index) {
        return this.intervals[index];
    }

    public E getElement(int index) {
        return this.mappedElements[index];
    }

    public E getElement(Object hashAble) {
        return getElement(getElementIndex(hashAble));
    }

    public int getElementIndex(Object hashAble) {
        return getElementIndex(hashAble.hashCode() / Integer.MAX_VALUE);
    }

    public int getElementIndex(double hash) {
        double aboveBorder = hash % this.inverseSize;
        double border = hash - aboveBorder;
        int borderIndex = (int) (border / this.inverseSize);
        if (borderIndex >= array.length)
            return -1;
        LinkedList<Integer> possibleIndices = this.array[borderIndex];
        for (Integer possibleIndex : possibleIndices) {
            if(intervals[possibleIndex].contains(hash)){
                return possibleIndex;
            }
        }
        return -1;
    }
}
