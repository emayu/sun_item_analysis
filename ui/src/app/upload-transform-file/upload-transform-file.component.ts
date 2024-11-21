import { Component } from '@angular/core';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UpdaloadFileService } from '../../services/analyze-file.service';
import { HttpClientModule } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { ResponseData } from '../../models/response';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-upload-transform-file',
  standalone: true,
  imports: [MatStepperModule, 
    MatButtonModule,
    MatInputModule, 
    MatProgressSpinnerModule, 
    MatDividerModule,
    MatIconModule,
    ReactiveFormsModule,
    CommonModule, 
    RouterModule,
    HttpClientModule],
  providers: [UpdaloadFileService],
  templateUrl: './upload-transform-file.component.html',
  styleUrl: './upload-transform-file.component.scss'
})
export class UploadTransformFileComponent {
  selectedFile:File | null = null;
  isSending = false;
  proccedData:ResponseData|null = null;
  error: string |null = null;
  hasDownloaded = false;

  configFormGroup = this.formBuilder.group({
    syllabus: ['', Validators.required],
    line1: ['', Validators.required],
    line2: ['', [Validators.required, Validators.pattern(/^[A-Za-z]+$/)]],
    line3: ['', [Validators.required, Validators.pattern(/^[1-9]+$/)]],
    line4: ['', [Validators.required, Validators.pattern(/^[Y|N]+$/)]],
  })

  constructor(
    private formBuilder:FormBuilder,
    private processFileService:UpdaloadFileService
  ){
  }

  onFileChange(input:any){
    if(input?.files){
      this.selectedFile = input.files[0];
    }
  }

  transform(stepper:MatStepper){
    if(this.selectedFile){
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      formData.append('syllabus', this.syllabus.value as string);
      formData.append('line1', this.line1.value as string);
      formData.append('line2', this.line2.value as string);
      formData.append('line3', this.line3.value as string);
      formData.append('line4', this.line4.value as string);
      this.isSending = true;
      //reseting values
      this.error = null;
      this.proccedData = null;
      this.processFileService.transformFile(formData)
      .subscribe({
        next: (response) => {
          this.proccedData = response;
          stepper.next();
        },
        error: (err) => {
          this.isSending = false;
          if(err.error && err.error.message){
            this.error = err.error.message;
          }else if(err.message){
            this.error = err.message;
          }else{
            this.error = err;
          }
          stepper.next();
        },
        complete: () =>{
          this.isSending = false;
        }
      })
    }
    

    
  }

  downloadTextFile(){
    if(this.proccedData){
      const fileName = this.selectedFile?.name.split('.')[0];
      const blob = new Blob([this.proccedData.data], { type: 'text/plain' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${fileName}.config.txt`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url)
      this.hasDownloaded = true;
    }
  }

  onStepChange(event:any){
    console.log(event);
  }

  interacted(event:any){
    console.log(event);
  }

  autoGenerateConfig(){
    if(this.line2.value){
      let amountAnswers = this.line2.value.length;
      this.line1.setValue(this.getFirstLine(amountAnswers));
      let line3 = "";
      let line4 = "";
      for(let i = 1; i <= amountAnswers; i++){
        line3 = line3.concat("4");
        line4 = line4.concat("Y");
      }
      this.line3.setValue(line3);
      this.line4.setValue(line4);
    }
  }

  get syllabus(){
    return this.configFormGroup.get('syllabus')!;
  }

  get line1 (){
    return this.configFormGroup.get('line1')!;
  }

  get line2 (){
    return this.configFormGroup.get('line2')!;
  }

  get line3(){
    return this.configFormGroup.get('line3')!;
  }

  get line4(){
    return this.configFormGroup.get('line4')!;
  }

  private getFirstLine(amount:number) {
    return ` ${amount} Z O 0`;
  }

}
