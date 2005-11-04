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
package org.jsecurity.session;

import org.jsecurity.JSecurityException;

import java.io.Serializable;

/**
 * General security exception attributed to problems during interaction with the system during
 * a session.
 *
 * @since 0.1
 * @author Les Hazlewood
 */
public class SessionException extends JSecurityException {

    private Serializable sessionId;

    /**
     * Creates a new SessionException.
     */
    public SessionException() {
        super();
    }

    /**
     * Constructs a new SessionException.
     * @param message the reason for the exception
     */
    public SessionException( String message ) {
        super( message );
    }

    /**
     * Constructs a new SessionException.
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public SessionException( Throwable cause ) {
        super( cause );
    }

    /**
     * Constructs a new SessionException.
     * @param message the reason for the exception
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public SessionException( String message, Throwable cause ) {
        super( message, cause );
    }

    /**
     * Constructs a new SessionException.
     * @param sessionId the session id of associated {@link Session Session}.
     */
    public SessionException( Serializable sessionId ) {
        setSessionId( sessionId );
    }

    /**
     * Constructs a new SessionException.
     * @param message the reason for the exception
     * @param sessionId the session id of associated {@link Session Session}.
     */
    public SessionException( String message, Serializable sessionId ) {
        this( message );
        setSessionId( sessionId );
    }

    /**
     * Constructs a new InvalidSessionException.
     * @param message the reason for the exception
     * @param cause the underlying Throwable that caused this exception to be thrown.
     * @param sessionId the session id of associated {@link Session Session}.
     */
    public SessionException( String message, Throwable cause, Serializable sessionId ) {
        this( message, cause );
        setSessionId( sessionId );
    }

    /**
     * Returns the session id of the associated <tt>Session</tt>.
     * @return the session id of the associated <tt>Session</tt>.
     */
    public Serializable getSessionId() {
        return sessionId;
    }

    /**
     * Sets the session id of the <tt>Session</tt> associated with this exception.
     * @param sessionId the session id of the <tt>Session</tt> associated with this exception.
     */
    public void setSessionId( Serializable sessionId ) {
        this.sessionId = sessionId;
    }

}
