import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFilm } from 'app/shared/model/film.model';

type EntityResponseType = HttpResponse<IFilm>;
type EntityArrayResponseType = HttpResponse<IFilm[]>;

@Injectable({ providedIn: 'root' })
export class FilmService {
  public resourceUrl = SERVER_API_URL + 'api/films';

  constructor(protected http: HttpClient) {}

  create(film: IFilm): Observable<EntityResponseType> {
    return this.http.post<IFilm>(this.resourceUrl, film, { observe: 'response' });
  }

  update(film: IFilm): Observable<EntityResponseType> {
    return this.http.put<IFilm>(this.resourceUrl, film, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFilm>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFilm[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
