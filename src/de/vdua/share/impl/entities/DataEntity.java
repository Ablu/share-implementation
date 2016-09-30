package de.vdua.share.impl.entities;

import de.vdua.share.impl.interfaces.DoubleHashable;

/**
 * Created by postm on 17-Aug-16.
 */
public class DataEntity extends AbstractEntity implements DoubleHashable{

    private int id;
    private Object data;

    public DataEntity(Object data) {
        this.id = getNextId(DataEntity.class);
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public Object getData() {
        return data;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
