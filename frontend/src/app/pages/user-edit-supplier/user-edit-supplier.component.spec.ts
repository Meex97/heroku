import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEditSupplierComponent } from './user-edit-supplier.component';

describe('UserEditSupplierComponent', () => {
  let component: UserEditSupplierComponent;
  let fixture: ComponentFixture<UserEditSupplierComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserEditSupplierComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserEditSupplierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
