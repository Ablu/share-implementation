import { Injectable } from '@angular/core';
import {StorageNode} from "./storagenode";

@Injectable()
export class ShareService {
    private websocket : WebSocket;

    constructor() {
        this.websocket = new WebSocket('ws://localhost:9456/');
        this.websocket.addEventListener('message', this.onMessage);
    }

    getStorageNodes(): Promise<StorageNode[]> {
        return new Promise<StorageNode[]>(resolve => {
            resolve([]);
        })
    }

    onMessage(message: MessageEvent) {
        console.log(message);
    }
}
