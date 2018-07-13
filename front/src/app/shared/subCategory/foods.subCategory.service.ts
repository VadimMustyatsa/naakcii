import {Injectable} from '@angular/core';
import {SubCategory} from './foods.subCategory.model';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class FoodsSubCategoriesService {
  private subCategoryLocal = 'assets/json/SubCategoryList.json';
  private subCategoryUrl = 'http://localhost:8080/subcategory';

  constructor(private http: HttpClient) {
  }

  getByCategory(idCategory) {
    //console.log('SubCategoriesService - getByCategory');
    let url = this.subCategoryUrl;
    url = url + '/' + idCategory;
    return this.http.get<SubCategory[]>(url)   //для реального запроса с бэка
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
