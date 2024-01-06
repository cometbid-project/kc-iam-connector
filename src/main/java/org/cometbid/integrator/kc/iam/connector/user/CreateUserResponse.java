/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

import org.cometbid.integrator.kc.iam.connector.enums.ProcessStatus;

/**
 *
 * @author samueladebowale
 */
public record CreateUserResponse(ProcessStatus status, String totpSecret) {

    static CreateUserResponse createSuccessResponse(String totpSecret) {
        return new CreateUserResponse(ProcessStatus.SUCCESS, totpSecret);
    }

    static CreateUserResponse createFailedResponse() {
        return new CreateUserResponse(ProcessStatus.FAILED, null);
    }

    static CreateUserResponse createCompletedResponse() {
        return new CreateUserResponse(ProcessStatus.COMPLETED, null);
    }
}
