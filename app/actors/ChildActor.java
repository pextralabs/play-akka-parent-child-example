package actors;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class ChildActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private String username;
    private Set<ActorRef> subscribers;

    @Inject
    public ChildActor(@Assisted String id) {
        this.username = id;
        subscribers = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(Actors.Message.class, hello -> {
                logger.info("`{}` received \"{}\"", username, hello.content);
                sender().tell(hello.content, self());
                subscribers.forEach(
                        subscriber -> {
                            logger.info("message \"{}\" for `{}` published to {}", hello.content, username, subscriber.path());
                            subscriber.tell(hello.content, self());
                        }
                );
            })
            .match(Actors.Subscribe.class, sub -> {
                logger.info("new subscription for \"{}\"", username);
                subscribers.add(sender());
            })
            .match(Actors.Unsubscribe.class, sub -> {
                logger.info("{} unsubscribed from \"{}\"", sender().path(), username);
                subscribers.remove(sender());
            })
                .build();
    }

    public interface Factory {
        public Actor create(String id);
    }

}
