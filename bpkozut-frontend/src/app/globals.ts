import { Injectable } from '@angular/core';

@Injectable()
export class Globals {

public BASE_URL = 'http://localhost:8080';
public IMG_ROUTE = '/assets/';

formatDate(date: any) {
    const newDate = new Date(date);
    const mm = newDate.getMonth() + 1; // getMonth() is zero-based
    const dd = newDate.getDate();

    const resultDate = [newDate.getFullYear(),
            (mm > 9 ? '' : '0') + mm,
            (dd > 9 ? '' : '0') + dd
    ].join('-');

    return resultDate;
}

}