import {Component, Input, OnInit} from '@angular/core';
import {StorageNode} from "./entities/storagenode";


@Component({
    selector: 'storage-node',
    styles: [`
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
        <div class="intervals">
            <div *ngFor="let interval of storageNode.intervals">
                <interval [interval]="interval">
                </interval>
            </div>
        </div>
        <div class="metadata">
            <h1>Node {{storageNode.id}}</h1>
            Capacity: <input #slider type="range" [(ngModel)]="modifiedCapacity" name="modifiedCapacity"/>
        </div>
        <div class="floatStop"></div>
`
})
export class StorageNodeComponent implements OnInit {
    @Input() public storageNode: StorageNode;
    public modifiedCapacity: number = 0.0;

    constructor() {
    }

    ngOnInit() {
        this.modifiedCapacity = this.storageNode.capacity * 100;
    }
}

