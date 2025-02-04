import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { Component, isDevMode } from '@angular/core';
import { UpdaloadFileService } from '../../services/analyze-file.service';
import { ResponseData } from '../../models/response';
import { CommonModule } from '@angular/common';

import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-upload-analyze-file',
  standalone: true,
  providers: [UpdaloadFileService],
  imports: [HttpClientModule,
    CommonModule,
    MatButtonModule,
    MatDividerModule,
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
  isDevMode = isDevMode();

  constructor(
    private processFileService:UpdaloadFileService,
    private _snackBar: MatSnackBar
  ){}

  onFileChange(input:any){
    if(input?.files){
      this.selectedFile = input.files[0];
      input.value = ''
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
    if(this.selectedFile){
      this.proccedData = null;
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      console.log('sending');
      this.isSending = true;
      this.processFileService.analyzeFile(formData)
      .subscribe({
        next: (response) => {
          console.log('server response:', response.status);
          this._snackBar.open("Completado", "", {duration:2500});
          this.proccedData = response;
        },
        error: (err) => {
          console.error('error:', err);
          this.isSending = false;
          if(err instanceof HttpErrorResponse){
            console.log('is HttpErrorResponse');
          }
          if(err.error && err.error.message){
            this._snackBar.open(`Error:\n${err.error.message}`, "Cerrar", {panelClass: "snackbar-error"});
          }else{
            this._snackBar.open(`Ocurrió un error`, "Cerrar", {panelClass: "snackbar-error"});
          }
        },
        complete:() =>{
          this.isSending = false;
        }
      })
    }
  }

}
