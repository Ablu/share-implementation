import {Component, ViewChild, ElementRef} from '@angular/core';
import {ShareService} from "./share.service";
import setInterval = core.setInterval;

@Component({
    selector: 'storage-node-list',
    providers: [ShareService],
    styles: [`
    interval {
        width: 100%;
    }
`],
    template: `
    <interval
        *ngFor="let interval of storageNodes"
        (width)="(interval.end - interval.start) * 100"
        ></interval>
`,
})
export class StorageNodeListComponent {
    private storageNodes = [];

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
                    delete this.storageNodes[i];
                    break;
                }
            }
        });
    }
}
