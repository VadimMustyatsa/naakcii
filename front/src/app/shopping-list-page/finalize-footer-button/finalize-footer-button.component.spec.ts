import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalizeFooterButtonComponent } from './finalize-footer-button.component';

describe('FinalizeFooterButtonComponent', () => {
  let component: FinalizeFooterButtonComponent;
  let fixture: ComponentFixture<FinalizeFooterButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FinalizeFooterButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FinalizeFooterButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
