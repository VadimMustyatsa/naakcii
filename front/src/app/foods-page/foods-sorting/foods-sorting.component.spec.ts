import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsSortingComponent } from './foods-sorting.component';

describe('FoodsSortingComponent', () => {
  let component: FoodsSortingComponent;
  let fixture: ComponentFixture<FoodsSortingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FoodsSortingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsSortingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
