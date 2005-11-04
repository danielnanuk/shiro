/*
* Copyright (C) 2005 Les Hazlewood
*
* This library is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as published
* by the Free Software Foundation; either version 2.1 of the License, or
* (at your option) any later version.
*
* This library is distributed in the hope that it will be useful, but
* WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
* or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
* Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this library; if not, write to the
*
* Free Software Foundation, Inc.
* 59 Temple Place, Suite 330
* Boston, MA 02111-1307
* USA
*
* Or, you may view it online at
* http://www.opensource.org/licenses/lgpl-license.php
*/
package org.jsecurity.ri.authz.support;

import org.jsecurity.authc.module.AuthenticationInfo;
import org.jsecurity.authz.AuthorizationContext;
import org.jsecurity.ri.authz.AuthorizationContextFactory;

import java.security.Principal;

/**
 * Created on: Oct 24, 2005 4:35:59 PM
 *
 * @author Les Hazlewood
 */
public abstract class AbstractAuthorizationContextFactory
    implements AuthorizationContextFactory {

    public AbstractAuthorizationContextFactory(){}

    public AuthorizationContext createAuthorizationContext( AuthenticationInfo info ) {
        Principal subjectIdentifier = info.getPrincipal();
        if ( subjectIdentifier == null ) {
            String msg = "AuthenticationInfo parameter must return a non-null principal.";
            throw new IllegalArgumentException( msg );
        }
        return onCreateAuthorizationContext( info );
    }

    protected abstract AuthorizationContext onCreateAuthorizationContext( AuthenticationInfo info );

}
