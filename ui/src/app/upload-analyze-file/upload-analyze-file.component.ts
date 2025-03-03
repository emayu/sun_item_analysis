import { HttpClient, HttpClientModule, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, isDevMode, Inject } from '@angular/core';
import { UpdaloadFileService } from '../../services/analyze-file.service';
import { ResponseData } from '../../models/response';
import { CommonModule } from '@angular/common';

import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { MatMenuModule } from '@angular/material/menu';
import { 
  MatDialog,
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogTitle,
  MatDialogContent,
  MatDialogActions,
  MatDialogClose
 } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatStepperModule } from '@angular/material/stepper';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../services/report.service';
import { Observable } from 'rxjs';
import { AppFilePreviewComponent } from '../app-file-preview/app-file-preview.component';

export interface DialogData {
  subtitle:string;
}

@Component({
  selector: 'app-upload-analyze-file',
  standalone: true,
  providers: [UpdaloadFileService, ReportService],
  imports: [HttpClientModule,
    CommonModule,
    MatButtonModule,
    MatDividerModule,
    MatIconModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    MatMenuModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    AppFilePreviewComponent,
    MatStepperModule,
  ],
  templateUrl: './upload-analyze-file.component.html',
  styleUrl: './upload-analyze-file.component.scss'
})
export class UploadAnalyzeFileComponent {
  
  selectedFile:File | null = null;
  isSending = false;
  proccededData:ResponseData | null =  null;
  isDevMode = isDevMode();
  error: string |null = null;

  constructor(
    private processFileService:UpdaloadFileService,
    private _snackBar: MatSnackBar,
    private reportingService:ReportService,
    public dialog: MatDialog
  ){}

  onFileChange(input:any){
    if(input?.files){
      this.selectedFile = input.files[0];
      input.value = ''
    }

  }

  private downloadBlobToFile(blob: Blob, fileName: string) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }

  

  downloadTextFile(){
    if (this.proccededData) {
      const originalFileName = this.proccededData?.data.source.split('.')[0];
      const blob = new Blob([this.proccededData.textReport], { type: 'text/plain' });
      const fileName = `${originalFileName}-resultado.txt`;
      this.downloadBlobToFile(blob,fileName);
    }
  }

  private unwrapRawJson(data:Blob):Promise<any>{
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        try{
          resolve(JSON.parse(reader.result as string));
        }catch(e){
          console.log('no se pudo leer la respuesta', e);
          reject(null);
        }
      }
      reader.readAsText(data);
    });
  }

  private async showErrors(error: any) {
    console.error('error:', error);
    this.isSending = false;
    if (error instanceof HttpErrorResponse) {
      console.log('is HttpErrorResponse');
    }
    if ( error.error instanceof Blob ){
      error.error = await this.unwrapRawJson(error.error);
    }

    if (error.error && error.error.message) {
      this.error = error.error.message;
      this._snackBar.open(`Error:\n${this.error}`, "Cerrar", { panelClass: "snackbar-error" , duration:4500 });
    } else {
      this.error = error?.message || `Ocurrió un error desconocido`;
      this._snackBar.open(this.error!, "Cerrar", { panelClass: "snackbar-error", duration:4500 });
    }
  }

  onSubmit(){
    if(this.selectedFile){
      this.proccededData = null;
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      console.log('sending');
      this.isSending = true;
      this.error = null;
      this.processFileService.analyzeFile(formData)
      .subscribe({
        next: (response) => {
          console.log('server response:', response.status);
          this._snackBar.open("Completado", "", {duration:2500});
          this.proccededData = response;
        },
        error: (err) => {
          this.showErrors(err);
        },
        complete:() =>{
          this.isSending = false;
        }
      })
    }
  }

  private generatePDF(service: Observable<HttpResponse<Blob>>){
    this.isSending = true;
    service.subscribe({
      next: (response) => {
        this.isSending = false;
        console.log(response);
        const contentDisposition = response.headers.get('Content-Disposition');
        console.log(contentDisposition);
        let fileName = "reporte.xlsx";
        if(contentDisposition){
          const match = contentDisposition.match(/filename="(.+)"/);
          if(match){
            fileName = match[1];
          }
        }
        const url = window.URL.createObjectURL(response.body as Blob);
        const newWindow = window.open(url)!;
        setTimeout(()=> newWindow.document.title=fileName, 500);
      },
      error: async (err) => {
        await this.showErrors(err);
        this.error = null;//clean the error from UI, its is shown in snack bar only
      }
    })
  }


  generateScoreDistributionPDF(){
    if(this.proccededData){
      const observable = this.reportingService.downloadScoreDistributionReportPDF(this.proccededData.data);
      this.generatePDF(observable);
    }
  }

  private generateXLS(service: Observable<HttpResponse<Blob>>){
    this.isSending = true;
    service.subscribe({
        next: (response) => {
          this.isSending = false;
          console.log(response);
          const contentDisposition = response.headers.get('Content-Disposition');
          console.log(contentDisposition);
          let fileName = "reporte.xlsx";
          if(contentDisposition){
            const match = contentDisposition.match(/filename="(.+)"/);
            if(match){
              fileName = match[1];
            }
          }
          this.downloadBlobToFile(response.body as Blob, fileName);
        },
        error: async (err) => {
          await this.showErrors(err);
          this.error = null;//clean the error from UI, its is shown in snack bar only
        }
      })
  }

  generateScoreDistributionReportXLS(){
    if(this.proccededData){
      const obs =  this.reportingService.downloadScoreDistributionReportXls(this.proccededData.data)
      this.generateXLS(obs);
    }
  }

  generateResultReportPDF() {
    if (this.proccededData) {
      const dialogRef = this.dialog.open(ReportTitleDialog, { data: { subtitle: "" } });
      const self = this;
      dialogRef.afterClosed().subscribe( (result:DialogData) => {
        if(result){
          const observable = this.reportingService.downloadResultReporPDF(result.subtitle, self.proccededData!.data);
          this.generatePDF(observable);
        }
      });
    }
  }

  generateResultReporXLS(){
    if (this.proccededData) {
      const dialogRef = this.dialog.open(ReportTitleDialog, { data: { subtitle: "" } });
      const self = this;
      dialogRef.afterClosed().subscribe( (result:DialogData) => {
        if(result){
          const observable = this.reportingService.downloadResultReporXLS(result.subtitle, self.proccededData!.data);
          this.generateXLS(observable);
        }
      });
    }
  }

}
@Component({
  selector:'report-title-dialog',
  templateUrl:'report-title-dialog.html',
  standalone:true,
  imports:[ 
    MatFormFieldModule,
    MatInputModule, 
    FormsModule, 
    MatButtonModule,
    MatButtonModule, 
    MatDialogTitle, 
    MatDialogContent, 
    MatDialogActions, 
    MatDialogClose]
})
export class ReportTitleDialog{

  subtitle:string|null = null;

  constructor(public dialogRef:MatDialogRef<ReportTitleDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ){}

  cancel(){
    this.dialogRef.close();
  }

  
}
