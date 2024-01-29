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
package org.cometbid.integration.kc.iam.connector.realm;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import org.cometbid.integration.kc.iam.connector.exception.ConflictException;
import org.keycloak.representations.idm.RealmRepresentation;

/**
 *
 * @author samueladebowale
 */
public sealed interface RealmManagerIT permits RealmManager {

    void createRealm(String realmName) throws ClientErrorException, IOException;

    void createRealm(RealmRepresentation customRealmRepresentation) throws ClientErrorException, IOException;

    void clearRealmKeysCache() throws ClientErrorException, NotFoundException, IOException;

    void clearRealmCache() throws ClientErrorException, NotFoundException, IOException;

    void clearRealmUserCache() throws ClientErrorException, NotFoundException, IOException;

    void makeDefaultGroup(String groupId) throws ConflictException, ClientErrorException, IOException;

    void deleteDefaultGroup(String groupId) throws ConflictException, ClientErrorException, IOException;

    void resetRealm(String realmName) throws NotFoundException, ClientErrorException, IOException;

    void logoutAll(String realmName) throws NotFoundException, ClientErrorException, IOException;
}
