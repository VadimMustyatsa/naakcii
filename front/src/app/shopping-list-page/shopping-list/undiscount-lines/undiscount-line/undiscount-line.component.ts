import { Component, OnInit,Input } from '@angular/core';

import {UndiscountService} from '../../../../shared/services/undiscount.service';
import setImgStyles from '../../../../shared/utils/setImgStyles';

@Component({
  selector: 'app-undiscount-line',
  templateUrl: './undiscount-line.component.html',
  styleUrls: ['./undiscount-line.component.scss']
})
export class UndiscountLineComponent implements OnInit {
  @Input() item:{id:string,text:string};

  constructor(private undiscountStorage: UndiscountService
    ){
  }

  ngOnInit() {
  }

  onRemoveUndiscount(){
   this.undiscountStorage.delProduct(this.item.id);
 }

  setImgStyles(pict) {
    setImgStyles(pict);
  }

}
