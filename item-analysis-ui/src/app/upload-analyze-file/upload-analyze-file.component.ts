import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { AnalyzeFileService } from '../../services/analyze-file.service';
import { ResponseData } from '../../models/response';
import { CommonModule } from '@angular/common';

import { MatButtonModule } from '@angular/material/button';
import { MatDivider } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-upload-analyze-file',
  standalone: true,
  providers: [AnalyzeFileService],
  imports: [HttpClientModule,
    CommonModule,
    MatButtonModule,
    MatDivider,
    MatIconModule,
    MatTooltipModule,
    MatProgressSpinnerModule],
  templateUrl: './upload-analyze-file.component.html',
  styleUrl: './upload-analyze-file.component.scss'
})
export class UploadAnalyzeFileComponent {
  
  selectedFile:File | null = null;
  isSending = false;
  proccedData:ResponseData | null =  null;

  constructor(private analyzeService:AnalyzeFileService){

  }

  onFileChange(input:any){
    console.log(input);
    if(input?.files){
      this.selectedFile = input.files[0];
    }
    
  }

  downloadTextFile(){
    if (this.proccedData) {
      const fileName = this.proccedData?.data.source.split('.')[0];
      const blob = new Blob([this.proccedData.textReport], { type: 'text/plain' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${fileName}-resultado.txt`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    }
  }

  onSubmit(){
    console.log('here')
    if(this.selectedFile){
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      console.log('sending');
      this.isSending = true;
      this.analyzeService.analyzeFile(formData)
      .subscribe({
        next: (response) => {
          console.log('server response:', response.status);
          this.proccedData = response;
        },
        error: (err) => {
          console.error('error:', err)
        },
        complete:() =>{
          this.isSending = false;
        }
      })
    }
  }

}
