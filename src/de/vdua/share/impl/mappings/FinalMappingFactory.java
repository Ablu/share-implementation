package de.vdua.share.impl.mappings;

import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.entities.Interval;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Created by postm on 06-Sep-16.
 */
public class FinalMappingFactory extends ConsistentHashMapFactory<ConsistentHashMap<StorageNode>> {

    public FinalMappingFactory(StorageNodeCHMFactory firstMapping, boolean useVerification) {
        super(useVerification);
        this.finalizeMapping(firstMapping.createConsistentHashMap(), useVerification);
    }

    @Override
    public boolean addMapping(Interval interval, ConsistentHashMap<StorageNode> element) {
        return false;
    }

    private void finalizeMapping(ConsistentHashMap<LinkedList<ResponsibilityIntervalStorageMapping>> firstMapping, boolean useVerification) {
        for (int i = 0; i < firstMapping.getSize(); i++) {
            Interval interval = firstMapping.getInterval(i);
            LinkedList<ResponsibilityIntervalStorageMapping> possibleNodes = firstMapping.getElement(i);

            if(possibleNodes == null){
                int test = 0;
                test++;
            }

            //Create second level intervals
            StorageNode[] finalMappedNodes = new StorageNode[possibleNodes.size() + 1];
            TreeSet<Double> secondLvlIntervalBorder = new TreeSet<>((d1, d2) -> d1.compareTo(d2));
            for (ResponsibilityIntervalStorageMapping possibleNodeMapping : possibleNodes) {
                secondLvlIntervalBorder.add(possibleNodeMapping.getHashAsDouble());
            }
            //Map intervals to nodes
            Interval[] secondLvlIntervals = genBagIntervalsFromBorderSet(secondLvlIntervalBorder);
            for (ResponsibilityIntervalStorageMapping possibleNodeMapping : possibleNodes) {
                for (int j = 0; j < secondLvlIntervals.length; j++) {
                    if (secondLvlIntervals[j].contains(possibleNodeMapping.getHashAsDouble())) {
                        finalMappedNodes[j] = possibleNodeMapping.getT2();
                    }
                }
            }
            //First interval is not mapped
            //-> add mapping to first interval
            finalMappedNodes[0] = finalMappedNodes[finalMappedNodes.length - 1];
            //Construct final mapping
            ConsistentHashMapFactory<StorageNode> secondMappingFactory = new ConsistentHashMapFactory<>(useVerification);

            for (int j = 0; j < secondLvlIntervals.length; j++) {
                secondMappingFactory.addMapping(secondLvlIntervals[j], finalMappedNodes[j]);
            }
            //Map final mapping
            super.addMapping(interval, secondMappingFactory.createConsistentHashMap());
        }
    }
}
