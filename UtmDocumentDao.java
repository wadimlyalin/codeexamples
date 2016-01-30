package ru.v6services.auchan.alcoopt.dao;

import ru.v6services.auchan.alcoopt.dao.common.Dao;
import ru.v6services.auchan.alcoopt.model.criteria.PagingCriteria;
import ru.v6services.auchan.alcoopt.model.criteria.UtmDocumentCriteria;
import ru.v6services.auchan.alcoopt.model.utm.UtmDocument;

import java.util.List;

/**
 * @author Vadim Lyalin
 */
public interface UtmDocumentDao extends Dao<UtmDocument> {
    public List<UtmDocument> getNotSent(UtmDocumentCriteria criteria);
    public Long countNotSent();
}
