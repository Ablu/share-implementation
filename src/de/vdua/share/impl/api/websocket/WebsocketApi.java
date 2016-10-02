package de.vdua.share.impl.api.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import de.vdua.share.impl.api.interfaces.Api;
import de.vdua.share.impl.entities.StorageNode;
import de.vdua.share.impl.subjects.ServerSubject;
import de.vdua.share.impl.subjects.StorageNodeSubject;
import de.vdua.share.impl.subjects.message.*;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

public class WebsocketApi extends WebSocketServer implements Api {
    private Collection<WebSocket> clientConnections = new ArrayList<>();
    private Gson gson = new GsonBuilder().create();
    private ServerSubject serverSubject;

    private Set<StorageNodeSubject> storageNodeSubjects =
            Collections.newSetFromMap(new WeakHashMap<StorageNodeSubject, Boolean>());

    private class State {
        HashSet<StorageNode> storageNodes;
        double stretchFactor;

        State() {
            storageNodes = serverSubject.getStorageNodes();
            stretchFactor = serverSubject.getStretchFactor();
        }
    }

    private HashMap<String, Object> getState() {
        HashMap<String, Object> tree = new HashMap<>();
        tree.put("stretchFactor", serverSubject.getStretchFactor());
        ArrayList<HashMap<String, Object>> storageNodes = new ArrayList<>();

        for (StorageNode node : serverSubject.getStorageNodes()) {
            HashMap<String, Object> storageNodeObject = new HashMap<>();
            storageNodeObject.put("id", node.getId());
            storageNodeObject.put("capacity", node.getCapacity());
            storageNodeObject.put("intervals", node.getIntervals()); // bit hacky...

            ArrayList<HashMap<String, Object>> storedData = new ArrayList<>();
            for (StorageNodeSubject subject : storageNodeSubjects) {
                if (subject.getNodeId() == node.getId()) {
                    subject.getStoredData().forEach((id, value) -> {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("id", id);
                        data.put("data", value);
                        storedData.add(data);
                    });
                }
            }
            storageNodeObject.put("storedData", storedData);

            storageNodes.add(storageNodeObject);
        }
        tree.put("storageNodes", storageNodes);

        return tree;
    }

    public WebsocketApi(InetSocketAddress bindAddress, ServerSubject subject) throws UnknownHostException {
        super(bindAddress);
        serverSubject = subject;
        serverSubject.addChangeListener(this::sendUpdate);
    }

    private void sendUpdate() {
        broadcast(getState());
    }

    @Override
    public void initalize() {
        start();
    }

    private synchronized void broadcast(Object message) {
        synchronized (clientConnections) {
            for (WebSocket socket : clientConnections) {
                socket.send(gson.toJson(message));
            }
        }
    }

    @Override
    public synchronized void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        try {
            clientConnections.add(webSocket);
            broadcast(getState());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public synchronized void onClose(WebSocket webSocket, int i, String s, boolean b) {
        try {
            clientConnections.remove(webSocket);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private StorageNode getStorageNodeById(int id) {
        for (StorageNode node : serverSubject.getStorageNodes()) {
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
                    StorageNodeSubject newNode = new StorageNodeSubject();
                    newNode.addChangeListener(this::sendUpdate);
                    storageNodeSubjects.add(newNode);
                    newNode.start();

                    StorageNodeJoinMessage joinMessage = new StorageNodeJoinMessage();
                    joinMessage.subject = newNode;
                    serverSubject.send(joinMessage);
                    break;
                case "storeData":
                    String data = (String) message.get("data");
                    System.out.println("Storing data '" + data + "'!");
                    StoreDataMessage storeMessage = new StoreDataMessage();
                    storeMessage.data = data;
                    serverSubject.send(storeMessage);
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
                    CapacityChangeMessage capacityChangeMessage = new CapacityChangeMessage();
                    capacityChangeMessage.newCapacities = newCapacities;
                    serverSubject.send(capacityChangeMessage);
                    break;
                case "updateStretchFactor":
                    double stretchFactor = (double) message.get("factor");
                    StretchFactorUpdateMessage stretchFactorUpdateMessage = new StretchFactorUpdateMessage();
                    stretchFactorUpdateMessage.stretchFactor = stretchFactor;
                    serverSubject.send(stretchFactorUpdateMessage);
                    break;
                case "deleteStorageNode":
                    int idToDelete = ((Double) message.get("id")).intValue();
                    StorageNodeLeaveMessage storageNodeLeaveMessage = new StorageNodeLeaveMessage();
                    storageNodeLeaveMessage.nodeId = idToDelete;
                    serverSubject.send(storageNodeLeaveMessage);
                    break;
                default:
                    System.err.print("Received unknown command: ");
                    System.err.println(message);
                    break;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("on error");
    }
}
