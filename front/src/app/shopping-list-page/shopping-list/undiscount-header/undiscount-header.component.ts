import { Component, OnInit } from '@angular/core';

import {UndiscountService} from '../../../shared/services/undiscount.service';

@Component({
  selector: 'app-undiscount-header',
  templateUrl: './undiscount-header.component.html',
  styleUrls: ['./undiscount-header.component.scss']
})
export class UndiscountHeaderComponent implements OnInit {

  constructor(
    private undiscountStorage: UndiscountService
    ) { 

  }

  ngOnInit() {
  }

  getCouunt(){
    return this.undiscountStorage.getCount();
  }

}
