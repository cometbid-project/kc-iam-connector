/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author samueladebowale
 */
public interface AuthenticationManagerIT {
    
    void logout(final String username) throws ClientErrorException, NotFoundException, IOException;
    
    Map<String, Object> impersonate(final String username) throws ClientErrorException, NotFoundException, IOException;
}
