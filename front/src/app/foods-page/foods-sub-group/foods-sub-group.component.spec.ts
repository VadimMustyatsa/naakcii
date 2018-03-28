import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsSubGroupComponent } from './foods-sub-group.component';

describe('FoodsSubGroupComponent', () => {
  let component: FoodsSubGroupComponent;
  let fixture: ComponentFixture<FoodsSubGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FoodsSubGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsSubGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
