import {Injectable} from '@angular/core';

import {FoodList} from './foods.foodList.model';
import {ApiService} from '../services/api.service';


@Injectable()
export class FoodsFoodListService {

  constructor(private apiService: ApiService) {
  }

  getFoodList(selectedSubCatListID, first, last) {
   
    let listID = '';
    selectedSubCatListID.map(id => {
      listID += String(id.id) + ',';
    });
    const dataGet = {SubcategoryList: listID, first: first, last: last};
    return this.apiService.getApiFoodList(dataGet);
    
  }
}


