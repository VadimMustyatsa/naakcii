import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShoppingListComponent } from "../../shopping-list/shopping-list.component";
import { HomePageComponent } from "../../home-page/home-page.component";
import { ProductSelectionComponent } from "../../product-selection/product-selection.component";
import { FinalizePageGuard } from "../guards/finalize-page.guard";
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { RequestInterceptorService} from "./request-interceptor.service";


const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'form-shopping-list', component: ProductSelectionComponent},
  {path: 'finalize-shopping-list', component: ShoppingListComponent, canActivate: [FinalizePageGuard]}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RequestInterceptorService,
      multi: true,
    }]
})
export class AppRouterModule {
}
