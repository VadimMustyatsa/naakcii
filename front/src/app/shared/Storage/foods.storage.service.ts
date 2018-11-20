import {Injectable} from '@angular/core';

import {ApiService} from '../services/api.service';
import {Storag} from './foods.storage.model';

@Injectable()
export class FoodsStorageService {

  constructor(private apiService: ApiService) {
  }

  getAll() {
    return this.apiService.getAllFoods();
  }
}



