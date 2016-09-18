import {Component, ViewChild, ElementRef} from '@angular/core';
import {ShareService} from "./share.service";
import setInterval = core.setInterval;
import {StorageNode} from "./entities/storagenode";

@Component({
    selector: 'storage-node-list',
    providers: [ShareService],
    styles: [`
    interval {
        width: 100%;
    }
`],
    template: `
    <div *ngFor="let storageNode of storageNodes">
        <storage-node [storageNode]="storageNode"></storage-node>
    </div>
    
    <input type="submit" value="Update capacities" (click)="updateCapacities()"/>
    <input type="submit" value="Add Storage Node" (click)="createStorageNode()"/>
`,
})
export class StorageNodeListComponent {
    private storageNodes: StorageNode[] = [];

    constructor(private shareService: ShareService) {
        shareService.storageNodeAddedSource$.subscribe(storageNode => {
            this.storageNodes.push(storageNode);
        });
        shareService.storageNodeUpdatedSource$.subscribe(storageNode => {
            for (let i = 0; i < this.storageNodes.length; ++i) {
                if (this.storageNodes[i].id == storageNode.id) {
                    this.storageNodes[i] = storageNode;
                    break;
                }
            }
        });
        shareService.storageNodeDeletedSource$.subscribe(storageNode => {
            for (let i = 0; i < this.storageNodes.length; ++i) {
                if (this.storageNodes[i].id == storageNode.id) {
                    this.storageNodes.splice(i, 1);
                    break;
                }
            }
        });
    }

    private updateCapacities() {
        let capacitySum = 0;
        for (let node: StorageNode of this.storageNodes) {
            capacitySum += +node.capacity;
        }

        var newCapacities = [];
        for (let node: StorageNode of this.storageNodes) {
            newCapacities.push({
                id: node.id,
                capacity: node.capacity / capacitySum,
            });
        }
        this.shareService.updateCapacities(newCapacities);
    }

    private createStorageNode() {
        this.shareService.addStorageNode();
    }
}
