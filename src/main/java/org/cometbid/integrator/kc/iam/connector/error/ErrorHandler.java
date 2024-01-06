/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.error;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.cometbid.integrator.kc.iam.connector.exception.ConflictException;
import org.cometbid.integrator.kc.iam.connector.exception.UnexpectedStatusException;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.keycloak.util.JsonSerialization;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class ErrorHandler {

    /**
     *
     * @param e
     * @throws IOException
     * @throws ClientErrorException
     */
    public static void handleClientErrorException(ClientErrorException e) throws ClientErrorException, IOException {
        //e.printStackTrace();
        Response response = e.getResponse();
        try {
            log.error("status: {}", response.getStatus());
            log.error("reason: {}", response.getStatusInfo().getReasonPhrase());

            Map error = JsonSerialization.readValue((ByteArrayInputStream) response.getEntity(), Map.class);

            log.error("error: " + error.get("error"));
            log.error("error_description: " + error.get("error_description"));

            SynchronousDispatcher.rethrow(e);
        } catch (IOException ex) {
            //ex.printStackTrace();
            log.error("error: {}", ex);

            SynchronousDispatcher.rethrow(ex);
        }
    }

    /**
     *
     * @param e
     * @param errorMessage
     * @throws ClientErrorException
     * @throws IOException
     */
    public static void handleException(Exception e, String errorMessage)
            throws NotFoundException, ClientErrorException, IOException {
        Throwable cause = e.getCause();

        log.error(errorMessage);

        if (cause instanceof NotFoundException) {

            throw new NotFoundException(errorMessage);

        } else if (cause instanceof ClientErrorException clientErrorException) {
            handleClientErrorException(clientErrorException);
        } else {

            throw new IOException(errorMessage);
        }
    }

    /**
     *
     * @param errorMessage
     * @throws ConflictException
     */
    public static void throwConflictException(String errorMessage) throws ConflictException {

        log.debug(errorMessage);

        throw new ConflictException(errorMessage, Response.Status.CONFLICT);
    }

    /**
     *
     * @param errorMessage
     * @param status
     * @throws UnexpectedStatusException
     */
    public static void throwUnexpectedStatusException(String errorMessage, int status) throws UnexpectedStatusException {

        log.debug(errorMessage);

        throw new UnexpectedStatusException(errorMessage, status);
    }
}
