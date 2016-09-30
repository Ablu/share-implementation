package de.vdua.share.impl.subjects;

import de.vdua.share.impl.entities.DataEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by postm on 30-Sep-16.
 */
public class StorageNodesSubject extends Subject {

    public static final String STORE_DATA = "getStorageNodeIdResponsibleForStoring";
    public static final String DELETE_DATA = "deleteData";

    @Override
    protected void init() {
        storedData = new HashMap<>();
    }

    @Override
    protected void onMessageReceived(Object message) {
        List<Object> splitMessage = (List<Object>) message;
        String methodName = (String) splitMessage.get(0);
        switch (methodName) {
            case STORE_DATA:
                storeData((DataEntity) splitMessage.get(1));
                break;
            case DELETE_DATA:
                deleteData((DataEntity) splitMessage.get(1));
                break;
            default:
                System.out.println("Cannot process methodName \"" + methodName + "\"");
        }
    }

    @Override
    protected void onTimeout() {

    }

    //This method is used for display purposes only - thus it does not use SubjectMessagingSystem
    public Map<Integer, DataEntity> getStoredData(){
        return Collections.unmodifiableMap(this.storedData);
    }

    public void storeData(DataEntity data) {
        System.out.print("StorageNode.getStorageNodeIdResponsibleForStoring: data={id=" + data.getId() + ", object=" + data.getData() + "}");
        if (!this.storedData.containsKey(data.getId())) {
            this.storedData.put(data.getId(), data);
            System.out.println(" finished");
        } else {
            System.out.println(" failed");
        }
    }

    public void deleteData(DataEntity data) {
        System.out.println("StorageNode.deleteData: data={id=" + data.getId() + ", object=" + data.getData() + "}");
        if (this.storedData.containsKey(data.getId())) {
            this.storedData.remove(data.getId());
            System.out.println(" finished");
        } else {
            System.out.println(" failed");
        }
    }
}
