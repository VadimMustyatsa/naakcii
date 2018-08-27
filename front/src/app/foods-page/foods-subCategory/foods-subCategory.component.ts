import {Component, Input, Inject, OnInit, HostListener} from '@angular/core';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observable} from 'rxjs/Observable';
import {SubCategory} from '../../shared/subCategory/foods.subCategory.model';
import {FoodsSubCategoriesService} from '../../shared/subCategory/foods.subCategory.service';
import {Category} from '../../shared/category/foods.category.model';
import {Observer} from 'rxjs/Observer';

@Component({
  selector: 'app-foods-subCategory',
  templateUrl: './foods-subCategory.component.html',
  styleUrls: ['./foods-subCategory.component.scss'],
  providers: [FoodsSubCategoriesService]
})
export class FoodsSubCategoryComponent implements OnInit {
  fixedPaddingTop = 0;
  curCategory: Category;
  subCategoryList: SubCategory[];

  constructor(private service: FoodsSubCategoriesService,
              @Inject(SHARED_STATE) private observer: Observer<SharedState>,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
  }

  @HostListener('window:scroll', ['$event']) onScrollEvent($event){
    const verticalOffset = window.pageYOffset
      || document.documentElement.scrollTop
      || document.body.scrollTop || 0;
    if (verticalOffset > 280) {
      // this.fixedPaddingTop = verticalOffset - 300;
    } else {
      this.fixedPaddingTop = 0;
    }
  }

  ngOnInit() {
    this.stateEvents.subscribe((update) => {
      if (update.mode === MODES.SELECT_CATEGORY) {
        this.service.getByCategory(update.category.id).subscribe(subCategoryList => {
          this.subCategoryList = null;
          this.curCategory = update.category;
          this.subCategoryList = subCategoryList;
          this.subCategoryList[0].selected = true;
          this.observer.next(new SharedState(MODES.SELECT_SUBCATEGORY, this.curCategory, this.subCategoryList));
        });
      }
    });
  }

  onChangeItem(idSubCut) {
    this.subCategoryList.map(el => {
      el.selected = (el.id === idSubCut);
    });
    this.observer.next(new SharedState(MODES.SELECT_SUBCATEGORY, this.curCategory, this.subCategoryList));
  }
  getPaddingTop() {
    return (String(this.fixedPaddingTop) + 'px');
  }
}
