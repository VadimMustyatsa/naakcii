import {Pipe} from '@angular/core';

@Pipe ({
  name: 'cutString'
})
export class AppCutStringPipe {
  defaultLength = 20;
  transform(str: any, length?: any): String {
    const lengthNumber = length === undefined ? this.defaultLength : Number.parseInt(length);
    if (str.length > lengthNumber) {
      return (str.substring(0, lengthNumber) + '...');
    }
    return str;
  }
}
