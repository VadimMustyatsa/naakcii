import {Injectable} from '@angular/core';
import {Category} from './foods.category.model';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';

@Injectable()
export class FoodsCategoriesService {
  private categoryUrl = 'assets/json/Category.json';
  //private categoryUrl = 'http://localhost:90/api/getCategory';

  private data: Category[] = [];

  constructor(private http: HttpClient) {
    this.data = [];
    this.http.get<Category[]>(this.categoryUrl).subscribe(dt => {
      dt.map(el => {
        this.data.push({
          id: el['id'],
          name: el['name'],
          img: el['img']
        });
      });
    });
  }

  private selectedCategory: Category;

  getAll(): Category[] {
    return this.data;
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
