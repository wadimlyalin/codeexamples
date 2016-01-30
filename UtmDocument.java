package ru.v6services.auchan.alcoopt.model.utm;

import ru.v6services.auchan.alcoopt.model.Department;
import ru.v6services.auchan.alcoopt.model.common.BaseContentEntity;
import ru.v6services.auchan.alcoopt.model.constants.UtmDocumentState;

import javax.persistence.*;
import java.util.Date;

/**
 * XML-document for sending to UTM.
 * @author Vadim Lyalin
 */
@Entity
@Table(name = "UTM_DOCUMENT")
public class UtmDocument extends BaseContentEntity {
    @Id
    @SequenceGenerator(name = "SEQ", sequenceName = "SEQ_UTM_DOCUMENT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ")
    private Long id;

    /** Link to UTM */
    @Column(name = "UTM_URL")
    private String utmUrl;

    /** document state */
    @Enumerated(EnumType.STRING)
    private UtmDocumentState state;

    /** query id returned by UTM. */
    @Column(name = "QUERY_ID")
    private String queryId;

    /** error returned by UTM in case of incorrect query */
    @Column
    private String error;

    /** creation date */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /** sending date */
    @Column(name = "SEND_DATE")
    private Date sendDate;

    /** department */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHIPPER_DEPARTMENT_ID")
    private Department shipperDepartment;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUtmUrl() {
        return utmUrl;
    }

    public void setUtmUrl(String utmUrl) {
        this.utmUrl = utmUrl;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public UtmDocumentState getState() {
        return state;
    }

    public void setState(UtmDocumentState state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Department getShipperDepartment() {
        return shipperDepartment;
    }

    public void setShipperDepartment(Department shipperDepartment) {
        this.shipperDepartment = shipperDepartment;
    }
}
