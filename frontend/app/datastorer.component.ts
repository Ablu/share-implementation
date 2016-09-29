import {Component, ViewChild, ElementRef} from '@angular/core';
import {ShareService} from "./share.service";

@Component({
    selector: 'datastorer',
    providers: [ShareService],
    template: `
    <md-input placeholder="Data to store" [(ngModel)]="data"></md-input>
    <button md-mini-fab type="submit" (click)="store()" color="primary">
        <md-icon class="md-24">add</md-icon>
    </button>
`,
})
export class DataStorerComponent {
    private data: string;

    constructor(private shareService: ShareService) {
    }

    private store() {
        this.shareService.storeData(this.data);
        this.data = '';
    }
}
