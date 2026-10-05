import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPasoDeRunbook } from '../paso-de-runbook.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../paso-de-runbook.test-samples';

import { PasoDeRunbookService } from './paso-de-runbook.service';

const requireRestSample: IPasoDeRunbook = {
  ...sampleWithRequiredData,
};

describe('PasoDeRunbook Service', () => {
  let service: PasoDeRunbookService;
  let httpMock: HttpTestingController;
  let expectedResult: IPasoDeRunbook | IPasoDeRunbook[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PasoDeRunbookService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PasoDeRunbook', () => {
      const pasoDeRunbook = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pasoDeRunbook).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PasoDeRunbook', () => {
      const pasoDeRunbook = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pasoDeRunbook).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PasoDeRunbook', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PasoDeRunbook', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PasoDeRunbook', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPasoDeRunbookToCollectionIfMissing', () => {
      it('should add a PasoDeRunbook to an empty array', () => {
        const pasoDeRunbook: IPasoDeRunbook = sampleWithRequiredData;
        expectedResult = service.addPasoDeRunbookToCollectionIfMissing([], pasoDeRunbook);
        expect(expectedResult).toEqual([pasoDeRunbook]);
      });

      it('should not add a PasoDeRunbook to an array that contains it', () => {
        const pasoDeRunbook: IPasoDeRunbook = sampleWithRequiredData;
        const pasoDeRunbookCollection: IPasoDeRunbook[] = [
          {
            ...pasoDeRunbook,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPasoDeRunbookToCollectionIfMissing(pasoDeRunbookCollection, pasoDeRunbook);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PasoDeRunbook to an array that doesn't contain it", () => {
        const pasoDeRunbook: IPasoDeRunbook = sampleWithRequiredData;
        const pasoDeRunbookCollection: IPasoDeRunbook[] = [sampleWithPartialData];
        expectedResult = service.addPasoDeRunbookToCollectionIfMissing(pasoDeRunbookCollection, pasoDeRunbook);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pasoDeRunbook);
      });

      it('should add only unique PasoDeRunbook to an array', () => {
        const pasoDeRunbookArray: IPasoDeRunbook[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pasoDeRunbookCollection: IPasoDeRunbook[] = [sampleWithRequiredData];
        expectedResult = service.addPasoDeRunbookToCollectionIfMissing(pasoDeRunbookCollection, ...pasoDeRunbookArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pasoDeRunbook: IPasoDeRunbook = sampleWithRequiredData;
        const pasoDeRunbook2: IPasoDeRunbook = sampleWithPartialData;
        expectedResult = service.addPasoDeRunbookToCollectionIfMissing([], pasoDeRunbook, pasoDeRunbook2);
        expect(expectedResult).toEqual([pasoDeRunbook, pasoDeRunbook2]);
      });

      it('should accept null and undefined values', () => {
        const pasoDeRunbook: IPasoDeRunbook = sampleWithRequiredData;
        expectedResult = service.addPasoDeRunbookToCollectionIfMissing([], null, pasoDeRunbook, undefined);
        expect(expectedResult).toEqual([pasoDeRunbook]);
      });

      it('should return initial array if no PasoDeRunbook is added', () => {
        const pasoDeRunbookCollection: IPasoDeRunbook[] = [sampleWithRequiredData];
        expectedResult = service.addPasoDeRunbookToCollectionIfMissing(pasoDeRunbookCollection, undefined, null);
        expect(expectedResult).toEqual(pasoDeRunbookCollection);
      });
    });

    describe('comparePasoDeRunbook', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePasoDeRunbook(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15921 };
        const entity2 = null;

        const compareResult1 = service.comparePasoDeRunbook(entity1, entity2);
        const compareResult2 = service.comparePasoDeRunbook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15921 };
        const entity2 = { id: 2779 };

        const compareResult1 = service.comparePasoDeRunbook(entity1, entity2);
        const compareResult2 = service.comparePasoDeRunbook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15921 };
        const entity2 = { id: 15921 };

        const compareResult1 = service.comparePasoDeRunbook(entity1, entity2);
        const compareResult2 = service.comparePasoDeRunbook(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
