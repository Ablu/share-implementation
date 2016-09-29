import { Component } from '@angular/core';
import {MdIconRegistry} from '@angular/material';

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
    <add-storagenode></add-storagenode>
  `
})
export class AppComponent {
    constructor(){
    }
}

