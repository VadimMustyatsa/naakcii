import {Injectable} from '@angular/core';
import {Category} from './foods.category.model';
import 'rxjs/add/operator/map';
import {RestDataService} from '../services/rest-data.service';

@Injectable()
export class FoodsCategoriesService {

  private data: Category[] = [];
  private selectedCategory: Category;

  constructor(private restDataService: RestDataService) {
  }

  getAll() {
    return this.restDataService.category
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
