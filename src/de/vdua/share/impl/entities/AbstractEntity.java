package de.vdua.share.impl.entities;

import java.util.HashMap;

/**
 * Created by postm on 17-Aug-16.
 */
public class AbstractEntity {

    public static final int STARTING_ID = 0;

    private static HashMap<Class<?>, Integer> NEXT_IDS = new HashMap<Class<?>, Integer>();

    protected static int getNextId(Class<?> subClass) {
        synchronized (NEXT_IDS) {
            if(!NEXT_IDS.containsKey(subClass)){
                NEXT_IDS.put(subClass, STARTING_ID);
            }
            Integer returnVal = NEXT_IDS.get(subClass);
            NEXT_IDS.put(subClass ,NEXT_IDS.get(subClass) + 1);
            return returnVal;
        }
    }
}
