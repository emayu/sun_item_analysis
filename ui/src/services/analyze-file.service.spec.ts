import { TestBed } from '@angular/core/testing';

import { UpdaloadFileService } from './analyze-file.service';

describe('AnalyzeFileService', () => {
  let service: UpdaloadFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UpdaloadFileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
