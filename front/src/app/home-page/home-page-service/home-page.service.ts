import { Injectable } from '@angular/core';
import { RestDataService } from '../../shared/services/rest-data.service';

@Injectable()
export class HomePageService {

  statistics: any;

  constructor(restDataService: RestDataService) {
    this.statistics = restDataService.statistics;
  }

}
