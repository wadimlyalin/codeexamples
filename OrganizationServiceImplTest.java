package ru.v6services.auchan.alcoopt.service;

import org.junit.Before;
import org.junit.Test;
import ru.v6services.auchan.alcoopt.dao.OrganizationDao;
import ru.v6services.auchan.alcoopt.model.Organization;
import ru.v6services.auchan.alcoopt.model.constants.OrganizationFunctionality;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * @author Вадим Лялин
 */
public class OrganizationServiceImplTest {
    OrganizationServiceImpl organizationService;
    OrganizationDao organizationDao;

    @Before
    public void before() {
        organizationService = new OrganizationServiceImpl();
        organizationDao = mock(OrganizationDao.class);
        organizationService.setDao(organizationDao);
    }

    @Test
    public void testGetByFunctionality() {
        List<Organization> organizations = new ArrayList<>();
        when(organizationDao.getByFunctionality(OrganizationFunctionality.AUCHAN)).thenReturn(organizations);

        assertSame(organizationService.getByFunctionality(OrganizationFunctionality.AUCHAN), organizations);
    }
}
