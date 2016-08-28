import {Component, ViewChild, ElementRef} from '@angular/core';
import {IntervalComponent} from "./interval.component";

@Component({
    selector: 'interval-view',
    directives: [IntervalComponent],
    style: `
    interval {
        width: 100%;
    }
`,
    template: `hi
    <interval></interval>
    <interval></interval>
`,
})
export class IntervalView {

}