import {Component, ViewChild, ElementRef} from '@angular/core';
import {ShareService} from "./share.service";

@Component({
    selector: 'datastorer',
    providers: [ShareService],
    template: `
    <label for="data">Data to store:</label>
    <input type="text" id="data"
       required
       [(ngModel)]="data" name="data">
    <input type="submit" value="Store" (click)="store()"/>
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
