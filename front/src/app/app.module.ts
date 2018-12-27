import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MaterializeModule } from 'angular2-materialize';
import { NguCarouselModule } from '@ngu/carousel';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { AppComponent } from './app.component';
import { HomePageComponent } from './home-page/home-page.component';
import { FoodsPageComponent } from './foods-page/foods-page.component';

import { ToolbarComponent } from './toolbar/toolbar.component';
import { HomeSliderComponent } from './home-page/home-slider/home-slider.component';
import { HomeStepsComponent } from './home-page/home-steps/home-steps.component';
import { HomeLayer1Component } from './home-page/home-layer1/home-layer1.component';
import { FoodsSearchComponent } from './foods-page/foods-search/foods-search.component';
import { FoodsCategoryComponent } from './foods-page/foods-category/foods-category.component';
import { FoodsSubCategoryComponent } from './foods-page/foods-subCategory/foods-subCategory.component';
import { FoodsSortingComponent } from './foods-page/foods-sorting/foods-sorting.component';
import { FoodsFoodListComponent } from './foods-page/foods-food-list/foods-food-list.component';
import { FoodsTotalItemsComponent } from './foods-page/foods-total-items/foods-total-items.component';
import { FoodsCategoriesService } from './shared/category/foods.categories.service';
import { SHARED_STATE, SharedState } from './foods-page/sharedState.model';
import { Subject} from 'rxjs/Subject';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FoodsFoodCardComponent } from './foods-page/foods-food-list/foods-food-card/foods-food-card.component';
import { AppCutStringPipe } from './cutString.pipe';
import { FoodsStorageListComponent } from './foods-page/foods-storage-list/foods-storage-list.component';
import { HttpClientModule } from '@angular/common/http';
import { FoodsStorageService } from './shared/Storage/foods.storage.service';
import { Cart } from './shared/cart/cart.model';
import { FinalizePageComponent } from './finalize-page/finalize-page.component';
import { LayoutModule } from '@angular/cdk/layout';
import { BreakPointCheckService } from './shared/services/breakpoint-check.service';
import { Chain} from './shared/chain/chain.model';
import { LOCALE_ID} from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/ru';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeDiplomComponent } from './home-page/home-diplom/home-diplom.component';
import { HomePartnersComponent } from './home-page/home-partners/home-partners.component';
import { PdfGeneratorService } from './shared/services/pdf-generator.service';
import { AppRouterModule } from './shared/routing/router.module';
import { FinalizePageGuard } from './shared/guards/finalize-page.guard';
import { TooltipDirective } from './shared/directives/tooltip.directive';
import { SessionStorageService } from './shared/services/session-storage.service';
import { UndiscountService } from './shared/services/undiscount.service';
import { RestDataService } from './shared/services/rest-data.service';
import { HomePageService } from './home-page/home-page-service/home-page.service';
import { ScrollToTopDirective } from './shared/directives/scroll-to-top.directive';

registerLocaleData(localeFr);

@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    FoodsPageComponent,
    ToolbarComponent,
    HomeSliderComponent,
    HomeStepsComponent,
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
    FoodsStorageListComponent,
    FinalizePageComponent,
    HomeDiplomComponent,
    HomePartnersComponent,
    TooltipDirective,
    ScrollToTopDirective
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    MaterializeModule,
    AppRouterModule,
    HttpClientModule,
    NguCarouselModule,
    InfiniteScrollModule,
    LayoutModule,
    BrowserAnimationsModule
  ],
  providers: [
    FoodsStorageService,
    HomePageService,
    Chain,
    Cart,
    FoodsCategoriesService,
    BreakPointCheckService,
    PdfGeneratorService,
    SessionStorageService,
    UndiscountService,
    FinalizePageGuard,
    RestDataService,
    {provide: SHARED_STATE, useValue: new Subject<SharedState>() },
    { provide: LOCALE_ID, useValue: 'ru-BY'}],
  bootstrap: [AppComponent]
})
export class AppModule { }




