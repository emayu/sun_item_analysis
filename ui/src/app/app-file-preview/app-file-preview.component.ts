import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDividerModule } from '@angular/material/divider';

const PREVIEW_AMOUNT_LINES = 20;

@Component({
  selector: 'app-app-file-preview',
  standalone: true,
  imports: [
    CommonModule,
    MatDividerModule],
  templateUrl: './app-file-preview.component.html',
  styleUrl: './app-file-preview.component.scss'
})
export class AppFilePreviewComponent implements OnChanges {
  
  @Input() file: File | null = null;
  fileContent: string | null = null;


  ngOnChanges(changes: SimpleChanges): void {
    if(changes['file'] && this.file){
      const reader = new FileReader();
      reader.onload = () => {
        const fileContent = reader.result as string;
        this.fileContent = this.getLines(fileContent, PREVIEW_AMOUNT_LINES);
      }
      if(this.file?.type.startsWith('text')){
        reader.readAsText(this.file)
      } else {
        this.fileContent = 'No se puede previsualizar este tipo de archivo.';
      }
    }
  }

  getLines = (text:string, maxLines:number):string => {
    const lines = text.split(/\r?\n/);
    if( lines.length > maxLines){
      return lines.slice(0, maxLines)
          .concat(["...\n", `solo se muestran las primeras ${maxLines} lineas.`])
          .join('\n');
    }
    return lines.slice(0, maxLines).join('\n');
  }

}
