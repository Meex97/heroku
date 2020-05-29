import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupSupplierComponent } from './signup-supplier.component';

describe('SignupSupplierComponent', () => {
  let component: SignupSupplierComponent;
  let fixture: ComponentFixture<SignupSupplierComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SignupSupplierComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignupSupplierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
