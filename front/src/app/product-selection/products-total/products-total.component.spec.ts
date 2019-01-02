import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductsTotalComponent } from './products-total.component';

describe('ProductsTotalComponent', () => {
  let component: ProductsTotalComponent;
  let fixture: ComponentFixture<ProductsTotalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductsTotalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductsTotalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
