import {Component, EventEmitter, OnInit, Inject} from '@angular/core';
import {MaterializeAction} from 'angular2-materialize';
import {FoodsCategoriesService} from '../../shared/category/foods.categories.service';
import {Category} from '../../shared/category/foods.category.model';
import {Observer} from 'rxjs/Observer';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';

@Component({
  selector: 'app-foods-group',
  templateUrl: './foods-category.component.html',
  styleUrls: ['./foods-category.component.css'],
  providers: [FoodsCategoriesService]
})
export class FoodsCategoryComponent implements OnInit {
  categories: Category[];
  private defSelectCategory = 0;

  constructor(private service: FoodsCategoriesService, @Inject(SHARED_STATE) private observer: Observer<SharedState>) {
    console.log('CategoryComponent - constr');
  }

  ngOnInit() {
    console.log('CategoryComponent - ngOnInit');
    this.service.getAll().subscribe(categoryList => {
      this.categories = categoryList;
      this.service.setCategories(categoryList);
      this.selectCategory(this.service.getById(this.defSelectCategory));
      console.log(this.categories);
    });
  }
  carouselActions = new EventEmitter<string | MaterializeAction>();

  isSelected(id: number): boolean {
    if (!this.service.getSelectCategory()) {
      return false;
    }
    return (this.service.getSelectCategory().id === id);
  }

  selectCategory(category: Category) {
    this.service.setSelectCategory(category);
    this.observer.next(new SharedState(MODES.SELECT_CATEGORY, category));
    console.log('Category: ' + category.id + ':' + category.name);
  }

  chevronLeft(this) {
    this.carouselActions.emit({action: 'carousel', params: ['prev']});
  }

  chevronRight(this) {
    this.carouselActions.emit({action: 'carousel', params: ['next']});
  }
}
