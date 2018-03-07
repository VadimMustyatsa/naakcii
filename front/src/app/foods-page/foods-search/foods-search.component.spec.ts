import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodsSearchComponent } from './foods-search.component';

describe('FoodsSearchComponent', () => {
  let component: FoodsSearchComponent;
  let fixture: ComponentFixture<FoodsSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FoodsSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FoodsSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
