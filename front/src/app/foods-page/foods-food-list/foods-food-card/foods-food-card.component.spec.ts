import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsFoodCardComponent } from './foods-food-card.component';

describe('FoodsFoodCardComponent', () => {
  let component: FoodsFoodCardComponent;
  let fixture: ComponentFixture<FoodsFoodCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FoodsFoodCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsFoodCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
