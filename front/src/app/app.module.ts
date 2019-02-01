import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MaterializeModule } from 'angular2-materialize';
import { NguCarouselModule } from '@ngu/carousel';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { AppComponent } from './app.component';
import { HomePageComponent } from './home-page/home-page.component';
import { ProductSelectionComponent } from './product-selection/product-selection.component';

import { ToolbarComponent } from './toolbar/toolbar.component';
import { HomeSliderComponent } from './home-page/home-slider/home-slider.component';
import { HomeStepsComponent } from './home-page/home-steps/home-steps.component';
import { HomeLayer1Component } from './home-page/home-layer1/home-layer1.component';
import { ProductSearchComponent } from './product-selection/product-search/product-search.component';
import { ProductCategoryComponent } from './product-selection/product-category/product-category.component';
import { ProductSubcategoryComponent } from './product-selection/product-subcategory/product-subcategory.component';
import { ProductSortingComponent } from './product-selection/product-sorting/product-sorting.component';
import { ProductListComponent } from './product-selection/product-list/product-list.component';
import { ProductsTotalComponent } from './product-selection/products-total/products-total.component';
import { FoodsCategoriesService } from './shared/category/foods.categories.service';
import { SHARED_STATE, SharedState } from './product-selection/sharedState.model';
import { Subject} from 'rxjs/Subject';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActionProductCardComponent } from './product-selection/product-list/action-product-card/action-product-card.component';
import { AppCutStringPipe } from './cutString.pipe';
import { ProductStorageListComponent } from './product-selection/product-storage-list/product-storage-list.component';
import { HttpClientModule } from '@angular/common/http';
import { FoodsStorageService } from './shared/Storage/foods.storage.service';
import { Cart } from './shared/cart/cart.model';
import { ShoppingListPageComponent } from './shopping-list-page/shopping-list-page.component';
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
import { SessionStorageService } from './shared/services/session-storage.service';
import { UndiscountService } from './shared/services/undiscount.service';
import { RestDataService } from './shared/services/rest-data.service';
import { HomePageService } from './home-page/home-page-service/home-page.service';
import { ScrollToTopDirective } from './shared/directives/scroll-to-top.directive';
import { EmailModalComponent } from './product-selection/email-modal/email-modal.component';
import { SubscribeService } from "./shared/services/subscribe.service";
import { DateGeneratorDirective } from './shared/directives/date-generator/date-generator.directive';
import { TooltipDirective } from "./shared/directives/tooltip/tooltip.directive";
import { ShoppingListComponent } from './shopping-list-page/shopping-list/shopping-list.component';
import { ChainLineComponent } from './shopping-list-page/shopping-list/chain-line/chain-line.component';
import { CartLineComponent } from './shopping-list-page/shopping-list/cart-line/cart-line.component';
import { FinalizeFooterComponent } from './shopping-list-page/finalize-footer/finalize-footer.component';
import { ModalFinalComponent } from './shopping-list-page/modal-final/modal-final.component';
import { FinalizeFooterButtonComponent } from './shopping-list-page/finalize-footer-button/finalize-footer-button.component';
import { UndiscountHeaderComponent } from './shopping-list-page/shopping-list/undiscount-header/undiscount-header.component';
import { UndiscountLinesComponent } from './shopping-list-page/shopping-list/undiscount-lines/undiscount-lines.component';
import { UndiscountAddComponent } from './shopping-list-page/shopping-list/undiscount-add/undiscount-add.component';
import { UndiscountLineComponent } from './shopping-list-page/shopping-list/undiscount-lines/undiscount-line/undiscount-line.component';

registerLocaleData(localeFr);

@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    ProductSelectionComponent,
    ToolbarComponent,
    HomeSliderComponent,
    HomeStepsComponent,
    HomeLayer1Component,
    ProductSearchComponent,
    ProductCategoryComponent,
    ProductSubcategoryComponent,
    ProductSortingComponent,
    ProductListComponent,
    ProductsTotalComponent,
    ActionProductCardComponent,
    ProductStorageListComponent,
    AppCutStringPipe,
    ProductStorageListComponent,
    ShoppingListPageComponent,
    HomeDiplomComponent,
    HomePartnersComponent,
    TooltipDirective,
    ScrollToTopDirective,
    EmailModalComponent,
    DateGeneratorDirective,
    ShoppingListComponent,
    ChainLineComponent,
    CartLineComponent,
    FinalizeFooterComponent,
    ModalFinalComponent,
    FinalizeFooterButtonComponent,
    UndiscountHeaderComponent,
    UndiscountLinesComponent,
    UndiscountAddComponent,
    UndiscountLineComponent
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
    SubscribeService,
    {provide: SHARED_STATE, useValue: new Subject<SharedState>() },
    { provide: LOCALE_ID, useValue: 'ru-BY'}],
  bootstrap: [AppComponent]
})
export class AppModule { }




