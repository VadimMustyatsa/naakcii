import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FinalizePageComponent} from "../../../finalize-page/finalize-page.component";
import {HomePageComponent} from "../../../home-page/home-page.component";
import {FoodsPageComponent} from "../../../foods-page/foods-page.component";


const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'form-shopping-list', component: FoodsPageComponent},
  {path: 'finalize-shopping-list', component: FinalizePageComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRouterModule {
}
