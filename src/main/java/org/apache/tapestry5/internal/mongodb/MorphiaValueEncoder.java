package org.apache.tapestry5.internal.mongodb;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.mongodb.MorphiaSessionSource;
import org.slf4j.Logger;

import java.io.Serializable;

/**
 * TODO Finish...
 */
public class MorphiaValueEncoder<E> implements ValueEncoder<E>
{
    private final Class<E> entityClass;

    private final MorphiaSessionSource morphiaSessionSource;

    private final TypeCoercer typeCoercer;

    private final PropertyAdapter propertyAdapter;

    private final Logger logger;

    public MorphiaValueEncoder(Class<E> entityClass, MorphiaSessionSource morphiaSessionSource,
                                       PropertyAccess propertyAccess, TypeCoercer typeCoercer, Logger logger)
    {
        this.entityClass = entityClass;
        this.morphiaSessionSource = morphiaSessionSource;
        this.typeCoercer = typeCoercer;
        this.logger = logger;

        propertyAdapter = propertyAccess.getAdapter(this.entityClass).getPropertyAdapter("id");
    }


    /**
     */
    @Override
    public String toClient(E value)
    {
        if (value == null)
            return null;

        Object id = propertyAdapter.get(value);

        if (id == null)
        {
            return null;
        }

        return typeCoercer.coerce(id, String.class);
    }

    /**
     *
     */
    @Override
    public E toValue(String clientValue)
    {
        if (InternalUtils.isBlank(clientValue))
            return null;

        Object id = null;

        try
        {

            id = typeCoercer.coerce(clientValue, propertyAdapter.getType());
        }
        catch (Exception ex)
        {
            throw new RuntimeException(String.format(
                    "Exception converting '%s' to instance of %s (id type for entity %s): %s", clientValue,
                    propertyAdapter.getType().getName(), entityClass.getName(), InternalUtils.toMessage(ex)), ex);
        }

        // TODO Need to handle the db (Datastore) where the ObjectId come from

        E result = (E) morphiaSessionSource.openDefaultDatastore().get(entityClass, id);

        if (result == null)
        {
            // We don't identify the entity type in the message because the logger is based on the
            // entity type.
            logger.error(String.format("Unable to convert client value '%s' into an entity instance.", clientValue));
        }

        return result;
    }
}
