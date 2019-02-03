import { Component, OnInit } from '@angular/core';

import {UndiscountService} from '../../../shared/services/undiscount.service';
import {BreakPointCheckService} from '../../../shared/services/breakpoint-check.service';

@Component({
  selector: 'app-undiscount-header',
  templateUrl: './undiscount-header.component.html',
  styleUrls: ['./undiscount-header.component.scss']
})
export class UndiscountHeaderComponent implements OnInit {

  constructor(
    private undiscountStorage: UndiscountService,
    public breakPointCheckService: BreakPointCheckService,
    ) { 

  }

  ngOnInit() {
  }

  getCouunt(){
    return this.undiscountStorage.getCount();
  }

}
