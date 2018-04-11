import { Component, OnInit } from '@angular/core';
import {MaterializeDirective} from 'angular2-materialize';
import {Storag} from '../../shared/Storage/foods.storage.model';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';

@Component({
  selector: 'app-foods-storage-list',
  templateUrl: './foods-storage-list.component.html',
  styleUrls: ['./foods-storage-list.component.css']
})
export class FoodsStorageListComponent implements OnInit {
  storageList: Storag[];
  private service: FoodsStorageService = new FoodsStorageService;

  constructor() { }

  ngOnInit() {
    this.storageList = this.service.getAll();
  }

}
