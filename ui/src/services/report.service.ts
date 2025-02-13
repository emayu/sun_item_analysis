import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http:HttpClient) { }

  downloadScoreDistributionReportPDF(body:any){
    return this.http.post('/api/report/scoreDistribution/pdf', body, {responseType:'blob', observe:'response'});
  }

  downloadScoreDistributionReportXls(body:any){
    return this.http.post('/api/report/scoreDistribution/xlsx', body, {responseType:'blob', observe:'response'});
  }

  downloadResultReporPDF(subtitle:string|null, body:any){
    let params = new HttpParams();
    params = subtitle === null? params: params.append('subtitle', subtitle);
    return this.http.post('/api/report/resultReport/pdf', body, {
      params,
      responseType: 'blob',
      observe: 'response'
    })
  }

  downloadResultReporXLS(subtitle:string|null, body:any){
    let params = new HttpParams();
    params = subtitle === null? params: params.append('subtitle', subtitle);

    return this.http.post('/api/report/resultReport/xlsx', body, {
      params,
      responseType: 'blob',
      observe: 'response'
    })
  }

}
