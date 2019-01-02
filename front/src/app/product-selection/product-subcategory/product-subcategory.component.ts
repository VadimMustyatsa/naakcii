import {Component, Input, Inject, OnInit, HostListener} from '@angular/core';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observable} from 'rxjs/Observable';
import {SubCategory} from '../../shared/subCategory/foods.subCategory.model';
import {FoodsSubCategoriesService} from '../../shared/subCategory/foods.subCategory.service';
import {Category} from '../../shared/category/foods.category.model';
import {Observer} from 'rxjs/Observer';

@Component({
  selector: 'app-foods-subCategory',
  templateUrl: './product-subcategory.component.html',
  styleUrls: ['./product-subcategory.component.scss'],
  providers: [FoodsSubCategoriesService]
})
export class ProductSubcategoryComponent implements OnInit {
  curCategory: Category;
  subCategoryList: SubCategory[];

  constructor(private service: FoodsSubCategoriesService,
              @Inject(SHARED_STATE) private observer: Observer<SharedState>,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
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
}
