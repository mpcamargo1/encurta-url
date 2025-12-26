import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncurtarLinkInput } from './encurtar-link-input';

describe('EncurtarLinkInput', () => {
  let component: EncurtarLinkInput;
  let fixture: ComponentFixture<EncurtarLinkInput>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EncurtarLinkInput]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EncurtarLinkInput);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
