package ru.v6services.auchan.alcoopt.dao;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfoBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.v6services.auchan.alcoopt.model.criteria.UtmDocumentCriteria;
import ru.v6services.auchan.alcoopt.model.utm.UtmDocument;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Вадим Лялин
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:/application-context-test.xml")
public class UtmDocumentDaoImplTest {
    UtmDocumentDaoImpl utmDocumentDao;
    @PersistenceContext
    private EntityManager entityManager;

    @Before
    public void before() {
        utmDocumentDao = new UtmDocumentDaoImpl();
        utmDocumentDao.setEntityManager(entityManager);
    }

    @Test
    public void testGetNotSent() {
        UtmDocumentCriteria utmDocumentCriteria = new UtmDocumentCriteria();
        utmDocumentCriteria.setOffset(0);
        utmDocumentCriteria.setLimit(100);
        utmDocumentCriteria.setDepartmentId(123L);
        utmDocumentCriteria.setMinDocumentId(0L);
        utmDocumentCriteria.setSortInfo(new ArrayList<>());
        utmDocumentCriteria.getSortInfo().add(new SortInfoBean("id", SortDir.ASC));
        List<UtmDocument> utmDocumentList = utmDocumentDao.getNotSent(utmDocumentCriteria);

        assertTrue(utmDocumentList.isEmpty());
    }
}
