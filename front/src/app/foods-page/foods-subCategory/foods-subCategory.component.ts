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
  checkedAll = true;  //выбор всех подкатегорий по-умолчанию
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
        this.service.getByCategory(update.category.id).subscribe(subCategoryList => {
          this.subCategoryList = null;
          this.curCategory = update.category;
          this.subCategoryList = subCategoryList;
          console.log(this.subCategoryList);
          this.checkedAll = true;
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
    this.subCategoryList.map(el => {
      el.selected = this.checkedAll
    });
    console.log('setAllItemsByCheckedAll');
    this.observer.next(new SharedState(MODES.SELECT_SUBCATEGORY, this.curCategory, this.subCategoryList));
  }

  onChangeAll() {
    this.checkedAll = !this.checkedAll;
    this.setAllItemsByCheckedAll();
  }

  onChangeItem(idSubCut) {
    console.log('onChangeItem: ' + idSubCut);
    this.subCategoryList.map(el => {
      if (el.id === idSubCut) {
        el.selected = !el.selected;
      }
    });
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
