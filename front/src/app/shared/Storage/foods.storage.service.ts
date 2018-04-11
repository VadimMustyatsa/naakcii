import {Injectable} from '@angular/core';
import {Storag} from './foods.storage.model';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';

@Injectable()
export class FoodsStorageService {
  private storeUrl = 'assets/json/StoreList.json';
  //private storeUrl = 'api/StorageList';

  private data: Storag[] = [];

  constructor(private http: HttpClient) {
    this.http.get<Storag[]>(this.storeUrl).subscribe(data => {
      data.map(el => {
        this.data.push({
          id: el['id'],
          name: el['name'],
          location: el['location'],
          countGoods: el['countGoods'],
          percent: el['percent'],
          imgLogo: el['imgLogo'],
          imgLogoSmall: el['imgLogoSmall']
        });
      });
    });
  }

  getAll() {
    console.log(this.data);
    return this.data;
  }

  getById(id: number): Storag {
    return this.data.find(x => x.id === id);
  }
}
