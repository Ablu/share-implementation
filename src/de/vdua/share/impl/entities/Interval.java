package de.vdua.share.impl.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by postm on 17-Aug-16.
 */
public class Interval {

    public static final Interval NULL_INSTANCE = new Interval(0.0, 0.0, false, false);

    private final double start;
    private final double end;
    private final boolean includeStart;
    private final boolean includeEnd;

    public Interval(double start, double end) {
        this.start = start;
        this.end = end;
        this.includeStart = true;
        this.includeEnd = false;
    }

    public Interval(double start, double end, boolean includeStart, boolean includeEnd) {
        this.start = start;
        this.end = end;
        this.includeStart = includeStart;
        this.includeEnd = includeEnd;
    }

    public boolean contains(double val) {
        return (val > start || (start == val && includeStart)) && (val < end || (end == val && includeEnd));
    }

    public boolean contains(double start, double end) {
        return contains(new Interval(start, end));
    }

    public boolean contains(Interval i) {
        return (i.getStart() > this.start || (this.start == i.getStart() && this.includeStart)) && (i.getEnd() < this.end || (this.end == i.getEnd() && this.includeEnd));
    }

    public boolean intersects(Interval i) {
        if (this.getStart() > i.getStart()) {
            return i.contains(this.getStart());
        } else if (this.getStart() < i.getStart()) {
            return this.contains(i.getStart());
        } else {
            //same start -> 100% interception
            return true;
        }

    }

    public SortedSet<Integer> getAllContainedIntervals(Interval[] probedIntervals) {
        TreeSet<Integer> contained = new TreeSet<Integer>();

        int mid = ((probedIntervals.length) / 2) + 1;//TODO maybe change the entrypoint for the search

        if (this.contains(probedIntervals[mid])) {
            //We hit an intrval within this interval.
            //-> consume intervals to both sides until we dont hit anymore
            contained.addAll(getAllIntervalInDirection(1, mid, probedIntervals));
            contained.addAll(getAllIntervalInDirection(-1, mid, probedIntervals));
        } else {
            //We didnt hit an interval within this interval.
            //-> probe to both sides until we hit in one direction
            //After that consume until we dont hit anymore
            int searchDirection = 1;
            Interval probe;
            int i = 1;
            for (; i <= mid - 1; i++) {
                probe = probedIntervals[mid + (i * searchDirection)];
                if (this.contains(probe)) {
                    break;
                } else {
                    searchDirection = -1;
                }
                probe = probedIntervals[mid + (i * searchDirection)];
                if (this.contains(probe)) {
                    break;
                } else {
                    searchDirection = 1;
                }
            }
            contained.addAll(getAllIntervalInDirection(searchDirection, mid + (i * searchDirection), probedIntervals));
        }

        return contained;
    }

    private Collection<Integer> getAllIntervalInDirection(int searchDirection, int start, Interval[] probedIntervals) {
        HashSet<Integer> contained = new HashSet<Integer>();
        for (int i = start; i >= 0 && i < probedIntervals.length; i = i + searchDirection) {
            if (this.contains(probedIntervals[i])) {
                contained.add(i);
            } else {
                break;
            }
        }
        return contained;
    }

    public double getStart() {
        return start;
    }


    public double getEnd() {
        return end;
    }


    public boolean isIncludeStart() {
        return includeStart;
    }


    public boolean isIncludeEnd() {
        return includeEnd;
    }

}
