package de.vdua.share.impl.interfaces;

public interface IServerListener {

    void onMappingUpdate(IServer eventServer);

    void onIssueStore(int storageNodeId, int dataId);
    void onIssueDelete(int storageNodeId, int dataId);
    void onIssueMove(int sourceStorageNode, int targetStorageNode, int dataId);
}
