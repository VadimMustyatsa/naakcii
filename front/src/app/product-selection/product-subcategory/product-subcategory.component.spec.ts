import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductSubcategoryComponent } from './product-subcategory.component';

describe('ProductSubcategoryComponent', () => {
  let component: ProductSubcategoryComponent;
  let fixture: ComponentFixture<ProductSubcategoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductSubcategoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductSubcategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
