import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CopyField } from './copy-field';

describe('CopyField', () => {
  let component: CopyField;
  let fixture: ComponentFixture<CopyField>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CopyField]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CopyField);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
