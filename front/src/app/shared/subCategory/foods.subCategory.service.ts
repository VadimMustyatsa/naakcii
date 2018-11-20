import {Injectable} from '@angular/core';

import {SubCategory} from './foods.subCategory.model';
import {ApiService} from '../services/api.service';


@Injectable()
export class FoodsSubCategoriesService {

  constructor(private apiService: ApiService) {
  }

  getByCategory(idCategory) {
    return this.apiService.getSubByCategory(idCategory);
  }
}
