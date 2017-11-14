package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class WsSubscriberActor extends AbstractActor {

    private ActorRef parent;
    private ActorRef out;
    private String topic;

    public static Props props(ActorRef parent, ActorRef out, String topic) {
        return Props.create(WsSubscriberActor.class, parent, out, topic);
    }

    public WsSubscriberActor(ActorRef parent, ActorRef out, String topic) {
        this.parent = parent;
        this.out = out;
        this.topic = topic;
    }

    @Override
    public void preStart() {
        parent.tell(new Actors.Subscribe(topic) , self());
    }

    @Override
    public void postStop() {
        parent.tell(new Actors.Unsubscribe(topic) , self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message ->
                        out.tell("I received your message: " + message, self())
                )
                .build();
    }

}
