package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.inject.Inject;
import play.libs.akka.InjectedActorSupport;
import scala.Option;

import java.util.concurrent.CompletionStage;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;

public class ParentActor extends AbstractActor implements InjectedActorSupport {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private ChildActor.Factory childFactory;

    @Inject
    public ParentActor(ChildActor.Factory childFactory) {
        this.childFactory = childFactory;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Actors.Message.class, (msg) -> {
                        logger.info("parent received content for `{}`", msg.recipient);
                        Option<ActorRef> maybe = context().child(msg.recipient);
                        ActorRef child = maybe.nonEmpty() ?
                                            maybe.get() :
                                            injectedChild(() -> {
                                                logger.info("actor for `{}` does not exist. creating one first...", msg.recipient);
                                                return childFactory.create(msg.recipient);
                                            }, msg.recipient);
                        CompletionStage<Object> future = ask(child, msg, 1000);
                        pipe(future, context().dispatcher()).to(sender());
                    }
                ).build();
    }
}