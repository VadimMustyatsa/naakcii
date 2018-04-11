import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeStepsComponent } from './home-steps.component';

describe('HomeStepsComponent', () => {
  let component: HomeStepsComponent;
  let fixture: ComponentFixture<HomeStepsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeStepsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeStepsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
