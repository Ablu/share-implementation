package de.vdua.share.impl.interfaces;

public interface IServerListener {
    void onIssueMove(int sourceStorageNodeId, int targetStorageNodeId, int dataId);
}