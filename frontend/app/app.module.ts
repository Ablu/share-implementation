import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import {IntervalComponent} from './interval.component';
import {StorageNodeListComponent} from './storagenodelist.component';
import {StorageNodeComponent} from "./storagenode.component";

@NgModule({
    imports:      [ BrowserModule ],
    declarations: [
        AppComponent,
        IntervalComponent,
        StorageNodeComponent,
        StorageNodeListComponent,
    ],
    bootstrap:    [ AppComponent ]
})
export class AppModule { }
