import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

const PROTOCOL='http';
const PORT=8080;
const IP_BACK='178.124.206.42';

@Injectable()
export class ApiService {

  private baseUrl:string;
  constructor(private http:HttpClient) {
      this.baseUrl=window.location.hostname === 'localhost' ? `${PROTOCOL}//${IP_BACK}:${PORT}` : `${PROTOCOL}//${window.location.hostname}:${PORT}/api/category`;
   }

}
