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

describe('PasoDeRunbook e2e test', () => {
  const pasoDeRunbookPageUrl = '/paso-de-runbook';
  let username: string;
  let password: string;
  const pasoDeRunbookSample = { orden: 48, titulo: 'begonia hence', instrucciones: 'sundae', obligatorio: true };

  let pasoDeRunbook;
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
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/runbooks',
      body: {
        nombre: 'furiously gadzooks faraway',
        descripcion: 'jaggedly pull',
        origenAlerta: 'HEALTHCHECK',
        severidadMinima: 'SEV2',
        patronFingerprint: 'lest',
        tiempoMaxMinutos: 256,
        activo: false,
      },
    }).then(({ body }) => {
      runbook = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/paso-de-runbooks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/paso-de-runbooks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/paso-de-runbooks/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/runbooks', {
      statusCode: 200,
      body: [runbook],
    });
  });

  afterEach(() => {
    if (pasoDeRunbook) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/paso-de-runbooks/${pasoDeRunbook.id}`,
      }).then(() => {
        pasoDeRunbook = undefined;
      });
    }
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

  it('PasoDeRunbooks menu should load PasoDeRunbooks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('paso-de-runbook');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PasoDeRunbook').should('exist');
    cy.location('pathname').should('eq', pasoDeRunbookPageUrl);
  });

  describe('PasoDeRunbook page', () => {
    it('should have translated page title', () => {
      cy.visit(pasoDeRunbookPageUrl);
      cy.getEntityHeading('PasoDeRunbook').should('not.contain', 'oncallApp.pasoDeRunbook.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pasoDeRunbookPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PasoDeRunbook page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${pasoDeRunbookPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PasoDeRunbook');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pasoDeRunbookPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/paso-de-runbooks',
          body: {
            ...pasoDeRunbookSample,
            runbook,
          },
        }).then(({ body }) => {
          pasoDeRunbook = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/paso-de-runbooks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [pasoDeRunbook],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pasoDeRunbookPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PasoDeRunbook page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pasoDeRunbook');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pasoDeRunbookPageUrl);
      });

      it('edit button click should load edit PasoDeRunbook page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PasoDeRunbook');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pasoDeRunbookPageUrl);
      });

      it('edit button click should load edit PasoDeRunbook page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PasoDeRunbook');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pasoDeRunbookPageUrl);
      });

      it('last delete button click should delete instance of PasoDeRunbook', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pasoDeRunbook').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pasoDeRunbookPageUrl);

        pasoDeRunbook = undefined;
      });
    });
  });

  describe('new PasoDeRunbook page', () => {
    beforeEach(() => {
      cy.visit(pasoDeRunbookPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PasoDeRunbook');
    });

    it('should create an instance of PasoDeRunbook', () => {
      cy.get(`[data-cy="orden"]`).type('8');
      cy.get(`[data-cy="orden"]`).should('have.value', '8');

      cy.get(`[data-cy="titulo"]`).type('ha attest supposing');
      cy.get(`[data-cy="titulo"]`).should('have.value', 'ha attest supposing');

      cy.get(`[data-cy="instrucciones"]`).type('what');
      cy.get(`[data-cy="instrucciones"]`).should('have.value', 'what');

      cy.get(`[data-cy="obligatorio"]`).should('not.be.checked');
      cy.get(`[data-cy="obligatorio"]`).click();
      cy.get(`[data-cy="obligatorio"]`).should('be.checked');

      cy.get(`[data-cy="runbook"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pasoDeRunbook = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', pasoDeRunbookPageUrl);
    });
  });
});
