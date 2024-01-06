/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.enums;

/**
 *
 * @author samueladebowale
 */
public enum ProfileStatus {

    PROFILE_LOCKED("profile_locked"),
    PROFILE_EXPIRED("profile_expired"),
    PROFILE_ACTIVE("profile_status"),
    PASSWORD_EXPIRED("password_expired");

    private String status;

    public String getProfileStatus() {
        return status;
    }

    ProfileStatus(final String status) {
        this.status = status;
    }

    public String toString() {
        return status;
    }
}
