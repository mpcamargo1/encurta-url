import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncurtarLinkForm } from './encurtar-link-form';

describe('EncurtarLinkForm', () => {
  let component: EncurtarLinkForm;
  let fixture: ComponentFixture<EncurtarLinkForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EncurtarLinkForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EncurtarLinkForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
