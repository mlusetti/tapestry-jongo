package org.apache.tapestry5.internal.mongodb;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.mongodb.JongoSessionSource;
import org.apache.tapestry5.mongodb.MongoDB;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class JongoSessionSourceImpl implements JongoSessionSource
{
	private final Logger logger;
	private final MongoDB mongoDB;

	private final Collection<Class> mappedClasses;

	private final Jongo jongo;

	public JongoSessionSourceImpl(Logger logger, MongoDB mongoDB,
								  ClassNameLocator classNameLocator, Collection<String> packages)
	{
		this.logger = logger;
		this.mongoDB = mongoDB;

		jongo = new Jongo(mongoDB.getDefaultMongoDb());

		mappedClasses = new ArrayList<Class>();

		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        for (String packageName : packages)
        {
            for (String className : classNameLocator.locateClassNames(packageName))
            {
                try
                {
                    Class entity = contextClassLoader.loadClass(className);

                    mappedClasses.add(entity);

                }
				catch (ClassNotFoundException cnfe)
                {
                    logger.error("Unable to locate {} within {}",className, packageName);

                    throw new RuntimeException(cnfe);
                }
            }
        }
	}

	/**
	 * Access to the source Jongo implementation
	 *
	 * @return the <code>Jongo</code> configured instance
	 */
	@Override
	public Jongo openJongo()
	{
		return jongo;
	}

	/**
	 * Access to or create the collection named <code>collectionName</code> in the configured
	 * default DB.
	 *
	 * @param collectionName the name of the collection
	 * @return the MongoCollection associated
	 */
	@Override
	public MongoCollection obtainCollation(String collectionName)
	{
		return jongo.getCollection(collectionName);
	}

	/**
	 * Obtain each classes mapped by Jongo
	 *
	 * @return collection of classes mapped by Jongo
	 */
	@Override
	public Collection<Class> getMappedClasses()
	{
		return mappedClasses;
	}
}
