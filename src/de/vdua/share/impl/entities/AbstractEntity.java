package de.vdua.share.impl.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AbstractEntity {

    private static HashMap<Class<?>, List<Integer>> USED_IDS = new HashMap<>();

    protected static int getNextId(Class<?> subClass) {
        synchronized (USED_IDS) {
            if (!USED_IDS.containsKey(subClass)) {
                USED_IDS.put(subClass, new LinkedList<>());
            }
            Integer generatedId;
            synchronized (USED_IDS.get(subClass)) {
                List<Integer> usedIds = USED_IDS.get(subClass);
                do {
                    double gen = Math.random();
                    generatedId = (int) (gen * Integer.MAX_VALUE);
                } while (usedIds.contains(generatedId));
                usedIds.add(generatedId);
            }
            return generatedId;
        }
    }
}
