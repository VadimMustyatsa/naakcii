import {Component, EventEmitter, OnInit, Inject} from '@angular/core';
import {MaterializeAction} from 'angular2-materialize';
import {FoodsCategoriesService} from '../../shared/category/foods.categories.service';
import {Category} from '../../shared/category/foods.category.model';
import {Observer} from 'rxjs/Observer';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';

@Component({
  selector: 'app-foods-group',
  templateUrl: './foods-category.component.html',
  styleUrls: ['./foods-category.component.css']
})
export class FoodsCategoryComponent implements OnInit {
  categories: Category[];
  private service: FoodsCategoriesService = new FoodsCategoriesService;
  constructor(@Inject(SHARED_STATE) private observer: Observer<SharedState>) { }

  ngOnInit() {
    this.categories = this.service.getAll();
  }
  carouselActions = new EventEmitter<string|MaterializeAction>();

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
