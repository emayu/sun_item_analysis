import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadTransformFileComponent } from './upload-transform-file.component';

describe('UploadTransformFileComponent', () => {
  let component: UploadTransformFileComponent;
  let fixture: ComponentFixture<UploadTransformFileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UploadTransformFileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UploadTransformFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
