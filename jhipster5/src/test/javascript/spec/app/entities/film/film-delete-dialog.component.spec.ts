/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Bisaiapp5TestModule } from '../../../test.module';
import { FilmDeleteDialogComponent } from 'app/entities/film/film-delete-dialog.component';
import { FilmService } from 'app/entities/film/film.service';

describe('Component Tests', () => {
  describe('Film Management Delete Component', () => {
    let comp: FilmDeleteDialogComponent;
    let fixture: ComponentFixture<FilmDeleteDialogComponent>;
    let service: FilmService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Bisaiapp5TestModule],
        declarations: [FilmDeleteDialogComponent]
      })
        .overrideTemplate(FilmDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FilmDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FilmService);
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
