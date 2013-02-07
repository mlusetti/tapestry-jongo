package org.apache.tapestry5.mongodb;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

/**
 * Service to get access to the configurated Jongo implementation
 */
public interface JongoSessionSource
{
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
