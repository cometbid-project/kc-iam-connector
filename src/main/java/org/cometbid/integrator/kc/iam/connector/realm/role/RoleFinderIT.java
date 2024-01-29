/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.integrator.kc.iam.connector.realm.role;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.cometbid.integrator.kc.iam.connector.common.model.PagingModel;

/**
 *
 * @author samueladebowale
 */
public interface RoleFinderIT {

    List<KeycloakRole> findRealmRoles()
            throws ClientErrorException, NotFoundException, IOException;
    
    List<KeycloakRole> searchRealmWithMaxOccurenceResultPaginated(SearchRoleCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;
    
    List<KeycloakRole> searchRealmResultPaginated(SearchRoleCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;
    
    List<KeycloakRole> searchRealm(SearchRoleCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;

    KeycloakRole findRealmRoleByName(final String roleName)
            throws ClientErrorException, NotFoundException, IOException;

    KeycloakRole findClientRoleByName(final String roleName, final String clientId)
            throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakRole> findClientRolesById(final String uuid)
            throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakRole> findClientRolesByClientId(final String clientId)
            throws ClientErrorException, NotFoundException, IOException;

    Map<String, List<String>> findRoleAttributes(String roleName)
            throws ClientErrorException, NotFoundException, IOException;

    KeycloakComposite findRoleComposites(String roleName)
            throws ClientErrorException, NotFoundException, IOException;
}
