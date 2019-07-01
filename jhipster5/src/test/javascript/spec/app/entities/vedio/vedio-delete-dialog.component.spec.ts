/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Bisaiapp5TestModule } from '../../../test.module';
import { VedioDeleteDialogComponent } from 'app/entities/vedio/vedio-delete-dialog.component';
import { VedioService } from 'app/entities/vedio/vedio.service';

describe('Component Tests', () => {
  describe('Vedio Management Delete Component', () => {
    let comp: VedioDeleteDialogComponent;
    let fixture: ComponentFixture<VedioDeleteDialogComponent>;
    let service: VedioService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Bisaiapp5TestModule],
        declarations: [VedioDeleteDialogComponent]
      })
        .overrideTemplate(VedioDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VedioDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VedioService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
