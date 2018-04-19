import {Component, Input, Inject, OnInit} from '@angular/core';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observable} from 'rxjs/Observable';
import {SubCategory} from '../../shared/subCategory/foods.subCategory.model';
import {FoodsSubCategoriesService} from '../../shared/subCategory/foods.subCategory.service';
import {Category} from '../../shared/category/foods.category.model';
import {Observer} from 'rxjs/Observer';

@Component({
  selector: 'app-foods-subCategory',
  templateUrl: './foods-subCategory.component.html',
  styleUrls: ['./foods-subCategory.component.css'],
  providers: [FoodsSubCategoriesService]
})
export class FoodsSubCategoryComponent implements OnInit {
  curCategory: Category;
  subCategoryList: SubCategory[];
  checkedAll = false;  //выбор всех подкатегорий
  private isShowedTapStep3 = false;

  constructor(private service: FoodsSubCategoriesService,
              @Inject(SHARED_STATE) private observer: Observer<SharedState>,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
  }

  ngOnInit() {
    console.log('SubCategoryComponent - ngOnInit');
    this.stateEvents.subscribe((update) => {
      if (update.mode === MODES.SELECT_CATEGORY) {
        console.log('stateEvents: ' + update.category.id + ':' + update.category.name);
        this.subCategoryList = null;
        this.curCategory = update.category;

        this.service.getByCategory(update.category.id).subscribe(subCategoryList => {
          this.subCategoryList = subCategoryList;
          console.log(this.subCategoryList);
          this.setAllItemsByCheckedAll();
          if (!this.isShowedTapStep3) {
            //setTimeout("$('.tapStep3').tapTarget('open')", 500);
            this.isShowedTapStep3 = true;
          } else {

          }
        });

      }
    });
  }

  setAllItemsByCheckedAll() {
    if (this.subCategoryList) {
      for (let i = 0; i < this.subCategoryList.length; i++) {
        this.subCategoryList[i].selected = this.checkedAll;
      }
    }
    this.observer.next(new SharedState(MODES.SELECT_SUBCATEGORY, this.curCategory, this.subCategoryList));
  }
  onChangeAll() {
    this.checkedAll = !this.checkedAll;
    this.setAllItemsByCheckedAll();
  }

  onChangeItem(idSubCut) {
    this.subCategoryList[idSubCut].selected = !this.subCategoryList[idSubCut].selected;
    this.correctAllItemsCheck();
    this.observer.next(new SharedState(MODES.SELECT_SUBCATEGORY, this.curCategory, this.subCategoryList));
  }

  correctAllItemsCheck() {
    if (this.subCategoryList) {
      const curCheck = this.subCategoryList[0].selected;
      for (let i = 1; i < this.subCategoryList.length; i++) {
        if (curCheck !== this.subCategoryList[i].selected) {
          this.checkedAll = false;
          return;
        }
      }
      this.checkedAll = curCheck;
    }
  }
}
