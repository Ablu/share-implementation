import { Component, Input, ViewChild, ElementRef } from '@angular/core';
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
            Capacity: <input #slider type="range" (input)="update($event)" [value]="storageNode.capacity * 100"/>
        </div>
        <div class="floatStop"></div>
`
})
export class StorageNodeComponent {
    @Input() public storageNode: StorageNode;
    public modifiedCapacity: number = 0.0;

    @ViewChild("slider") slider: ElementRef;

    constructor() {

    }

    update() {
        this.modifiedCapacity = this.slider.nativeElement.value;
    }
}

