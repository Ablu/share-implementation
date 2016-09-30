package de.vdua.share.impl.interfaces;

public interface IServerListener {

    void onMappingUpdate();

    void onIssueStore(int storageNodeId, int dataId, Object data);

    void onIssueDelete(int storageNodeId, int dataId);

    void onIssueMove(int sourceStorageNodeId, int targetStorageNodeId, int dataId);

}
