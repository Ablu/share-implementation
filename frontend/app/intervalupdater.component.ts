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
        var newCapacities = [];
        for (let node of this.storageNodes) {
            newCapacities.push({
                id: node.id,
                capacity: +node.capacity,
            });
        }
        this.shareService.updateCapacities(newCapacities);
    }
}
