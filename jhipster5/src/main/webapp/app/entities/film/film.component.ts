import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IFilm } from 'app/shared/model/film.model';
import { AccountService } from 'app/core';
import { FilmService } from './film.service';

@Component({
  selector: 'jhi-film',
  templateUrl: './film.component.html'
})
export class FilmComponent implements OnInit, OnDestroy {
  films: IFilm[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected filmService: FilmService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.filmService
      .query()
      .pipe(
        filter((res: HttpResponse<IFilm[]>) => res.ok),
        map((res: HttpResponse<IFilm[]>) => res.body)
      )
      .subscribe(
        (res: IFilm[]) => {
          this.films = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInFilms();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IFilm) {
    return item.id;
  }

  registerChangeInFilms() {
    this.eventSubscriber = this.eventManager.subscribe('filmListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
