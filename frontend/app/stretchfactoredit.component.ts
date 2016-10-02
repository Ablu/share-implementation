import {Component, ViewChild, ElementRef} from '@angular/core';
import {ShareService} from "./share.service";
import setInterval = core.setInterval;

@Component({
    selector: 'stretch-factor-edit',
    providers: [ShareService],
    template: `
    <md-input placeholder="Stretchfactor" [(ngModel)]="factor" (keyup.enter)="updateStretchFactor()"></md-input>
    <button md-mini-fab type="submit" (click)="updateStretchFactor()" color="primary">
        <md-icon class="md-24">done</md-icon>
    </button>
`,
})
export class StretchFactorEdit {
    private factor: number;

    constructor(private shareService: ShareService) {
        shareService.strechFactorUpdatedSource$.subscribe(stretchFactor => {
            this.factor = stretchFactor;
        })
    }

    updateStretchFactor() {
        this.shareService.updateStretchFactor(+this.factor);
    }
}
