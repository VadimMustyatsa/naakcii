import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalizeFooterComponent } from './finalize-footer.component';

describe('FinalizeFooterComponent', () => {
  let component: FinalizeFooterComponent;
  let fixture: ComponentFixture<FinalizeFooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FinalizeFooterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FinalizeFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
