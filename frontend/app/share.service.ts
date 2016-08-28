import { Injectable } from '@angular/core';
import {StorageNode} from "./storagenode";

@Injectable()
export class ShareService {
    private websocket : WebSocket;

    constructor() {
        this.websocket = new WebSocket('ws://localhost:9456/');
    }

    getStorageNodes(): Promise<StorageNode[]> {
        return new Promise<StorageNode[]>(resolve => {
            resolve([]);
        })
    }
}
