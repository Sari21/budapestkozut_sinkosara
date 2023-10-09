import { Component, ViewEncapsulation } from '@angular/core';
import { NewsService } from 'src/app/service/news.service';
import { News } from 'src/app/model/News';
import { ToastrService } from 'ngx-toastr';
import { ModalDismissReasons, NgbDatepickerModule, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { NewsDto } from 'src/app/model/NewsDto'
import { Form } from '@angular/forms';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';


@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent {
  constructor(private modalService: NgbModal, private newsService: NewsService, private toastr: ToastrService) {

  }
  title = 'bpkozut-frontend';

  allNews: News[] = [];
  newsToAdd: NewsDto = new NewsDto();
  newsToUpdate: News = new News();
  newsToRead: News = new News();
  closeResult: string | undefined;
  newFile: File | undefined;

  numberOfNews: number = 0;

  ngOnInit() {
    this.getNumberOfNews();
    this.getPage(0);
  }

  length = 50;
  pageSize = 9;
  pageIndex = 0;
  pageSizeOptions = [5, 10, 25];
  pageEvent: PageEvent | undefined;
  hidePageSize = false;
  showPageSizeOptions = false;
  showFirstLastButtons = false;
  disabled = false;

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;
    this.getPage(this.pageIndex);
  }

  public getNumberOfNews() {
    this.newsService.getNumberOfNews().subscribe(
      (res) => {
        // tslint:disable-next-line:prefer-const
        this.length = res.numberOfNews;
      },
      (err) => {
        this.showError(err.error.message, 'Error');
      }
    );
  }
  public getPage(idx: number) {
    this.newsService.getPage(idx).subscribe(
      (res) => {
        let data: any = res;
        this.allNews = <News[]>data;

        if (this.allNews.length == 0 && this.pageIndex != 0) {
          this.pageIndex--;
          this.getPage(this.pageIndex);
        }

      },
      (err) => {
        this.showError(err.error.message, 'Error');
      }
    );
  }

  public addNewsFunction(modal: any) {
    this.modalService.open(modal, { ariaLabelledBy: 'modal-basic-title' }).result.then(
      (result) => {
        if (this.newFile) {
          const formData: FormData = new FormData();
          formData.append("file", this.newFile);
          this.newsService.postImage(formData).subscribe(
            (data: any) => {
              this.newsToAdd.image = data.image;
              this.newFile = undefined;
              this.newsService.postNews(this.newsToAdd).subscribe(
                (data: any) => {
                  this.refreshNews();
                  this.showSuccess('Saved successfully');
                  this.newsToAdd = new NewsDto();
                },
                (err: any) => {
                  this.showError(err.error.message, 'Error');
                });
            },
            (err: any) => {
              this.showError(err.error.message, 'Error');
            });
        }
        else {

          this.newsService.postNews(this.newsToAdd).subscribe(
            (data: any) => {
              this.refreshNews();
              this.showSuccess('Saved successfully');
              this.newsToAdd = new NewsDto();
            },
            (err: any) => {
              this.showError(err.error.message, 'Error');
            });
        }
        this.closeResult = `Closed with: ${result}`;
      },
      (reason) => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      },
    );
  }
  public onFileSelected(event: any) {
    this.newFile = event.target.files[0];
  }

  updateNewsFunction(content: any, item: News) {
    this.newsToUpdate.author = item.author;
    this.newsToUpdate.id = item.id;
    this.newsToUpdate.title = item.title;
    this.newsToUpdate.content = item.content;
    this.newsToUpdate.image = item.image;
    this.newsToUpdate.created = item.created;
    this.newsToUpdate.updated = item.updated;
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
      (result) => {
        if (this.newFile) {
          const formData: FormData = new FormData();
          formData.append("file", this.newFile);
          this.newsService.putImage(formData, item.id).subscribe(
            (data: any) => {
              this.newsToUpdate.image = data.image;
              this.newsService.putNews(this.newsToUpdate).subscribe(
                (data: any) => {
                  let idx = this.allNews.findIndex((n) => n.id == item.id);
                  this.allNews[idx] = data;
                  this.showSuccess('Successfully updated');
                  this.newsToUpdate = new News();
                  // this.pushNews(data);
                  // this.form = empForm;
                  // this.files = null;
                },
                (err: any) => {
                  this.showError(err.error.message, 'Error');
                });
            },
            (err: any) => {
              this.showError(err.error.message, 'Error');
            });
        }
        else {

          this.newsService.putNews(this.newsToUpdate).subscribe(
            (data: any) => {
              let idx = this.allNews.findIndex((n) => n.id == item.id);
              this.allNews[idx] = data;
              this.showSuccess('Successfully updated');
              this.newsToUpdate = new News();

            },
            (err: any) => {
              this.showError(err.error.message, 'Error');
            });
        }
        this.closeResult = `Closed with: ${result}`;
      },
      (reason) => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      },
    );
  }
  public readNewsFunction(news: any, modal: any) {
    this.newsToRead = news;
    this.modalService.open(modal, { ariaLabelledBy: 'modal-basic-title' }).result.then(
      (result) => {
      },
      (err: any) => {
        this.showError(err.error.message, 'Error');
      });

  }

  public deleteNews(newsId: any) {
    this.newsService.deleteNews(newsId).subscribe(
      (data: any) => {
        /* let idx = this.allNews.findIndex((n) => n.id == newsId);
         this.allNews.splice(idx, 1);
         */
        this.refreshNews();
        this.showSuccess('News deleted');
      },
      (err: any) => {
        this.showError(err.error.message, 'Error');
      });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  private refreshNews() {
    this.getNumberOfNews();
    this.getPage(this.pageIndex);
  }
  showSuccess(message: string) {
    this.toastr.success(message);
  }

  showError(message: string, title: string) {
    this.toastr.error(message, title);
  }

}
