import {Injectable} from '@angular/core';
import {RestDataService} from "../services/rest-data.service";

@Injectable()
export class FoodsSubCategoriesService {

  constructor(private restDataService: RestDataService) {
  }

  getByCategory(categoryId) {
    return  this.restDataService.getSubCategory(categoryId)
      .map(sabCatList => {
        return sabCatList.map(sabCut => {
          return {
            id: sabCut['id'],
            name: sabCut['name'],
            img: sabCut['img'],
            selected: false
          };
        });
      });
  }
}
