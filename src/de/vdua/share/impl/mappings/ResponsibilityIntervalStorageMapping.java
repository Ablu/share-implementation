package de.vdua.share.impl.mappings;

import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.entities.Interval;
import de.vdua.share.impl.entities.Tuple;
import de.vdua.share.impl.interfaces.DoubleHashable;

public class ResponsibilityIntervalStorageMapping extends Tuple<Interval, StorageNode> implements DoubleHashable{

    public ResponsibilityIntervalStorageMapping(Interval t1, StorageNode t2) {
        super(t1, t2);
    }

    @Override
    public int hashCode() {
        return getT1().hashCode();
    }
}
