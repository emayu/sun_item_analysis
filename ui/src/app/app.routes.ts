import { Routes } from '@angular/router';
import { UploadAnalyzeFileComponent } from './upload-analyze-file/upload-analyze-file.component';
import { HomeComponent } from './home/home.component';
import { UploadTransformFileComponent } from './upload-transform-file/upload-transform-file.component';

export const routes: Routes = [
    { path: 'analyze', component: UploadAnalyzeFileComponent},
    { path: 'transform', component: UploadTransformFileComponent},
    { path: '', component: HomeComponent}
];
