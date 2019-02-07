import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductLineComponent } from './cart-line.component';

describe('ProductLineComponent', () => {
  let component: ProductLineComponent;
  let fixture: ComponentFixture<ProductLineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductLineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
