import { TestBed } from '@angular/core/testing';

import { EncurtarLinkService } from './encurtar-link-service';

describe('EncurtarLinkService', () => {
  let service: EncurtarLinkService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EncurtarLinkService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
