package de.vdua.share.impl.interfaces;

public interface IServerListener {

    void onMappingUpdate();

    void onIssueStore(int storageNodeId, int dataId, Object storedObject);

    void onIssueDelete(int storageNodeId, int dataId);

    void onIssueMove(int sourceStorageNode, int targetStorageNode, int dataId);

}
