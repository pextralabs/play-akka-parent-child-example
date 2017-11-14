package actors;

import akka.actor.ActorRef;

public class Actors {

    public static class Message {
        String recipient;
        String content;
        public Message(String recipient, String message) {
            this.recipient = recipient;
            this.content = message;
        }
    }

    public static class Subscribe {
        String topic;
        public Subscribe(String topic) {
            this.topic = topic;
        }
    }

    public static class Unsubscribe {
        String topic;
        public Unsubscribe(String topic) {
            this.topic = topic;
        }
    }

}
