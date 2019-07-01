import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IVedio, Vedio } from 'app/shared/model/vedio.model';
import { VedioService } from './vedio.service';

@Component({
  selector: 'jhi-vedio-update',
  templateUrl: './vedio-update.component.html'
})
export class VedioUpdateComponent implements OnInit {
  vedio: IVedio;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [],
    url: [],
    isplaying: [],
    length: []
  });

  constructor(protected vedioService: VedioService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ vedio }) => {
      this.updateForm(vedio);
      this.vedio = vedio;
    });
  }

  updateForm(vedio: IVedio) {
    this.editForm.patchValue({
      id: vedio.id,
      name: vedio.name,
      url: vedio.url,
      isplaying: vedio.isplaying,
      length: vedio.length
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const vedio = this.createFromForm();
    if (vedio.id !== undefined) {
      this.subscribeToSaveResponse(this.vedioService.update(vedio));
    } else {
      this.subscribeToSaveResponse(this.vedioService.create(vedio));
    }
  }

  private createFromForm(): IVedio {
    const entity = {
      ...new Vedio(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      url: this.editForm.get(['url']).value,
      isplaying: this.editForm.get(['isplaying']).value,
      length: this.editForm.get(['length']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVedio>>) {
    result.subscribe((res: HttpResponse<IVedio>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
