import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailModalComponent } from './email-modal.component';

describe('EmailModalComponent', () => {
  let component: EmailModalComponent;
  let fixture: ComponentFixture<EmailModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmailModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmailModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
