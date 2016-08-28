package de.vdua.share.impl.main;

import de.vdua.share.impl.api.interfaces.Api;
import de.vdua.share.impl.api.websocket.WebsocketApi;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String args[]) throws UnknownHostException {
        InetSocketAddress address = new InetSocketAddress(9456);
        Api api = new WebsocketApi(address);
        api.initalize();
    }
}
