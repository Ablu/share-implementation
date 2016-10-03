import { Component } from '@angular/core';
import {MdIconRegistry} from '@angular/material';
import {ShareService} from "./share.service";
import {StorageNode} from "./entities/storagenode";

@Component({
    selector: 'my-app',
    providers: [MdIconRegistry, ShareService],
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
            <stretch-factor-edit [storageNodes]="storageNodes"></stretch-factor-edit>
            <span class="spacer"></span>
            <search></search>
            <span class="spacer"></span>
            <interval-updater [storageNodes]="storageNodes"></interval-updater>
        </div>
        <div>
            <storage-node-list [storageNodes]="storageNodes"></storage-node-list>
        </div>
    </div>
    <add-storagenode></add-storagenode>
  `
})
export class AppComponent {
    storageNodes: StorageNode[] = [];

    constructor(private shareService: ShareService) {
        shareService.storageNodeAddedSource$.subscribe(storageNode => {
            this.storageNodes.push(storageNode);
        });
        shareService.storageNodeUpdatedSource$.subscribe(storageNode => {
            for (let i = 0; i < this.storageNodes.length; ++i) {
                if (this.storageNodes[i].id == storageNode.id) {
                    this.storageNodes[i] = storageNode;
                    break;
                }
            }
        });
        shareService.storageNodeDeletedSource$.subscribe(storageNode => {
            for (let i = 0; i < this.storageNodes.length; ++i) {
                if (this.storageNodes[i].id == storageNode.id) {
                    this.storageNodes.splice(i, 1);
                    break;
                }
            }
        });
    }

}

