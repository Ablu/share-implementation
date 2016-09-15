package de.vdua.share.impl.api.websocket;

import de.vdua.share.impl.api.interfaces.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.vdua.share.impl.interfaces.Server;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

public class WebsocketApi extends WebSocketServer implements Api {
    private Collection<WebSocket> clientConnections = new ArrayList<>();
    private Gson gson = new GsonBuilder().create();
    private Server server;

    public WebsocketApi(InetSocketAddress bindAddress, Server server) throws UnknownHostException {
        super(bindAddress);
        this.server = server;
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

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("on message: " + s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("on error");
    }
}
