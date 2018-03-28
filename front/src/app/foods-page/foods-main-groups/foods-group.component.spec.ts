import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsGroupComponent } from './foods-group.component';

describe('FoodsGroupComponent', () => {
  let component: FoodsGroupComponent;
  let fixture: ComponentFixture<FoodsGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FoodsGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
