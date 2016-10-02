package de.vdua.share.impl.main;

import de.vdua.share.impl.Server;
import de.vdua.share.impl.api.websocket.WebsocketApi;
import de.vdua.share.impl.interfaces.IServer;
import de.vdua.share.impl.subjects.ServerSubject;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String args[]) throws UnknownHostException {
        InetSocketAddress address = new InetSocketAddress(9456);
        final IServer server = new Server(2.0);
        final ServerSubject subject = new ServerSubject(server);
        subject.start();
        WebsocketApi api = new WebsocketApi(address, subject);
        api.initalize();
    }
}
