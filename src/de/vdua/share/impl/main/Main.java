package de.vdua.share.impl.main;

import de.vdua.share.impl.Server;
import de.vdua.share.impl.api.interfaces.Api;
import de.vdua.share.impl.api.websocket.WebsocketApi;
import de.vdua.share.impl.entities.DataEntity;
import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.interfaces.IServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String args[]) throws UnknownHostException {
        InetSocketAddress address = new InetSocketAddress(9456);
        final IServer server = new Server(2.0);
        Api api = new WebsocketApi(address, server);
        api.initalize();
        final StorageNode storageNode1 = server.addStorageNode();
        HashMap<StorageNode, Double> capacities = new HashMap<>();
        capacities.put(storageNode1, 1.0);
        server.changeCapacities(capacities);

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Add new node.");
                StorageNode storageNode2 = server.addStorageNode();
                HashMap<StorageNode, Double> capacities = new HashMap<>();
                capacities.put(storageNode1, 0.5);
                capacities.put(storageNode2, 0.5);
                server.changeCapacities(capacities);
            }
        }.start();
    }
}
