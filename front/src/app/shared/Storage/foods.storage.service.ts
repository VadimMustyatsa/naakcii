import {Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {RestDataService} from '../services/rest-data.service';
import {environment} from '../../../environments/environment';

@Injectable()
export class FoodsStorageService {

  constructor(private restDataService: RestDataService) {
  }

  getAll() {
    return this.restDataService.getChains
      .map(chainList => {
        return chainList.map(chain => {
          return {
            id: chain['id'],
            name: chain['name'],
            link: chain['link'],
            discountedProducts: chain['discountedProducts'],
            averageDiscountPercentage: chain['averageDiscountPercentage'],
            imgLogo: environment.baseUrl + chain['logo'],
            imgLogoSmall: environment.baseUrl + chain['logo'],
            selected: false
          };
        });
      });
  }
}
