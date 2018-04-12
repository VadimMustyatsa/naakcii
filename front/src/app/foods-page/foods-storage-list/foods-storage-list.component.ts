import { Component, OnInit } from '@angular/core';
import {MaterializeDirective} from 'angular2-materialize';
import {Storag} from '../../shared/Storage/foods.storage.model';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';

@Component({
  selector: 'app-foods-storage-list',
  templateUrl: './foods-storage-list.component.html',
  styleUrls: ['./foods-storage-list.component.css'],
  providers: [FoodsStorageService]
})
export class FoodsStorageListComponent implements OnInit {
  private storageList: Storag[];

  constructor(private service: FoodsStorageService) {

  }

  ngOnInit() {
    this.storageList = this.service.getAll();
    //setTimeout("$('.tapStep1').tapTarget('open')", 1000);
  }
  selectStorage(id) {
    console.log('id: ' + id);
  }
}
