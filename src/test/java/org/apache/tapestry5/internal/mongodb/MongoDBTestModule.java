package org.apache.tapestry5.internal.mongodb;

import com.mongodb.ServerAddress;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.mongodb.MongoDBSymbols;
import org.apache.tapestry5.mongodb.MorphiaSessionSource;

import java.net.UnknownHostException;

/**
 *
 */
public class MongoDBTestModule
{

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(MongoDBSymbols.DEFAULT_DB_NAME, "TapestryMorphiaTest");
        configuration.add(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM, "com.example");
    }


    public static void contributeMongoDBSource(OrderedConfiguration<ServerAddress> configuration)
    {
        try
        {
            configuration.add("test", new ServerAddress("localhost", 12345));
        }
        catch (UnknownHostException e)
        {
            throw new RuntimeException(e);
        }
    }

//    @Contribute(value = MorphiaSessionSource.class)
//    public static void contributeDefaultPackageName(Configuration<String> configuration,
//                                                    @Symbol(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM)
//                                                    String appRootPackage)
//    {
//        configuration.add("com.example.mongodb.entities");
//    }
}
