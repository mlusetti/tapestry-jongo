package org.apache.tapestry5.mongodb;

import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.mongodb.JongoSessionSourceImpl;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.LoggerSource;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Iterator;

/**
 * Defines services which are responsible for Jongo initializations and connections.
 */
public class JongoModule
{
    public static void bind(ServiceBinder binder)
    {
    }

    @Contribute(value = JongoSessionSource.class)
    public static void contributeDefaultPackageName(Configuration<String> configuration,
					@Symbol(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM)
                    String appRootPackage)
    {
        configuration.add(appRootPackage + ".mongodb");
    }


    public static JongoSessionSource buildMorphiaSessionSource(
            Logger logger, MongoDB mongoDB,
            @Symbol(MongoDBSymbols.DEFAULT_DB_NAME) String defaultDbName,
            final Collection<String> packageNames)
    {
        return new JongoSessionSourceImpl(logger, mongoDB);
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public static void contributeValueEncoderSource(MappedConfiguration<Class, ValueEncoderFactory> configuration,
                                                    final JongoSessionSource jongoSessionSource,
                                                    final TypeCoercer typeCoercer, final PropertyAccess propertyAccess,
                                                    final LoggerSource loggerSource)
    {
        Iterator<Class> mappings = jongoSessionSource.mappedClass().iterator();

        while (mappings.hasNext())
        {
            final Class entityClass = mappings.next();

            if (entityClass != null)
            {
                ValueEncoderFactory factory = new ValueEncoderFactory()
                {
                    public ValueEncoder create(Class type)
                    {
                        return new MorphiaValueEncoder(entityClass, morphiaSessionSource, propertyAccess,
                                typeCoercer, loggerSource.getLogger(entityClass));
                    }
                };

                configuration.add(entityClass, factory);

            }
        }
    }

    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration)
    {
        configuration.add(new CoercionTuple(String.class, ObjectId.class,
                new Coercion<String, ObjectId>() {
                    /**
                     * Converts an input value.
                     *
                     * @param input the input value
                     */
                    @Override
                    public ObjectId coerce(String input)
                    {
                        return ObjectId.massageToObjectId(input);
                    }
                }));
    }

}
