import {Component, Input} from '@angular/core';
import {ShareService} from "./share.service";
import setInterval = core.setInterval;
import {StorageNode} from "./entities/storagenode";

@Component({
    selector: 'stretch-factor-edit',
    providers: [ShareService],
    template: `
    <md-input placeholder="Stretchfactor" [(ngModel)]="factor" (keyup.enter)="updateStretchFactor()"></md-input>
    <button md-mini-fab type="submit" (click)="updateStretchFactor()" color="primary">
        <md-icon class="md-24">done</md-icon>
    </button>
    <button md-mini-fab type="submit" (click)="autoCalculateStretchFactor()" color="primary">
        <md-icon class="md-24">network_check</md-icon>
    </button>
`,
})
export class StretchFactorEdit {
    @Input()
    storageNodes: StorageNode[] = [];
    private factor: number;

    constructor(private shareService: ShareService) {
        shareService.strechFactorUpdatedSource$.subscribe(stretchFactor => {
            this.factor = stretchFactor;
        })
    }

    updateStretchFactor() {
        this.shareService.updateStretchFactor(+this.factor);
    }

    autoCalculateStretchFactor() {
        let nodeCount = this.storageNodes.length;
        let optimalFactor = Math.ceil(Math.log(nodeCount));
        this.shareService.updateStretchFactor(optimalFactor);
    }
}
