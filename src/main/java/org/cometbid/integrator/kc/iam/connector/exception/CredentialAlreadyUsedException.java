/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.exception;

/**
 *
 * @author samueladebowale
 */
public class CredentialAlreadyUsedException extends RuntimeException {

    public CredentialAlreadyUsedException() {
        super();
    }

    public CredentialAlreadyUsedException(String message) {
        super(message);
    }

    public CredentialAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CredentialAlreadyUsedException(Throwable cause) {
        super(cause);
    }

}
