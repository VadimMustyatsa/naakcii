import { Injectable } from '@angular/core';
import {RestDataService} from "./rest-data.service";

@Injectable()
export class SubscribeService {

  constructor(private restDataService: RestDataService) { 
  }

  addEmail(email){
    console.log(JSON.stringify({"email":email}))
    return this.restDataService.addSubscriber(JSON.stringify({"email":email}));
  }

}
