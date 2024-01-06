/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.exception;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author samueladebowale
 */
public class UnexpectedStatusException extends ClientErrorException {

    public UnexpectedStatusException() {
        this(null, Response.Status.SERVICE_UNAVAILABLE); // 503
    }

    public UnexpectedStatusException(String message, Response.Status status) {
        super(message, status);
    }

    public UnexpectedStatusException(String message, int status) {
        super(message, status);
    }

}
