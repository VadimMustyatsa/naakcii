import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsTotalItemsComponent } from './foods-total-items.component';

describe('FoodsTotalItemsComponent', () => {
  let component: FoodsTotalItemsComponent;
  let fixture: ComponentFixture<FoodsTotalItemsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FoodsTotalItemsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsTotalItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
