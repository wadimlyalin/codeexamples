package ru.v6services.auchan.alcoopt.dao;

import com.sencha.gxt.data.shared.SortInfoBean;
import org.springframework.stereotype.Repository;
import ru.v6services.auchan.alcoopt.dao.common.BaseHibernateDao;
import ru.v6services.auchan.alcoopt.dao.common.BasePagingDao;
import ru.v6services.auchan.alcoopt.model.Waybill;
import ru.v6services.auchan.alcoopt.model.constants.UtmDocumentState;
import ru.v6services.auchan.alcoopt.model.criteria.PagingCriteria;
import ru.v6services.auchan.alcoopt.model.criteria.UtmDocumentCriteria;
import ru.v6services.auchan.alcoopt.model.utm.UtmDocument;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadim Lyalin
 */
@Repository
public class UtmDocumentDaoImpl extends BasePagingDao<UtmDocument> implements UtmDocumentDao {

    public List<UtmDocument> getNotSent(UtmDocumentCriteria criteria){
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<UtmDocument> query = cb.createQuery(UtmDocument.class);
        Root<UtmDocument> root = query.from(UtmDocument.class);
        root.fetch("fileContent");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("state"), UtmDocumentState.READY));
        if(criteria.getDepartmentId() != null) {
            predicates.add(cb.equal(root.get("shipperDepartment").get("id"), criteria.getDepartmentId()));
        }
        if(criteria.getMinDocumentId() != null) {
            predicates.add(cb.gt(root.get("id"), criteria.getMinDocumentId()));
        }
        query.select(root)
                .where(predicates.toArray(new Predicate[predicates.size()]));
        addOrderBy(root, criteria.getSortInfo(), query, cb);
        return getListByQuery(query,criteria.getOffset(),criteria.getLimit());
    }

    public Long countNotSent(){
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<UtmDocument> root = query.from(UtmDocument.class);
        query.select(cb.count(root.get("id")));
        query.where(cb.equal(root.get("state"), UtmDocumentState.READY));
        return getLong(query);
    }

}
