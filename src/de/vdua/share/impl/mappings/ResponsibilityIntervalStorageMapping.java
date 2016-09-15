package de.vdua.share.impl.mappings;

import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.entities.Interval;
import de.vdua.share.impl.entities.Tuple;

/**
 * Created by postm on 18-Aug-16.
 */
public class ResponsibilityIntervalStorageMapping extends Tuple<Interval, StorageNode> {

    public ResponsibilityIntervalStorageMapping(Interval t1, StorageNode t2) {
        super(t1, t2);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
