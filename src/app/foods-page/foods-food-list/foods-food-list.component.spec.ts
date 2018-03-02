import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsFoodListComponent } from './foods-food-list.component';

describe('FoodsFoodListComponent', () => {
  let component: FoodsFoodListComponent;
  let fixture: ComponentFixture<FoodsFoodListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FoodsFoodListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsFoodListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
