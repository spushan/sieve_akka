import akka.actor.typed.ActorSystem;

public class Main {
    public static void main(String[] args) {

        ActorSystem<String> sieve = ActorSystem.create(ManagerBehavior.create(), "sieve");
        sieve.tell("start");
        long end = System.currentTimeMillis();

    }
}
