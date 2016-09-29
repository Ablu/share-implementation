import { Component } from '@angular/core';
import {MdIconRegistry} from '@angular/material';

@Component({
    selector: 'my-app',
    providers: [MdIconRegistry],
    styles: [`
    #content {
        padding: 20px;
    }
    .distribute {
        display: flex;
    }
    
    .spacer {
        flex: 1 1 auto;
    }
`],
    template: `
    <md-toolbar color="primary">
        <md-icon class="md-24" style="padding: 0 14px;">menu</md-icon>
        <span>A SHARE implementation</span>
    </md-toolbar>
    <div id="content">
        <div class="distribute">
            <datastorer></datastorer>
            <span class="spacer"></span>
            <stretch-factor-edit></stretch-factor-edit>
        </div>
        <div>
            <storage-node-list></storage-node-list>
        </div>
    </div>
    <add-storagenode></add-storagenode>
  `
})
export class AppComponent {
    constructor(){
    }
}

