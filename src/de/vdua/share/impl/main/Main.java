package de.vdua.share.impl.main;

import de.vdua.share.impl.api.interfaces.Api;
import de.vdua.share.impl.api.websocket.WebsocketApi;
import de.vdua.share.impl.interfaces.Server;
import de.vdua.share.impl.mock.FakeServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String args[]) throws UnknownHostException {
        InetSocketAddress address = new InetSocketAddress(9456);
        Server server = new FakeServer();
        Api api = new WebsocketApi(address, server);
        api.initalize();
    }
}
