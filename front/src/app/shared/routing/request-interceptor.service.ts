import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { environment } from "../../../environments/environment";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class RequestInterceptorService implements HttpInterceptor {
  constructor() {  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const request = req.clone({
      url: `${environment.baseUrl}${req.url}`,
      headers: null
    });
    return next.handle(request);
  }
}
