import { Component } from '@angular/core';
import {MdIconRegistry} from '@angular/material';
import {StorageNodeListComponent} from './storagenodelist.component'


@Component({
    selector: 'my-app',
    providers: [MdIconRegistry],
    template: `
    <div>
        <datastorer></datastorer>
    </div>
    <div>
        <stretch-factor-edit></stretch-factor-edit>
    </div>
    <div>
    <storage-node-list></storage-node-list>
    </div>
  `
})
export class AppComponent {
    constructor(){

    }
}

