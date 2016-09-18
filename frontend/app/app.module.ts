import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import {IntervalComponent} from './interval.component';
import {StorageNodeListComponent} from './storagenodelist.component';
import {StorageNodeComponent} from "./storagenode.component";
import {DataStorerComponent} from "./datastorer.component";
import {FormsModule} from "@angular/forms";

@NgModule({
    imports:      [
        BrowserModule,
        FormsModule,
    ],
    declarations: [
        AppComponent,
        IntervalComponent,
        DataStorerComponent,
        StorageNodeComponent,
        StorageNodeListComponent,
    ],
    bootstrap:    [ AppComponent ]
})
export class AppModule { }
