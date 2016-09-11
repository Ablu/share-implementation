import { Component } from '@angular/core';
import {IntervalView} from './intervalview.component'


@Component({
    selector: 'my-app',
    directives: [IntervalView],
    template: `
    <interval-view></interval-view>
  `
})
export class AppComponent {

}

