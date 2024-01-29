/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.enums;

/**
 *
 * @author samueladebowale
 */
public enum ProcessStatus {

    SUCCESS("successful"),
    FAILED("failed"),
    IN_PROGRESS("in-progress"),
    COMPLETED("completed");

    private String status;

    public String getProcessStatus() {
        return status;
    }

    ProcessStatus(final String status) {
        this.status = status;
    }

    public String toString() {
        return status;
    }
}
