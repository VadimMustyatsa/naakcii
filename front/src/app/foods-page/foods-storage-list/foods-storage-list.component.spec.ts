import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsStorageListComponent } from './foods-storage-list.component';

describe('FoodsStorageListComponent', () => {
  let component: FoodsStorageListComponent;
  let fixture: ComponentFixture<FoodsStorageListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FoodsStorageListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsStorageListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
