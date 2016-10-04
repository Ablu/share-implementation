package de.vdua.share.impl.interfaces;

public interface DoubleHashable {

    default double getHashAsDouble() {
        return truncateDouble(((double) this.hashCode()) / Integer.MAX_VALUE);
    }

    default double truncateDouble(double d){
        double multiplicative = 1000000000.0;
        double d2 = d * multiplicative;
        int casted = (int) d2;
        return casted / multiplicative;
    }
}
