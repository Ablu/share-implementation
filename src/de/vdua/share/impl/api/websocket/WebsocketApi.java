package de.vdua.share.impl.api.websocket;

import com.google.gson.internal.LinkedTreeMap;
import de.vdua.share.impl.api.interfaces.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.vdua.share.impl.entities.DataEntity;
import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.interfaces.IServer;
import de.vdua.share.impl.interfaces.IServerListener;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class WebsocketApi extends WebSocketServer implements Api {
    private Collection<WebSocket> clientConnections = new ArrayList<>();
    private Gson gson = new GsonBuilder().create();
    private IServer server;

    public WebsocketApi(InetSocketAddress bindAddress, IServer server) throws UnknownHostException {
        super(bindAddress);
        this.server = server;
        server.addServerListener(eventServer -> sendUpdate());
    }

    private void sendUpdate() {
        broadcast(server.getStorageNodes());
    }

    @Override
    public void initalize() {
        start();
    }

    private void broadcast(Object message) {
        for (WebSocket socket : clientConnections) {
            socket.send(gson.toJson(message));
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        try {
            System.out.println("on open");
            clientConnections.add(webSocket);
            webSocket.send(gson.toJson(server.getStorageNodes()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        try {
            System.out.println("on close");
            clientConnections.remove(webSocket);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private StorageNode getStorageNodeById(int id) {
        for (StorageNode node : server.getStorageNodes()) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        try {
            LinkedTreeMap message = (LinkedTreeMap) gson.fromJson(s, Object.class);
            final String command = (String) message.get("command");

            switch (command) {
                case "addStorageNode":
                    System.out.println("Adding storage node!");
                    server.addStorageNode();
                    break;
                case "storeData":
                    String data = (String) message.get("data");
                    System.out.println("Storing data '" + data + "'!");
                    server.storeData(new DataEntity(data));
                    break;
                case "updateCapacities":
                    ArrayList<LinkedTreeMap> capacities = (ArrayList<LinkedTreeMap>) message.get("capacities");
                    System.out.println("Updating capacities: " + capacities);

                    HashMap<StorageNode, Double> newCapacities = new HashMap<>();
                    for (LinkedTreeMap capacity : capacities) {
                        int id = ((Double) capacity.get("id")).intValue();
                        double newCapacity = (double) capacity.get("capacity");
                        newCapacities.put(getStorageNodeById(id), newCapacity);
                    }
                    server.changeCapacities(newCapacities);
                    break;
            }

            sendUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("on error");
    }
}
