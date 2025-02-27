import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppFilePreviewComponent } from './app-file-preview.component';

describe('AppFilePreviewComponent', () => {
  let component: AppFilePreviewComponent;
  let fixture: ComponentFixture<AppFilePreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppFilePreviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AppFilePreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
