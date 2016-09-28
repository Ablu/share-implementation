import {Component, ViewChild, ElementRef} from '@angular/core';
import {ShareService} from "./share.service";
import setInterval = core.setInterval;

@Component({
    selector: 'stretch-factor-edit',
    providers: [ShareService],
    template: `
    <label for="factor">Stretchfactor:</label>
    <input type="number" id="factor"
       required
       [(ngModel)]="factor" name="factor">
    <input type="submit" value="Update Strech Factor" (click)="updateStretchFactor()"/>
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
        this.shareService.updateStretchFactor(this.factor);
    }
}
