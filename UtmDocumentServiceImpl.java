package ru.v6services.auchan.alcoopt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fsrar.wegais.wb_doc_single_01.Documents;
import ru.v6services.auchan.alcoopt.dao.UtmDocumentDao;
import ru.v6services.auchan.alcoopt.exception.ApplicationException;
import ru.v6services.auchan.alcoopt.model.Department;
import ru.v6services.auchan.alcoopt.model.common.FileContent;
import ru.v6services.auchan.alcoopt.model.constants.UtmDocumentState;
import ru.v6services.auchan.alcoopt.model.constants.UtmDocumentType;
import ru.v6services.auchan.alcoopt.model.criteria.PagingCriteria;
import ru.v6services.auchan.alcoopt.model.criteria.UtmDocumentCriteria;
import ru.v6services.auchan.alcoopt.model.utm.UtmDocument;
import ru.v6services.auchan.alcoopt.service.common.BaseService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * @author Vadim Lyalin
 */
@Service
public class UtmDocumentServiceImpl extends BaseService<UtmDocument, UtmDocumentDao> implements UtmDocumentService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UtmDocumentServiceImpl.class);

    private Marshaller marshaller;
    {
        try {
            marshaller = JAXBContext.newInstance(Documents.class).createMarshaller();
        } catch (JAXBException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage());
        }
    }

    @Transactional
    public void changeState(long id, UtmDocumentState state) {
        UtmDocument utmDocument = findById(id);
        utmDocument.setState(state);
        dao.merge(utmDocument);
    }

    @Transactional
    public UtmDocument createUtmDocument(Documents documents, Department department, UtmDocumentType utmDocumentType, UtmDocumentState state) {
        FileContent fileContent = new FileContent();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            marshaller.marshal(documents, os);
            fileContent.setContent(os.toString("UTF-8"));
        } catch (UnsupportedEncodingException | JAXBException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage());
        }

        UtmDocument utmDocument = new UtmDocument();
        utmDocument.setFileContent(fileContent);
        utmDocument.setUtmUrl(department.getUtmUrl() + utmDocumentType.getLink());
        utmDocument.setState(state);
        utmDocument.setCreateDate(new Date());
        utmDocument.setShipperDepartment(department);

        persist(utmDocument);
        return utmDocument;
    }

    @Override
    public UtmDocument loadById(Long id) {
        return super.findById(id);
    }

    @Transactional
    public UtmDocument createUtmDocument(Documents documents, Department department, UtmDocumentType utmDocumentType) {
        return createUtmDocument(documents, department, utmDocumentType, UtmDocumentState.READY);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtmDocument> getNotSent(UtmDocumentCriteria criteria) {
        return dao.getNotSent(criteria);
    }

    @Override
    public Long countNotSent() {
        return countNotSent();
    }  
}
