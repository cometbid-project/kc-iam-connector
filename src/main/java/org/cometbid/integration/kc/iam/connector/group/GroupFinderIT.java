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
package org.cometbid.integration.kc.iam.connector.group;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.cometbid.integration.kc.iam.connector.common.model.PagingModel;
import org.cometbid.integration.kc.iam.connector.user.KeycloakUser;

/**
 *
 * @author samueladebowale
 */
public interface GroupFinderIT {

    KeycloakGroup findGroupById(String id) throws IOException;
    
    KeycloakGroup findGroupByPath(String path) throws IOException;

    List<KeycloakGroup> findAllPaginated(final PagingModel pageModel) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakGroup> searchWithResultPaginated(SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakGroup> searchMatchingWithResultPaginated(SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;

    Map<String, Long> countAll() throws IOException;

    Map<String, Long> countAllOccurences(SearchGroupCriteria searchGroupCriteria) throws IOException;

    Map<String, Long> countTopGroups(boolean onlyTopGroups) throws IOException;

    List<KeycloakGroup> subgroupsPaginated(String groupId, SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel) throws IOException;

    List<KeycloakGroup> subgroups(String groupId) throws IOException;

    Long countSubGroups(String groupId) throws IOException;

    List<String> associatedRealmRoles(String groupId) throws IOException;

    Map<String, List<String>> associatedClientRoles(String groupId) throws IOException;

    Map<String, List<String>> attributes(String groupId) throws IOException;

    Map<String, Boolean> access(String groupId) throws IOException;

    List<KeycloakUser> associatedMembers(String id, SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel) throws IOException;

}
