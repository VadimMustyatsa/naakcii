import {Injectable} from '@angular/core';
import {Category} from './foods.category.model';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';

@Injectable()
export class FoodsCategoriesService {

  private categoryUrl: string;
  private data: Category[] = [];
  private selectedCategory: Category;

  constructor(private http: HttpClient) {
  }

  getAll() {
    this.categoryUrl = window.location.hostname === 'localhost' ? '//178.124.206.42:8080/api/category' : '//' + window.location.hostname +':8080/api/category';
    return this.http.get<Category[]>(this.categoryUrl)
      .map(categoryList => {
        return categoryList.map(category => {
          return {
            id: category['id'],
            name: category['name'],
            icon: category['icon']
          };
        });
      });
  }

  setCategories(categories: Category[]) {
    this.data = categories;
  }

  getById(id: number): Category {
    return this.data.find(x => x.id === id);
  }

  setSelectCategory(cat: Category) {
    this.selectedCategory = cat;
  }

  getSelectCategory(): Category {
    return this.selectedCategory;
  }
}
