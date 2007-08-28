package org.jsecurity.web.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsecurity.JSecurityException;
import org.jsecurity.util.ClassUtils;
import org.jsecurity.util.Initializable;
import org.jsecurity.web.WebStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditor;

/**
 * TODO class JavaDoc
 *
 * @author Les Hazlewood
 */
public abstract class AbstractWebStore<T> extends SecurityWebSupport implements WebStore<T>, Initializable {

    public static final String DEFAULT_NAME = "name";

    protected transient final Log log = LogFactory.getLog( getClass() );

    protected String name = DEFAULT_NAME;

    protected boolean checkRequestParams = true;

    protected boolean mutable = true;

    /**
     * Property editor class to use to convert IDs to and from strings.
     */
    private Class<? extends PropertyEditor> editorClass = null;

    public AbstractWebStore() {
        this( DEFAULT_NAME, true );
    }

    public AbstractWebStore( String name ) {
        this( name, true );
    }

    public AbstractWebStore( String name, boolean checkRequestParams ) {
        setName( name );
        setCheckRequestParams( checkRequestParams );
    }

    public AbstractWebStore( String name, Class<? extends PropertyEditor> editorClass ) {
        this( name, true, editorClass );
    }

    public AbstractWebStore( String name, boolean checkRequestParams, Class<? extends PropertyEditor> editorClass ) {
        setName( name );
        setCheckRequestParams( checkRequestParams );
        setEditorClass( editorClass );
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public boolean isCheckRequestParams() {
        return checkRequestParams;
    }

    public void setCheckRequestParams( boolean checkRequestParams ) {
        this.checkRequestParams = checkRequestParams;
    }

    public Class<? extends PropertyEditor> getEditorClass() {
        return editorClass;
    }

    /**
     * If set, an instance of this class will be used to convert a Session ID to a string value (and vice versa) when
     * reading and populating values in
     * {@link javax.servlet.http.HttpServletRequest HttpServletRequest}s, {@link javax.servlet.http.Cookie Cookie}s or
     * {@link javax.servlet.http.HttpSession HttpSession}s.
     * <p/>
     * <p>If not set, the string itself will be used.
     *
     * @param editorClass {@link PropertyEditor PropertyEditor} implementation used to
     *                    convert between string values and sessionId objects.
     */
    public void setEditorClass( Class<? extends PropertyEditor> editorClass ) {
        this.editorClass = editorClass;
    }

    /**
     * Returns <tt>true</tt> if the value stored can be changed once it has been set, <tt>false</tt> if it cannot.
     * <p>Default is <tt>true</tt>.
     *
     * @return <tt>true</tt> if the value stored can be changed once it has been set, <tt>false</tt> if it cannot.
     */
    public boolean isMutable() {
        return mutable;
    }

    public void setMutable( boolean mutable ) {
        this.mutable = mutable;
    }

    public void init() {
    }

    protected T fromStringValue( String stringValue ) {
        Class clazz = getEditorClass();
        if ( clazz == null ) {
            try {
                return (T)stringValue;
            } catch ( Exception e ) {
                String msg = "If the Generics type is not String, you must specify the 'editorClass' property.";
                throw new JSecurityException( msg, e );
            }
        } else {
            PropertyEditor editor = (PropertyEditor)ClassUtils.newInstance( getEditorClass() );
            editor.setAsText( stringValue );
            Object value = editor.getValue();
            try {
                T retVal = (T)value;
                return retVal;
            } catch ( ClassCastException e ) {
                String msg = "Returned value from PropertyEditor does not match the specified Generics type.";
                throw new JSecurityException( msg, e );
            }
        }
    }

    protected String toStringValue( T value ) {
        Class clazz = getEditorClass();
        if ( clazz == null ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "No 'editorClass' property set - returning value.toString() as the string value for " +
                    "method argument." );
            }
            return value.toString();
        } else {
            PropertyEditor editor = (PropertyEditor)ClassUtils.newInstance( getEditorClass() );
            editor.setValue( value );
            return editor.getAsText();
        }
    }

    protected T getFromRequestParam( HttpServletRequest request ) {
        T value = null;

        String paramName = getName();
        String paramValue = request.getParameter( paramName );
        if ( paramValue != null ) {
            if ( log.isTraceEnabled() ) {
                log.trace( "Found string value [" + paramValue + "] from HttpServletRequest parameter [" + paramName + "]" );
            }
            value = fromStringValue( paramValue );
        } else {
            if ( log.isTraceEnabled() ) {
                log.trace( "No string value found in the HttpServletRequest under parameter named [" + paramName + "]" );
            }
        }

        return value;
    }

    public final T retrieveValue( HttpServletRequest request, HttpServletResponse response ) {
        T value = null;
        if ( isCheckRequestParams() ) {
            value = getFromRequestParam( request );
        } else {
            value = onRetrieveValue( request, response );
        }
        return value;
    }

    protected abstract T onRetrieveValue( HttpServletRequest request, HttpServletResponse response );

    public void storeValue( T value, HttpServletRequest request, HttpServletResponse response ) {
        if ( value == null ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "Will not store a null value - returning." );
                return;
            }
        }

        if ( !isMutable() ) {
            Object existing = onRetrieveValue( request, response );
            if ( existing != null ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "Found existing value stored under name [" + getName() + "].  Ignoring new " +
                        "storage request - this store is immutable after the value has initially been set." );
                }
            }
            return;
        }

        onStoreValue( value, request, response );
    }

    protected abstract void onStoreValue( T value, HttpServletRequest request, HttpServletResponse response );
}
