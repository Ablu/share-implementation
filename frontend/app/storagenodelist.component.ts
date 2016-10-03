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
    
    .row {
        width: 100%;
        table-layout: fixed;
        border-collapse: separate;
        border-spacing: 25px;
    }
    
    @media only screen and (min-device-width:480px){
        .row > div {
            display: table-cell;
        }
    }
    
    .data {
        display: flex;
        width: 100%;
    }
    
    .data > md-card {
        display: inline-block;
        margin: 5px;
    }
`],
    template: `
    <md-card *ngFor="let storageNode of storageNodes">
       <md-card-title>Node {{storageNode.id}}:</md-card-title>   
       <md-card-content class="row">
            <div class="intervals">
                <div *ngFor="let interval of storageNode.intervals">
                    <interval [interval]="interval">
                    </interval>
                </div>
            </div>
            <div class="data">
                <h2>Stored data:</h2>
                <i *ngIf="storageNode.storedData.length === 0" >None...</i>
                <md-card *ngFor="let data of storageNode.storedData">
                    <md-card-subtitle>{{data.id}}:</md-card-subtitle>
                    <md-card-content>
                        <p>{{data.data}}</p>
                    </md-card-content>
                    <md-card-actions>
                       <button md-button (click)="deleteData(data.id)">DELETE</button>
                    </md-card-actions>
                </md-card>
                <!--<md-list>
                    <md-list-item *ngFor="let data of storageNode.storedData">
                        <h3 md-line>{{data.id}}:</h3>
                        <p md-line>{{data.data}}</p>
                    </md-list-item>
                </md-list>-->
            </div>
            <div class="metadata">
                <h2>Capacity:</h2>
                <input #slider type="range" max="1" step="0.0001"
                    [(ngModel)]="storageNode.capacity" name="storageNode.capacity"
                    />
                    
                <!--<md-slider min="0" max="1" step="0.0001" [(ngModel)]="storageNode.capacity"></md-slider>-->
            </div>
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

    deleteData(id: number) {
        this.shareService.deleteData(id);
    }
}
