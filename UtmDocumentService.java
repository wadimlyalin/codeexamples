package ru.v6services.auchan.alcoopt.service;

import ru.fsrar.wegais.wb_doc_single_01.Documents;
import ru.v6services.auchan.alcoopt.model.Department;
import ru.v6services.auchan.alcoopt.model.constants.UtmDocumentState;
import ru.v6services.auchan.alcoopt.model.constants.UtmDocumentType;
import ru.v6services.auchan.alcoopt.model.criteria.PagingCriteria;
import ru.v6services.auchan.alcoopt.model.criteria.UtmDocumentCriteria;
import ru.v6services.auchan.alcoopt.model.utm.UtmDocument;
import ru.v6services.auchan.alcoopt.service.common.Service;

import java.util.List;

/**
 * @author Vadim Lyalin
 */
public interface UtmDocumentService extends Service<UtmDocument> {
    UtmDocument createUtmDocument(Documents documents, Department department, UtmDocumentType utmDocumentType);
    List<UtmDocument> getNotSent(UtmDocumentCriteria criteria);
    Long countNotSent();
    UtmDocument createUtmDocument(Documents documents, Department department, UtmDocumentType utmDocumentType, UtmDocumentState state);
    UtmDocument loadById(Long id);
    void changeState(long id, UtmDocumentState state);
}
