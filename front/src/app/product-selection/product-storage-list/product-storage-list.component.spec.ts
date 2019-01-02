import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductStorageListComponent } from './product-storage-list.component';

describe('ProductStorageListComponent', () => {
  let component: ProductStorageListComponent;
  let fixture: ComponentFixture<ProductStorageListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductStorageListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductStorageListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
