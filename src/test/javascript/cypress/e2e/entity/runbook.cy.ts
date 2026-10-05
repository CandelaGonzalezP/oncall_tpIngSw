import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Runbook e2e test', () => {
  const runbookPageUrl = '/runbook';
  let username: string;
  let password: string;
  const runbookSample = { nombre: 'deliberately but', activo: false };

  let runbook;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/runbooks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/runbooks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/runbooks/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (runbook) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/runbooks/${runbook.id}`,
      }).then(() => {
        runbook = undefined;
      });
    }
  });

  it('Runbooks menu should load Runbooks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('runbook');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Runbook').should('exist');
    cy.location('pathname').should('eq', runbookPageUrl);
  });

  describe('Runbook page', () => {
    it('should have translated page title', () => {
      cy.visit(runbookPageUrl);
      cy.getEntityHeading('Runbook').should('not.contain', 'oncallApp.runbook.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(runbookPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Runbook page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${runbookPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Runbook');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', runbookPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/runbooks',
          body: runbookSample,
        }).then(({ body }) => {
          runbook = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/runbooks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [runbook],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(runbookPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Runbook page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('runbook');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', runbookPageUrl);
      });

      it('edit button click should load edit Runbook page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Runbook');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', runbookPageUrl);
      });

      it('edit button click should load edit Runbook page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Runbook');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', runbookPageUrl);
      });

      it('last delete button click should delete instance of Runbook', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('runbook').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', runbookPageUrl);

        runbook = undefined;
      });
    });
  });

  describe('new Runbook page', () => {
    beforeEach(() => {
      cy.visit(runbookPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Runbook');
    });

    it('should create an instance of Runbook', () => {
      cy.get(`[data-cy="nombre"]`).type('rise after bonfire');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'rise after bonfire');

      cy.get(`[data-cy="descripcion"]`).type('sizzling');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'sizzling');

      cy.get(`[data-cy="origenAlerta"]`).select('CLOUDWATCH');

      cy.get(`[data-cy="severidadMinima"]`).select('SEV1');

      cy.get(`[data-cy="patronFingerprint"]`).type('meanwhile yuck aw');
      cy.get(`[data-cy="patronFingerprint"]`).should('have.value', 'meanwhile yuck aw');

      cy.get(`[data-cy="tiempoMaxMinutos"]`).type('1181');
      cy.get(`[data-cy="tiempoMaxMinutos"]`).should('have.value', '1181');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click();
      cy.get(`[data-cy="activo"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        runbook = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', runbookPageUrl);
    });
  });
});
