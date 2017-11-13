package actors;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;

public class ChildActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private String username;

    @Inject
    public ChildActor(@Assisted String id) {
        this.username = id;
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
            }
        ).build();
    }

    public interface Factory {
        public Actor create(String id);
    }

}
