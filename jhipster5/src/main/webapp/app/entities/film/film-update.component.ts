import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IFilm, Film } from 'app/shared/model/film.model';
import { FilmService } from './film.service';

@Component({
  selector: 'jhi-film-update',
  templateUrl: './film-update.component.html'
})
export class FilmUpdateComponent implements OnInit {
  film: IFilm;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, []],
    url: [],
    length: [],
    playing: [],
    curtime: []
  });

  constructor(protected filmService: FilmService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ film }) => {
      this.updateForm(film);
      this.film = film;
    });
  }

  updateForm(film: IFilm) {
    this.editForm.patchValue({
      id: film.id,
      name: film.name,
      url: film.url,
      length: film.length,
      playing: film.playing,
      curtime: film.curtime
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const film = this.createFromForm();
    if (film.id !== undefined) {
      this.subscribeToSaveResponse(this.filmService.update(film));
    } else {
      this.subscribeToSaveResponse(this.filmService.create(film));
    }
  }

  private createFromForm(): IFilm {
    const entity = {
      ...new Film(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      url: this.editForm.get(['url']).value,
      length: this.editForm.get(['length']).value,
      playing: this.editForm.get(['playing']).value,
      curtime: this.editForm.get(['curtime']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFilm>>) {
    result.subscribe((res: HttpResponse<IFilm>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
