import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MaterializeModule } from 'angular2-materialize';

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
import { FoodsGroupComponent } from './foods-page/foods-main-groups/foods-group.component';
import { FoodsSubGroupComponent } from './foods-page/foods-sub-group/foods-sub-group.component';
import { FoodsSortingComponent } from './foods-page/foods-sorting/foods-sorting.component';
import { FoodsFoodListComponent } from './foods-page/foods-food-list/foods-food-list.component';
import { FoodsTotalItemsComponent } from './foods-page/foods-total-items/foods-total-items.component';

const routes = [
  {path: '', component: HomePageComponent},
  {path: 'foods', component: FoodsPageComponent}
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
    FoodsGroupComponent,
    FoodsSubGroupComponent,
    FoodsSortingComponent,
    FoodsFoodListComponent,
    FoodsTotalItemsComponent
  ],
  imports: [
    BrowserModule,
    MaterializeModule,
    RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
