/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import org.cometbid.integrator.kc.iam.connector.exception.ConflictException;

/**
 *
 * @author samueladebowale
 */
public sealed interface ClientManagerIT permits ClientManager {

    void createClientInRealm(String clientName)
            throws ConflictException, NotFoundException, ClientErrorException, IOException;
}
