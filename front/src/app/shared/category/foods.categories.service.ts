import {Injectable} from '@angular/core';
import {Category} from './foods.category.model';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';

@Injectable()
export class FoodsCategoriesService {
  private categoryUrl = 'assets/json/Category.json';
  //private categoryUrl = 'http://localhost:8080/api/getCategory';

  private data: Category[] = [];
  private selectedCategory: Category;

  constructor(private http: HttpClient) {
    console.log('categoryService - constr');
  }
  getAll() {
    console.log('categoryService - getAll');
    return this.http.get<Category[]>(this.categoryUrl)
      .map(categoryList => {
        return categoryList.map(category => {
          return {
            id: category['id'],
            name: category['name'],
            img: category['img']
          };
        });
      });
  }
  setCategories(categories: Category[]) {
    this.data = categories;
    console.log(this.data);
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
