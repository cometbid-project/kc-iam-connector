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
public class ConflictException extends ClientErrorException {

    public ConflictException() {
        this(null, Response.Status.CONFLICT); // 409
    }

    public ConflictException(String message, Response.Status status) {
        super(message, status); // 409
    }
}
