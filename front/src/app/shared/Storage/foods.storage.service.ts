import {Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {RestDataService} from "../services/rest-data.service";

@Injectable()
export class FoodsStorageService {

  constructor(private restDataService: RestDataService) {
  }

  getAll() {
    return this.restDataService.getShops
      .map(chainList => {
        return chainList.map(chain => {
          return {
            id: chain['id'],
            name: chain['name'],
            link: chain['link'],
            countGoods: chain['countGoods'],
            percent: chain['percent'],
            imgLogo: chain['imgLogo'],
            imgLogoSmall: chain['imgLogoSmall'],
            selected: false
          };
        });
      });
  }
}



