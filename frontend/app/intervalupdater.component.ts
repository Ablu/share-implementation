import {Component, Input} from '@angular/core';
import {ShareService} from "./share.service";
import {StorageNode} from "./entities/storagenode";

@Component({
    selector: 'interval-updater',
    providers: [ShareService],
    template: `
    <button md-mini-fab (click)="updateCapacities()" color="primary">
        <md-icon class="md-24">refresh</md-icon>
    </button>
`,
})
export class IntervalUpdaterComponent {
    @Input()
    storageNodes: StorageNode[] = [];

    constructor(private shareService: ShareService) {
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
