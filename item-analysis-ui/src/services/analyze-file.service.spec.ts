import { TestBed } from '@angular/core/testing';

import { AnalyzeFileService } from './analyze-file.service';

describe('AnalyzeFileService', () => {
  let service: AnalyzeFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnalyzeFileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
