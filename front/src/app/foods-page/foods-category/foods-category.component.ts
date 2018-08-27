import {Component, EventEmitter, OnInit, Inject} from '@angular/core';
import {MaterializeAction} from 'angular2-materialize';
import {FoodsCategoriesService} from '../../shared/category/foods.categories.service';
import {Category} from '../../shared/category/foods.category.model';
import {Observer} from 'rxjs/Observer';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import { NguCarousel, NguCarouselStore, NguCarouselService } from '@ngu/carousel';
import {BreakPointCheckService} from "../../shared/services/breakpoint-check.service";

@Component({
  selector: 'app-foods-group',
  templateUrl: './foods-category.component.html',
  styleUrls: ['./foods-category.component.scss'],
  providers: [FoodsCategoriesService]
})
export class FoodsCategoryComponent implements OnInit {
  categories: Category[];
  public carouselCategory: NguCarousel;

  constructor(private carousel: NguCarouselService,
              private service: FoodsCategoriesService,
              public breakPointCheckService: BreakPointCheckService,
              @Inject(SHARED_STATE) private observer: Observer<SharedState>) {
  }
  ngOnInit() {
    this.initCarouselCategory();
    this.service.getAll().subscribe(categoryList => {
      this.categories = categoryList;
      this.service.setCategories(categoryList);
      this.selectCategory(this.service.getById(this.categories[0].id));
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
  }
  initCarouselCategory() {
    this.carouselCategory = {
      grid: {xs: 1, sm: 4, md: 5, lg: 7, all: 0},
      slide: 1,
      speed: 500,
      point: {
        visible: false
      },
      load: 2,
      touch: true,
      loop: true,
      custom: 'banner'
    }
  }
  public myfunc(event: Event) {
    // carouselLoad will trigger this funnction when your load value reaches
    // it is helps to load the data by parts to increase the performance of the app
    // must use feature to all carousel
  }
  getImage(id: number) {
    return 'url(\'assets/images/Category/sprites/'+this.categories.find(x=> x.id == id).icon+'.svg\')';
  }
}
