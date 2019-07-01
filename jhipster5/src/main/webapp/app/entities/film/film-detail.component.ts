import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFilm } from 'app/shared/model/film.model';

@Component({
  selector: 'jhi-film-detail',
  templateUrl: './film-detail.component.html'
})
export class FilmDetailComponent implements OnInit {
  film: IFilm;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ film }) => {
      this.film = film;
    });
  }

  previousState() {
    window.history.back();
  }
}
