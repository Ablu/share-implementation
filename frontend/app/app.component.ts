import { Component } from '@angular/core';
import {StorageNodeListComponent} from './storagenodelist.component'


@Component({
    selector: 'my-app',
    template: `
    <div>
    <storage-node-list></storage-node-list>
    </div>
    <div>
        <datastorer></datastorer>
    </div>
    <div>
        <stretch-factor-edit></stretch-factor-edit>
    </div>
  `
})
export class AppComponent {
}

