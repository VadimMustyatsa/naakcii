import {Injectable} from '@angular/core';
import {Storag} from './foods.storage.model';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';

@Injectable()
export class FoodsStorageService {
  // private storeUrl = 'assets/json/StoreList.json';
  private storeUrl = 'http://http://178.124.206.54:8080/api/getChain';

  constructor(private http: HttpClient) {
    console.log('storeService - constr');
  }
  getAll() {
    console.log('storeService - start');
    return this.http.get<Storag[]>(this.storeUrl)
      .map(chainList => {
        return chainList.map(chain => {
          return {
            id: chain['id'],
            name: chain['name'],
            location: chain['location'],
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
