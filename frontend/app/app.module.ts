import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {MaterialModule} from '@angular/material';

import { AppComponent }  from './app.component';
import {IntervalComponent} from './interval.component';
import {StorageNodeListComponent} from './storagenodelist.component';
import {DataStorerComponent} from "./datastorer.component";
import {StretchFactorEdit} from "./stretchfactoredit.component";
import {FormsModule} from "@angular/forms";
import {AddStorageNodeComponent} from "./addstoragenode.component";

@NgModule({
    imports:      [
        BrowserModule,
        FormsModule,
        MaterialModule,
    ],
    declarations: [
        AppComponent,
        IntervalComponent,
        DataStorerComponent,
        StorageNodeListComponent,
        StretchFactorEdit,
        AddStorageNodeComponent,
    ],
    bootstrap:    [ AppComponent ]
})
export class AppModule { }
