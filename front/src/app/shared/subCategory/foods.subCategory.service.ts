import {Injectable} from '@angular/core';
import {SubCategory} from './foods.subCategory.model';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class FoodsSubCategoriesService {
  private subCategoryUrl = 'http://localhost:8080/api/getSubcategory';

  constructor(private http: HttpClient) {
  }

  getByCategory(idCategoty) {
    const dataGet = {id: idCategoty};
    console.log(dataGet);
    return this.http.get<SubCategory[]>(this.subCategoryUrl, {params: dataGet})
      .map(sabCatList => {
        return sabCatList.map(sabCut => {
          return {
            id: sabCut['id'],
            name: sabCut['name'],
            img: sabCut['picture'],
            selected: false
          };
        });
      });
    //return this.dataCategoryList[idCategoty];
  }
}
