package de.vdua.share.impl.entities;

import java.util.*;

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
        this.includeEnd = true;
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

    public boolean contains(Interval i) {
        return (i.getStart() > this.start || (this.start == i.getStart() && this.includeStart)) && (i.getEnd() < this.end || (this.end == i.getEnd() && this.includeEnd));
    }

    public Collection<Interval> getAllContainedIntervals(ArrayList<Interval> probedIntervals) {
        HashSet<Interval> contained = new HashSet<Interval>();

        int mid = (probedIntervals.size() / 2) + 1;//TODO maybe change the entrypoint for the search

        if (this.contains(probedIntervals.get(mid))) {
            //We hit an intrval within this interval.
            //-> consume intervals to both sides until we dont hit anymore
            contained.addAll(getAllIntervalInDirection(1, mid, probedIntervals));
            contained.addAll(getAllIntervalInDirection(-1, mid, probedIntervals));
        } else {
            //We didnt hit an intrval within this interval.
            //-> probe to both sides until we hit in one direction
            //After that consume until we dont hit anymore
            int searchDirection = 1;
            Interval probe;
            int i = 1;
            for (; i <= mid - 1; i++) {
                probe = probedIntervals.get(mid + (i * searchDirection));
                if (this.contains(probe)) {
                    break;
                } else {
                    searchDirection = -1;
                }
                probe = probedIntervals.get(mid + (i * searchDirection));
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

    private Collection<Interval> getAllIntervalInDirection(int searchDirection, int start, ArrayList<Interval> probedIntervals) {
        HashSet<Interval> contained = new HashSet<Interval>();
        for (int i = start; i >= 0 && i < probedIntervals.size(); i = i + searchDirection) {
            Interval probe = probedIntervals.get(i);
            if (this.contains(probe)) {
                contained.add(probe);
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
