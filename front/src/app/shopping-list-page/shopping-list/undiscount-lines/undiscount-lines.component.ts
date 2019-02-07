import { Component, OnInit } from '@angular/core';

import {UndiscountService} from '../../../shared/services/undiscount.service';

@Component({
  selector: 'app-undiscount-lines',
  templateUrl: './undiscount-lines.component.html',
  styleUrls: ['./undiscount-lines.component.scss']
})
export class UndiscountLinesComponent implements OnInit {
  undiscountProduct: any;
  undiscount: Array<{ text: string; id: string }>;
  constructor(private undiscountStorage: UndiscountService,

  ) {
    this.undiscount = this.undiscountStorage.getFromUndiscount() || [];
   }

  ngOnInit() {
  }
}
