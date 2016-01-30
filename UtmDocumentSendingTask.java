package ru.v6services.auchan.alcoopt.service.utm;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfoBean;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.v6services.auchan.alcoopt.exception.ConnectException;
import ru.v6services.auchan.alcoopt.model.constants.UtmDocumentState;
import ru.v6services.auchan.alcoopt.model.criteria.UtmDocumentCriteria;
import ru.v6services.auchan.alcoopt.model.utm.DocumentsList;
import ru.v6services.auchan.alcoopt.model.utm.UtmDocument;
import ru.v6services.auchan.alcoopt.service.DepartmentService;
import ru.v6services.auchan.alcoopt.service.UtmDocumentService;
import ru.v6services.auchan.alcoopt.service.utm.sender.UtmQuerySender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Sends document to UTM
 * @author Vadim Lyalin
 */
@Service
public class UtmDocumentSendingTask {
    private final static Logger LOGGER = LoggerFactory.getLogger(UtmDocumentSendingTask.class);
    final static int DOCS_NUMBER = 200;
    /** 50 минут */
    private long maxThreadExecutionTime = 1000L * 60 * 50;

    @Autowired
    private UtmDocumentService service;
    @Autowired
    private UtmQuerySender utmQuerySender;
    @Autowired
    private DepartmentService departmentService;

    public void sendDocuments(){
        sendDocuments(null);
    }

    public void sendDocuments(Long departmentId){
        LOGGER.info("Sending documents to UTM");
        UtmDocumentCriteria utmDocumentCriteria = new UtmDocumentCriteria();
        utmDocumentCriteria.setOffset(0);
        utmDocumentCriteria.setLimit(DOCS_NUMBER);
        utmDocumentCriteria.setDepartmentId(departmentId);
        utmDocumentCriteria.setMinDocumentId(0L);
        utmDocumentCriteria.setSortInfo(new ArrayList<>());
        utmDocumentCriteria.getSortInfo().add(new SortInfoBean("id", SortDir.ASC));
        final MutableBoolean connectException = new MutableBoolean(false);

        long date = new Date().getTime();
        List<UtmDocument> documents = null;
        do {
            documents = service.getNotSent(utmDocumentCriteria);
            LOGGER.info("Number of documents to send {}", documents.size());
            documents
                    .parallelStream()
                    .forEach(document -> {
                        LOGGER.trace("Process name: {}", Thread.currentThread().getName());
                        try {
                            DocumentsList documentsList = utmQuerySender.sendDocument(document);
                            if (!documentsList.getUrlList().isEmpty()) {
                                document.setQueryId(documentsList.getUrlList().get(0).getValue());
                            }
                            document.setError(documentsList.getError());

                            document.setState(UtmDocumentState.SENT);
                            document.setSendDate(new Date());
                            service.saveOrUpdate(document);
                        } catch (ConnectException e) {
                            connectException.setValue(true);
                            LOGGER.error(e.getMessage(), e);
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    });
            if(new Date().getTime() - date >= maxThreadExecutionTime) {
                LOGGER.info("Interrupt sending due timeout");
                break;
            }

            utmDocumentCriteria.setMinDocumentId(documents.get(documents.size() - 1).getId());
        } while (documents.size() == DOCS_NUMBER);

        if(connectException.getValue() && departmentId != null) {
            throw new ConnectException("UTM " + departmentService.findById(departmentId).getUtmUrl() + " unavailable");
        }
    }

    void setService(UtmDocumentService service) {
        this.service = service;
    }

    void setUtmQuerySender(UtmQuerySender utmQuerySender) {
        this.utmQuerySender = utmQuerySender;
    }

    void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void setMaxThreadExecutionTime(long maxThreadExecutionTime) {
        this.maxThreadExecutionTime = maxThreadExecutionTime;
    }
}
