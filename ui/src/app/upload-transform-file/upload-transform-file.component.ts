import { Component, ViewChild } from '@angular/core';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormArray, FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UpdaloadFileService } from '../../services/analyze-file.service';
import { HttpClientModule } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { ResponseData } from '../../models/response';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatCheckboxChange, MatCheckboxModule } from '@angular/material/checkbox'
import { RouterModule } from '@angular/router';
import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';

@Component({
  selector: 'app-upload-transform-file',
  standalone: true,
  imports: [MatStepperModule, 
    MatButtonModule,
    MatInputModule, 
    MatProgressSpinnerModule, 
    MatDividerModule,
    MatIconModule,
    MatTableModule,
    MatCheckboxModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatMenuModule,
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

  displayedColumns:string[] = [];
  itemsConfigData:any[][]= [];
  formArrayInputs = new FormArray<FormControl>([]);

  @ViewChild(MatMenuTrigger)
  contextMenu: MatMenuTrigger | null = null;
  contextMenuPosition = { x: '0px', y: '0px' };

  configFormGroup = this.formBuilder.group({
    syllabus: ['', Validators.required],
    line1: ['', Validators.required],
    line2: ['', [Validators.required, Validators.pattern(/^[A-Z]+$/)]],
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

  onContextMenu(event: MouseEvent){
    event.preventDefault();
    if(this.line2.value && this.line2.invalid){
      this.contextMenuPosition.x = event.clientX + 'px';
      this.contextMenuPosition.y = event.clientY + 'px';
      this.contextMenu?.menu?.focusFirstItem('mouse');
      this.contextMenu?.openMenu();
    }
  }

  removeBlankSpacesOnLine2(){
    const newValue = this.line2.value?.replace(/\s/g,'');
    this.line2.setValue(newValue??"");
  }

  transform(stepper:MatStepper|null){
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
          if(stepper){
            stepper.next();
          }
          
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
          if(stepper){
            stepper.next();
          }
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

  toggleActivateItem(event:MatCheckboxChange, colIndex:number){
    const activatedRowIndex = 2;
    const rowData=  this.itemsConfigData[activatedRowIndex];
    rowData[colIndex] = !rowData[colIndex];
    const line4 = rowData.reduce((acc: string, val: boolean) =>
      acc + (val ? "Y" : "N")
      , "");
     this.line4.setValue(line4) ;
    
  }

  onStepChange(event:StepperSelectionEvent){
    if(event.selectedIndex == 2 && event.previouslySelectedIndex !=3){//Revisión de ítems tab
      this.recalculateTable();
    }
    if(event.selectedIndex == 3){ //Completado
      this.transform(null);
    }
  }
  

  updateLine2(){
    const rowKeyConfig = 0;
    const controlRow = this.itemsConfigData[rowKeyConfig];
    const line2 = controlRow.reduce( (acc, ctrl:FormControl) => acc + ctrl.value, "")
    this.line2.setValue(line2);
  }

  recalculateTable() {
    let max = (this.line2.value?.length || 0) > (this.line3.value?.length || 0) ? this.line2.value?.length : this.line3.value?.length;
    max = (max || 0) > (this.line4.value?.length || 0) ? (max || 0) : this.line4.value?.length || 0;
    
    this.displayedColumns = [];
    this.formArrayInputs = new FormArray<FormControl>([]);
    const answerRow = [];
    const amountRow = [];
    const activatedRow = [];
    for (let i = 0; i < max; i++) {
      this.displayedColumns.push(`Ítem ${i + 1}`);
      const answerRawValue = this.line2.value?.charAt(i);
      if( answerRawValue ) {
        let ctrl = new FormControl(answerRawValue, [Validators.required, Validators.pattern(/^[A-Z]{1}$/)]);
        ctrl.valueChanges.subscribe( () => {
          if(ctrl.valid){
            this.updateLine2();
          }
        });
        answerRow.push(ctrl);
        this.formArrayInputs.push(ctrl);
      } else {
        answerRow.push(null);
      }
      amountRow.push(this.line3.value?.charAt(i));
      const rawValue = this.line4.value?.charAt(i);
      if(rawValue){
        activatedRow.push(rawValue == 'Y'? true : false);
      }else{
        activatedRow.push(null)
      }
    }
    this.itemsConfigData = [ answerRow, amountRow, activatedRow];
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
    let spacesAtBeginig;
    if(amount <= 9){
      spacesAtBeginig = "  ";//two spaces
    }else if(amount <=99){
      spacesAtBeginig = " ";//one space
    }else{
      spacesAtBeginig = ""//not spaces for 100 to 999
    }
    return `${spacesAtBeginig}${amount} Z O 0`;
  }

}
