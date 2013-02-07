package org.apache.tapestry5.mongodb;

import org.apache.tapestry5.ioc.annotations.UsesConfiguration;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.Collection;

/**
 * Service to get access to the configurated Jongo implementation
 */
@UsesConfiguration(String.class)
public interface JongoSessionSource
{
	/**
	 * Obtain each classes mapped by Jongo
	 * @return collection of classes mapped by Jongo
	 */
	public Collection<Class> getMappedClasses();

	/**
	 * Access to the source Jongo implementation
	 * @return the <code>Jongo</code> configured instance
	 */
	public Jongo openJongo();

	/**
	 * Access to or create the collection named <code>collectionName</code> in the configured
	 * default DB.
	 *
	 * @param collectionName the name of the collection
	 * @return the MongoCollection associated
	 */
	public MongoCollection obtainCollation(String collectionName);
}
