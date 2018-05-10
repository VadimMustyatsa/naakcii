import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MaterializeModule } from 'angular2-materialize';
import { NguCarouselModule } from '@ngu/carousel';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { AppComponent } from './app.component';
import { HomePageComponent } from './home-page/home-page.component';
import { FoodsPageComponent } from './foods-page/foods-page.component';
import {RouterModule} from '@angular/router';

import { ToolbarComponent } from './toolbar/toolbar.component';
import { HomeSliderComponent } from './home-page/home-slider/home-slider.component';
import { HomeStepsComponent } from './home-page/home-steps/home-steps.component';
import { HomeStepComponent } from './home-page/home-steps/home-step/home-step.component';
import { HomeLayer1Component } from './home-page/home-layer1/home-layer1.component';
import { FoodsSearchComponent } from './foods-page/foods-search/foods-search.component';
import { FoodsCategoryComponent } from './foods-page/foods-category/foods-category.component';
import { FoodsSubCategoryComponent } from './foods-page/foods-subCategory/foods-subCategory.component';
import { FoodsSortingComponent } from './foods-page/foods-sorting/foods-sorting.component';
import { FoodsFoodListComponent } from './foods-page/foods-food-list/foods-food-list.component';
import { FoodsTotalItemsComponent } from './foods-page/foods-total-items/foods-total-items.component';
import {FoodsCategoriesService} from './shared/category/foods.categories.service';
import {SHARED_STATE, SharedState} from './foods-page/sharedState.model';
import {Subject} from 'rxjs/Subject';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {FoodsFoodCardComponent} from './foods-page/foods-food-list/foods-food-card/foods-food-card.component';
import {AppCutStringPipe} from './cutString.pipe';
import { FoodsStorageListComponent } from './foods-page/foods-storage-list/foods-storage-list.component';
import { HttpClientModule } from '@angular/common/http';

const routes = [
  {path: '', component: HomePageComponent},
  {path: 'form-shopping-list', component: FoodsPageComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    FoodsPageComponent,
    ToolbarComponent,
    HomeSliderComponent,
    HomeStepsComponent,
    HomeStepComponent,
    HomeLayer1Component,
    FoodsSearchComponent,
    FoodsCategoryComponent,
    FoodsSubCategoryComponent,
    FoodsSortingComponent,
    FoodsFoodListComponent,
    FoodsTotalItemsComponent,
    FoodsFoodCardComponent,
    FoodsStorageListComponent,
    AppCutStringPipe,
    FoodsStorageListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule, ReactiveFormsModule,
    MaterializeModule,
    RouterModule.forRoot(routes),
    HttpClientModule,
    NguCarouselModule,
    InfiniteScrollModule
  ],
  providers: [FoodsCategoriesService, {provide: SHARED_STATE, useValue: new Subject<SharedState>() }],
  bootstrap: [AppComponent]
})
export class AppModule { }
