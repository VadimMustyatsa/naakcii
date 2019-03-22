import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable()
export class RequestInterceptorService implements HttpInterceptor {
  constructor() {  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // console.log('Запрос:')
    // console.log(req)
    let request = req.clone({
      url: `${environment.baseUrl}/api/${req.url}`,
        setHeaders: {
        Authorization: 'Accept=application/vnd.naakcii.api-v2.0+json'
      }
    });
    return next.handle(request);
  }
}
