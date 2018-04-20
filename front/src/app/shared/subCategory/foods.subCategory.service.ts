import {Injectable} from '@angular/core';
import {SubCategory} from './foods.subCategory.model';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class FoodsSubCategoriesService {
  private subCategoryLocal = 'assets/json/SubCategoryList.json';
  private subCategoryUrl = 'http://178.124.206.54:8080/api/getSubcategory';

  constructor(private http: HttpClient) { }

  getByCategory(idCategorty) {
    console.log('SubCategoriesService - getByCategory');
    const dataGet = {id: idCategorty};

    return this.http.get<SubCategory[]>(this.subCategoryUrl, {params: dataGet})   //для реального запроса с бэка
    //return this.http.get<SubCategory[]>(this.subCategoryLocal)                      //для отладки - из файла
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
