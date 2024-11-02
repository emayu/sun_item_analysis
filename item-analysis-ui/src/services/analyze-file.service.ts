import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResponseData } from '../models/response';

@Injectable({
  providedIn: 'root'
})
export class AnalyzeFileService {

  constructor(private http:HttpClient) { }

  analyzeFile(formData:FormData){
    return this.http.post<ResponseData>(`/api/files/analyze`, formData, { params: { includeTextReport: true }} )
  }
}
