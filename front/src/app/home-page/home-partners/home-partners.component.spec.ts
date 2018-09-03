import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePartnersComponent } from './home-partners.component';

describe('HomePartnersComponent', () => {
  let component: HomePartnersComponent;
  let fixture: ComponentFixture<HomePartnersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomePartnersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomePartnersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
