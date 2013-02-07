package org.apache.tapestry5.internal.mongodb;

import org.apache.tapestry5.mongodb.JongoSessionSource;
import org.apache.tapestry5.mongodb.MongoDB;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;

/**
 *
 */
public class JongoSessionSourceImpl implements JongoSessionSource
{
	private final Logger logger;
	private final MongoDB mongoDB;

	private final Jongo jongo;

	public JongoSessionSourceImpl(Logger logger, MongoDB mongoDB)
	{
		this.logger = logger;
		this.mongoDB = mongoDB;

		jongo = new Jongo(mongoDB.getDefaultMongoDb());
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
}
