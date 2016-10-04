package de.vdua.share.impl.entities;

public class Tuple<T,E> {
    private T t1;
    private E t2;

    public Tuple(T t1, E t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T getT1() {
        return t1;
    }

    public E getT2() {
        return t2;
    }
}
