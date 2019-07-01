/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { Bisaiapp5TestModule } from '../../../test.module';
import { FilmUpdateComponent } from 'app/entities/film/film-update.component';
import { FilmService } from 'app/entities/film/film.service';
import { Film } from 'app/shared/model/film.model';

describe('Component Tests', () => {
  describe('Film Management Update Component', () => {
    let comp: FilmUpdateComponent;
    let fixture: ComponentFixture<FilmUpdateComponent>;
    let service: FilmService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Bisaiapp5TestModule],
        declarations: [FilmUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FilmUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FilmUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FilmService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Film(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Film();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
