import {Component, ViewChild, ElementRef} from '@angular/core';
import {ShareService} from "./share.service";

@Component({
    selector: 'add-storagenode',
    providers: [ShareService],
    styles: [`
    [md-fab] {
        position: fixed;
        right: 25px;
        bottom: 25px;
    }
`],
    template: `
    <button md-fab (click)="createStorageNode()" color="accent">
        <md-icon class="md-24">add</md-icon>
    </button>
`,
})
export class AddStorageNodeComponent {
    constructor(private shareService: ShareService) {
    }
    private createStorageNode() {
        this.shareService.addStorageNode();
    }
}
