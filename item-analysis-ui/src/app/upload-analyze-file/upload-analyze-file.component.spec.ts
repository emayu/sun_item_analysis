import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadAnalyzeFileComponent } from './upload-analyze-file.component';

describe('UploadAnalyzeFileComponent', () => {
  let component: UploadAnalyzeFileComponent;
  let fixture: ComponentFixture<UploadAnalyzeFileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UploadAnalyzeFileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UploadAnalyzeFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
