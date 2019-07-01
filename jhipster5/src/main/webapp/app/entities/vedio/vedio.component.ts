import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IVedio } from 'app/shared/model/vedio.model';
import { AccountService } from 'app/core';
import { VedioService } from './vedio.service';

@Component({
  selector: 'jhi-vedio',
  templateUrl: './vedio.component.html'
})
export class VedioComponent implements OnInit, OnDestroy {
  vedios: IVedio[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected vedioService: VedioService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.vedioService
      .query()
      .pipe(
        filter((res: HttpResponse<IVedio[]>) => res.ok),
        map((res: HttpResponse<IVedio[]>) => res.body)
      )
      .subscribe(
        (res: IVedio[]) => {
          this.vedios = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInVedios();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IVedio) {
    return item.id;
  }

  registerChangeInVedios() {
    this.eventSubscriber = this.eventManager.subscribe('vedioListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
