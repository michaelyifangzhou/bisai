import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Film } from 'app/shared/model/film.model';
import { FilmService } from './film.service';
import { FilmComponent } from './film.component';
import { FilmDetailComponent } from './film-detail.component';
import { FilmUpdateComponent } from './film-update.component';
import { FilmDeletePopupComponent } from './film-delete-dialog.component';
import { IFilm } from 'app/shared/model/film.model';

@Injectable({ providedIn: 'root' })
export class FilmResolve implements Resolve<IFilm> {
  constructor(private service: FilmService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFilm> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Film>) => response.ok),
        map((film: HttpResponse<Film>) => film.body)
      );
    }
    return of(new Film());
  }
}

export const filmRoute: Routes = [
  {
    path: '',
    component: FilmComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.film.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FilmDetailComponent,
    resolve: {
      film: FilmResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.film.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FilmUpdateComponent,
    resolve: {
      film: FilmResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.film.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FilmUpdateComponent,
    resolve: {
      film: FilmResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.film.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const filmPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FilmDeletePopupComponent,
    resolve: {
      film: FilmResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bisaiapp5App.film.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
