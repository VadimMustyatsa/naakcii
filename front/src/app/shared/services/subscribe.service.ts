import { Injectable } from '@angular/core';
import {RestDataService} from "./rest-data.service";

@Injectable()
export class SubscribeService {

  constructor(private restDataService: RestDataService) { 
  }

  addEmail(email){
    return this.restDataService.addSubscriber(JSON.stringify({"email":email}));
  }

}
