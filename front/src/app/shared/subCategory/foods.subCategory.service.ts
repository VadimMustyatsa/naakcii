import {Injectable} from '@angular/core';
import {SubCategory} from './foods.subCategory.model';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class FoodsSubCategoriesService {

  private subCategoryUrl: string;

  constructor(private http: HttpClient) {
  }

  getByCategory(idCategory) {
    // this.subCategoryUrl = window.location.hostname === 'localhost' ? 'http://178.124.206.42:8080/api/subcategory' : 'http://' + window.location.hostname +':8080/api/subcategory';
    this.subCategoryUrl = window.location.hostname === 'localhost' ? 'http://178.124.206.54:8080/api/subcategory' : 'https://' + window.location.hostname +':443/api/subcategory';

    // this.subCategoryUrl = window.location.hostname === 'localhost' ? 'https://178.124.206.54:8443/api/subcategory' : 'https://' + window.location.hostname +':443/api/subcategory';
    let url = this.subCategoryUrl;
    url = url + '/' + idCategory;
    return this.http.get<SubCategory[]>(url)
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
