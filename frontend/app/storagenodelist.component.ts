import {Component, Input} from '@angular/core';
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
            <button md-button (click)="deleteNode(storageNode.id)">
                <md-icon class="md-24">delete</md-icon>
            </button>
       </md-card-actions>
    </md-card>
`,
})
export class StorageNodeListComponent {
    @Input()
    storageNodes: StorageNode[] = [];

    constructor(private shareService: ShareService) {
    }

    deleteNode(id: number) {
        this.shareService.deleteStorageNode(id);
    }
}
