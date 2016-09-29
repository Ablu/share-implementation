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
    
    .intervals {
        float: left;
    }
    .metadata {
        float: right;
    }
    .floatStop {
        clear: both;
    }
`],
    template: `
    <md-card *ngFor="let storageNode of storageNodes">
       <md-card-title>Node {{storageNode.id}}:</md-card-title>   
       <md-card-content>
            <div class="intervals">
                <div *ngFor="let interval of storageNode.intervals">
                    <interval [interval]="interval">
                    </interval>
                </div>
            </div>
            <div class="metadata">
                <input #slider type="range" max="1" step="0.0001"
                    [(ngModel)]="storageNode.capacity" name="storageNode.capacity"
                    />
                <!--<md-slider min="0" max="1" step="0.0001" [(ngModel)]="storageNode.capacity"></md-slider>-->
            </div>
            
            <div class="floatStop"></div>
       </md-card-content>
       <md-card-actions>
            <button md-button><md-icon class="md-24">delete</md-icon></button>
       </md-card-actions>
    </md-card>
    
    <button md-mini-fab (click)="updateCapacities()" color="primary">
        <md-icon class="md-24">refresh</md-icon>
    </button>
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
        for (let node of this.storageNodes) {
            capacitySum += +node.capacity;
        }

        var newCapacities = [];
        for (let node of this.storageNodes) {
            newCapacities.push({
                id: node.id,
                capacity: node.capacity / capacitySum,
            });
        }
        this.shareService.updateCapacities(newCapacities);
    }
}
