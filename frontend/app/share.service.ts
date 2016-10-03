import { Injectable } from '@angular/core';
import { Subject }    from 'rxjs/Subject';
import {StorageNode} from "./entities/storagenode";
import {FindResult} from "./entities/foundresult";

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

    private stretchFactorUpdated = new Subject<number>();
    strechFactorUpdatedSource$ = this.stretchFactorUpdated.asObservable();

    private dataFound = new Subject<FindResult>()
    dataFoundSource$ = this.dataFound.asObservable();

    constructor() {
        this.createWebsocketConnection();
        setInterval(() => {
            if (this.websocket.readyState == this.websocket.CLOSED) {
                this.createWebsocketConnection();
                console.log('reconnecting ws channel');
            }
        }, 1000)
    }

    private createWebsocketConnection() {
        this.websocket = new WebSocket('ws://' + location.hostname + ':9456/');
        this.websocket.addEventListener('message', (message) => {
            this.onMessage(message);
        });
    }

    private onMessage(message: MessageEvent) {
        let message = JSON.parse(message.data);

        switch (message.type) {
            case "state":
                this.handleState(message);
                break;
            case "foundMessage":
                this.handleFoundMessage(message);
                break;
            default:
                console.error("unknown message:", message)
        }
    }

    private handleState(state) {
        this.stretchFactorUpdated.next(state.stretchFactor);

        let receivedStorageNodes = state.storageNodes;
        for (let receivedNode of receivedStorageNodes) {
            let storageNode: StorageNode = receivedNode;
            if (!this.storageNodes.has(storageNode.id)) {
                this.storageNodes.set(storageNode.id, storageNode);
                this.storageNodeAdded.next(storageNode);
            } else {
                this.storageNodes.set(storageNode.id, storageNode);
                this.storageNodeUpdated.next(storageNode);
            }
        }

        for (let id of Array.from(this.storageNodes.keys())) {
            let exists = receivedStorageNodes.filter(node => node.id == id).length > 0;
            if (!exists) {
                this.storageNodeDeleted.next(this.storageNodes.get(id));
                this.storageNodes.delete(id);
            }
        }
    }

    private handleFoundMessage(message) {
        this.dataFound.next(message);
    }

    private send(obj) {
        this.websocket.send(JSON.stringify(obj));
    }

    public addStorageNode() {
        this.send({
            command: 'addStorageNode',
        });
    }

    public storeData(data: string) {
        this.send({
            command: 'storeData',
            data: data,
        })
    }

    public updateCapacities(capacities) {
        this.send({
            command: 'updateCapacities',
            capacities: capacities,
        })
    }

    public updateStretchFactor(factor: number) {
        this.send({
            command: 'updateStretchFactor',
            factor: factor,
        })
    }

    public deleteStorageNode(id: number) {
        this.send({
            command: 'deleteStorageNode',
            id: id,
        })
    }

    public deleteData(id: number) {
        this.send({
            command: 'deleteData',
            id: id,
        })
    }

    public search(idToSearch: number) {
        this.send({
            command: 'search',
            id: idToSearch,
        })
    }
}
