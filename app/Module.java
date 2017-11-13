import actors.ChildActor;
import actors.ParentActor;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class Module extends AbstractModule implements AkkaGuiceSupport {
    @Override
    protected void configure() {
        bindActor(ParentActor.class, "parent");
        bindActorFactory(ChildActor.class, ChildActor.Factory.class);
    }
}


