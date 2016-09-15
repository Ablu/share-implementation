import { Injectable } from '@angular/core';
import { Subject }    from 'rxjs/Subject';
import {StorageNode} from "./entities/storagenode";

@Injectable()
export class ShareService {
    private websocket: WebSocket;

    private storageNodes: Map<number, StorageNode> = new Map<number, StorageNode>();
    
    private storageNodeAdded = new Subject<StorageNode>();
    storageNodeAddedSource$ = this.storageNodeAdded.asObservable();
    
    private storageNodeDeleted = new Subject<StorageNode>();
    storageNodeDeletedSource$ = this.storageNodeDeleted.asObservable();
    
    private storageNodeUpdated = new Subject<StorageNode>();
    storageNodeUpdatedSource$ = this.storageNodeUpdated.asObservable();
    
    
    constructor() {
        this.websocket = new WebSocket('ws://localhost:9456/');
        this.websocket.addEventListener('message', (message) => {
            this.onMessage(message);
        });
    }

    onMessage(message: MessageEvent) {
        let receivedStorageNodes = JSON.parse(message.data);
        for (let receivedNode of receivedStorageNodes) {
            let storageNode: StorageNode = receivedNode;
            if (!this.storageNodes[storageNode.id]) {
                this.storageNodes[storageNode.id] = storageNode;
                this.storageNodeAdded.next(storageNode);
            } else {
                this.storageNodes[storageNode.id] = storageNode;
                this.storageNodeUpdated.next(storageNode);
            }
        }

        for (let id of Array.from(this.storageNodes.keys())) {
            let exists = receivedStorageNodes.filter(node => node.id == id).length > 0;
            if (!exists) {
                this.storageNodeDeleted.next(this.storageNodes[id]);
                this.storageNodes.delete(id);
            }
        }
    }
}