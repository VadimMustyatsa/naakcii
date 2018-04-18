import {Component, EventEmitter, OnInit, Inject} from '@angular/core';
import {MaterializeAction} from 'angular2-materialize';
import {FoodsCategoriesService} from '../../shared/category/foods.categories.service';
import {Category} from '../../shared/category/foods.category.model';
import {Observer} from 'rxjs/Observer';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import { NguCarousel, NguCarouselStore, NguCarouselService } from '@ngu/carousel';

@Component({
  selector: 'app-foods-group',
  templateUrl: './foods-category.component.html',
  styleUrls: ['./foods-category.component.css'],
  providers: [FoodsCategoriesService]
})
export class FoodsCategoryComponent implements OnInit {
  categories: Category[];
  private defSelectCategory = 0;
  private carouselToken: string;
  public carouselTile: NguCarousel;

  constructor(private carousel: NguCarouselService, private service: FoodsCategoriesService, @Inject(SHARED_STATE) private observer: Observer<SharedState>) {
    console.log('CategoryComponent - constr');
  }

  ngOnInit() {
    console.log('CategoryComponent - ngOnInit');
    this.service.getAll().subscribe(categoryList => {
      this.categories = categoryList;
      this.service.setCategories(categoryList);
      this.selectCategory(this.service.getById(this.defSelectCategory));
      console.log(this.categories);
      this.carouselTile = {
        grid: {xs: 3, sm: 5, md: 5, lg: 7, all: 0},
        slide: 2,
        speed: 400,
        animation: 'lazy',
        point: {
          visible: false
        },
        load: 2,
        touch: true,
        loop: false,
        custom: 'banner'
      }
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

  initDataFn(key: NguCarouselStore) {
    this.carouselToken = key.token;
  }

  resetFn() {
    this.carousel.reset(this.carouselToken);
  }

  moveToSlide() {
    this.carousel.moveToSlide(this.carouselToken, 2, false);
  }
}
