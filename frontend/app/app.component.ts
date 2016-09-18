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
  `
})
export class AppComponent {
}

