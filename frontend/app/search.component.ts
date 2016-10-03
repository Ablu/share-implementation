import {Component, ViewChild, ElementRef} from '@angular/core';
import {ShareService} from "./share.service";

@Component({
    selector: 'search',
    providers: [ShareService],
    template: `
    <md-input placeholder="Search for id" [(ngModel)]="idToSearch" (keyup.enter)="search()"></md-input>
    <button md-mini-fab type="submit" (click)="search()" color="primary">
        <md-icon class="md-24">search</md-icon>
    </button>
`,
})
export class SearchComponent {
    idToSearch: number;

    constructor (private shareService: ShareService) {
        shareService.dataFoundSource$.subscribe(findResult => {
            if (findResult.data) {
                alert("Found data id " + findResult.dataId +
                    ". The value is:\n" + findResult.data);
            } else {
                alert("Did not found data with the id " + findResult.dataId + "!");
            }
        })
    }

    private search() {
        this.shareService.search(+this.idToSearch);
    }
}
