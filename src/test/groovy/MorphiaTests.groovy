import org.apache.tapestry5.ioc.annotations.SubModule
import org.apache.tapestry5.mongodb.MongodbCoreModule
import spock.lang.Specification
import org.apache.tapestry5.ioc.annotations.Inject

import spock.lang.Shared

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import org.apache.tapestry5.mongodb.MorphiaSessionSource
import com.google.code.morphia.Datastore
import com.example.mongodb.Address
import com.example.mongodb.Hotel
import org.apache.tapestry5.internal.mongodb.MongoDBTestModule
import org.apache.tapestry5.mongodb.MongodbWebModule

/**
 */
@SubModule([ MongodbCoreModule.class, MongodbWebModule.class, MongoDBTestModule.class ])
class MorphiaTests extends Specification
{
    final int total = 1000

    @Inject @Shared MorphiaSessionSource morphiaSessionSource

    static int PORT = 12345
    static MongodExecutable mongodExe
    static MongodProcess mongod

    @Shared Datastore ds

    def setupSpec()
    {
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        mongodExe = runtime.prepare(new MongodConfig(Version.Main.V2_0, 12345, Network.localhostIsIPv6()));
        mongod = mongodExe.start();

        ds = morphiaSessionSource.openDefaultDatastore()
    }

    def cleanupSpec()
    {
        if (mongod != null) mongod.stop()
        if (mongodExe != null) mongodExe.cleanup()
    }

    def "Lets check mongodb source"()
    {
        expect:
        morphiaSessionSource.openDefaultDatastore() != null
    }

    def "Lets populate it"()
    {
        when:
        Hotel hotel = new Hotel();
        hotel.setName("My Hotel");
        hotel.setStars(4);

        Address address = new Address();
        address.setStreet("123 Some street");
        address.setCity("Some city");
        address.setPostCode("123 456");
        address.setCountry("Some country");

        hotel.setAddress(address);

        ds.save(hotel)

        then:
        List<Hotel> fourStarHotels = ds.find(Hotel.class).field("stars").greaterThanOrEq(4).asKeyList();
        fourStarHotels.size() == 1

    }
}
