import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeDiplomComponent } from './home-diplom.component';

describe('HomeDiplomComponent', () => {
  let component: HomeDiplomComponent;
  let fixture: ComponentFixture<HomeDiplomComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeDiplomComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeDiplomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
