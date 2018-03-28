import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsSubCategoryComponent } from './foods-subCategory.component';

describe('FoodsSubCategoryComponent', () => {
  let component: FoodsSubCategoryComponent;
  let fixture: ComponentFixture<FoodsSubCategoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FoodsSubCategoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsSubCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
