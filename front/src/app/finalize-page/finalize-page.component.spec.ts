import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalizePageComponent } from './finalize-page.component';

describe('FinalizePageComponent', () => {
  let component: FinalizePageComponent;
  let fixture: ComponentFixture<FinalizePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FinalizePageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FinalizePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
