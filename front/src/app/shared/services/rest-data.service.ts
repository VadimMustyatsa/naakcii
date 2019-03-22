import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Category} from '../category/foods.category.model';
import {SubCategory} from '../subCategory/foods.subCategory.model';
import {Storag} from '../Storage/foods.storage.model';
import {FoodList} from '../foodList/foods.foodList.model';
import {ChainProductFromJson} from '../model/chain-product-json.model';
import {Observable} from 'rxjs';
import {Statistics} from '../../home-page/model/statistics';

@Injectable()


export class RestDataService {

  constructor (private http: HttpClient) { }

  get category() {
    // return this.http.get<Category[]>(`category`);
    return this.http.get<Category[]>(`categories`);
  }

  getSubCategory(categoryId) {
    // return this.http.get<SubCategory[]>(`subcategory/${categoryId}`);
    return this.http.get<SubCategory[]>(`subcategories/${categoryId}`);
  }

  get getChains() {
    // return this.http.get<Storag[]>(`chain`);
    return this.http.get<Storag[]>(`chains`);
  }

  getProducts(data) {
    // return this.http.get<FoodList[]>(`product/`, {params: data});
    return this.http.get<ChainProductFromJson[]>(`products`, {params: data});
  }

  get statistics(): Observable<Statistics> {
    // return this.http.get<Statistics>('/statistics');
    return this.http.get<Statistics>('statistics');
  }

  addSubscriber(body) {
    return this.http.post('subscribers', body, {'headers': {'Content-Type': 'application/json'}});
  }

}
