import { Injectable } from '@angular/core';
import {Globals} from '../globals';
import {HttpClient} from "@angular/common/http";
import {Observable} from 'rxjs';
import { News } from '../model/News';

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  
  constructor(private http: HttpClient, private globals: Globals) {
  }
  
  private BASE_URL = this.globals.BASE_URL;
  //private BASE_URL = this.globals.BASE_URL + "/api";

  getPage(idx:number): Observable<any> {
    return this.http.get(
      this.BASE_URL + '/api/news/page/'+idx
      );
    }
  getNumberOfNews(): Observable<any> {
    return this.http.get(
      this.BASE_URL + '/api/news'
      );
    }
  /*  getNews(pageNum: number): Observable<any> {
      return this.http.get(
        this.BASE_URL + '/news/page/'.concat(pageNum.toString())
        );
      }
      */  
  deleteNews(id: number): Observable<any> {
      return this.http
          .delete(this.globals.BASE_URL + '/api/news/' + id);
  }

  putNews(o: News): Observable<any> {
      return this.http
          .put(this.globals.BASE_URL + '/api/news', o);
  }

  postNews(o: Object): Observable<any> {
      return this.http
          .post(this.globals.BASE_URL + '/api/news', o);
  }
  postImage(o: Object): Observable<any> {
    return this.http
        .post(this.globals.BASE_URL + '/api/news/image', o);
}
  putImage(o: Object, id:any): Observable<any> {
    return this.http
      .put(this.globals.BASE_URL + '/api/news/image/'+id, o);
}
  
}
