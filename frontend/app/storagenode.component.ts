import {Component, Input} from '@angular/core';
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
            Capacity:
            <input #slider type="range" max="1" step="0.0001"
                [(ngModel)]="storageNode.capacity" name="storageNode.capacity"
                />
        </div>
        <div class="floatStop"></div>
`})
export class StorageNodeComponent {
    @Input() public storageNode: StorageNode;

    constructor() {
    }
}

