import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FinalizePageComponent } from "../../finalize-page/finalize-page.component";
import { HomePageComponent } from "../../home-page/home-page.component";
import { FoodsPageComponent } from "../../foods-page/foods-page.component";
import { FinalizePageGuard } from "../guards/finalize-page.guard";
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { RequestInterceptorService} from "./request-interceptor.service";


const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'form-shopping-list', component: FoodsPageComponent},
  {path: 'finalize-shopping-list', component: FinalizePageComponent, canActivate: [FinalizePageGuard]}
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
