import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Vedio } from 'app/shared/model/vedio.model';
import { VedioService } from './vedio.service';
import { VedioComponent } from './vedio.component';
import { VedioDetailComponent } from './vedio-detail.component';
import { VedioUpdateComponent } from './vedio-update.component';
import { VedioDeletePopupComponent } from './vedio-delete-dialog.component';
import { IVedio } from 'app/shared/model/vedio.model';

@Injectable({ providedIn: 'root' })
export class VedioResolve implements Resolve<IVedio> {
  constructor(private service: VedioService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IVedio> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Vedio>) => response.ok),
        map((vedio: HttpResponse<Vedio>) => vedio.body)
      );
    }
    return of(new Vedio());
  }
}

export const vedioRoute: Routes = [
  {
    path: '',
    component: VedioComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.vedio.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VedioDetailComponent,
    resolve: {
      vedio: VedioResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.vedio.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VedioUpdateComponent,
    resolve: {
      vedio: VedioResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.vedio.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VedioUpdateComponent,
    resolve: {
      vedio: VedioResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.vedio.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const vedioPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: VedioDeletePopupComponent,
    resolve: {
      vedio: VedioResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.vedio.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
