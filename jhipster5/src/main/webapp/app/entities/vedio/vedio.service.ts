import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVedio } from 'app/shared/model/vedio.model';

type EntityResponseType = HttpResponse<IVedio>;
type EntityArrayResponseType = HttpResponse<IVedio[]>;

@Injectable({ providedIn: 'root' })
export class VedioService {
  public resourceUrl = SERVER_API_URL + 'api/vedios';

  constructor(protected http: HttpClient) {}

  create(vedio: IVedio): Observable<EntityResponseType> {
    return this.http.post<IVedio>(this.resourceUrl, vedio, { observe: 'response' });
  }

  update(vedio: IVedio): Observable<EntityResponseType> {
    return this.http.put<IVedio>(this.resourceUrl, vedio, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVedio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVedio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
