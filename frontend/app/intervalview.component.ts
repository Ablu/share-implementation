import {Component, ViewChild, ElementRef} from '@angular/core';
import {IntervalComponent} from "./interval.component";
import {ShareService} from "./share.service";

@Component({
    selector: 'interval-view',
    directives: [IntervalComponent],
    providers: [ShareService],
    styles: [`
    interval {
        width: 100%;
    }
`],
    template: `hi
    <interval width="200"></interval>
    <interval></interval>
`,
})
export class IntervalView {
    constructor(private shareService: ShareService) {

    }
}