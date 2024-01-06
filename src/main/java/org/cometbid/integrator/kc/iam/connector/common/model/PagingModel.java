/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.common.model;

import lombok.ToString;
import lombok.Value;

/**
 *
 * @author samueladebowale
 */
@Value
@ToString(includeFieldNames = true)
public class PagingModel {

    private int pageNo;
    private int pageSize;

    /**
     * @param pageNo
     * @param pageSize
     */
    public PagingModel(int pageNo, int pageSize) {
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     *
     */
    public PagingModel() {
        super();
        this.pageNo = 1;
        this.pageSize = 10;
    }

    public int getPageNo() {
        return pageNo - 1;
    }

    public int getPageSize() {
        return pageSize;
    }
}
