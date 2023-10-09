import { Component, OnInit } from '@angular/core';
import { AppService } from '../../app.service';
import { HttpClient } from '@angular/common/http';
import { NewsService } from 'src/app/service/news.service';
import { ToastrService } from 'ngx-toastr';
import { News } from 'src/app/model/News';

@Component({
  templateUrl: './home.component.html'
})
export class HomeComponent {

  constructor(){

  }
  ngOnInit(){
  }
}