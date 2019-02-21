import { Injectable } from '@angular/core';
import { RestDataService } from '../../shared/services/rest-data.service';
import { Observable } from 'rxjs';
import { from } from 'rxjs/observable/from';

import { Statistics } from '../model/statistics';


@Injectable()
export class HomePageService {

  statistics: Observable<Statistics>;

  constructor(restDataService: RestDataService) {
    // this.statistics = restDataService.statistics;
    this.statistics = from<Statistics>([{chainQuantity: 7, discountedProducts: 554, averageDiscountPercentage: 30}]);
  }

}
