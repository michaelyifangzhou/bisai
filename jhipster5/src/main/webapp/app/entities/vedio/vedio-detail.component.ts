import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVedio } from 'app/shared/model/vedio.model';

@Component({
  selector: 'jhi-vedio-detail',
  templateUrl: './vedio-detail.component.html'
})
export class VedioDetailComponent implements OnInit {
  vedio: IVedio;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ vedio }) => {
      this.vedio = vedio;
    });
  }

  previousState() {
    window.history.back();
  }
}
