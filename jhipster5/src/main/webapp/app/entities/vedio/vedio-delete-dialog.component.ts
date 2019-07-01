import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVedio } from 'app/shared/model/vedio.model';
import { VedioService } from './vedio.service';

@Component({
  selector: 'jhi-vedio-delete-dialog',
  templateUrl: './vedio-delete-dialog.component.html'
})
export class VedioDeleteDialogComponent {
  vedio: IVedio;

  constructor(protected vedioService: VedioService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.vedioService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'vedioListModification',
        content: 'Deleted an vedio'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-vedio-delete-popup',
  template: ''
})
export class VedioDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ vedio }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(VedioDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.vedio = vedio;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/vedio', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/vedio', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
