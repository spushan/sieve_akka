import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ManagerBehavior extends AbstractBehavior<String> {
    private ManagerBehavior(ActorContext<String> context) {
        super(context);
    }

    public static Behavior<String> create() {
        return Behaviors.setup(ManagerBehavior::new);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start",() -> {
                    boolean[] inpArr = new boolean[1000001];
                    for(int l=0; l<1000000; l++) {
                        inpArr[l] = true;
                    }
                        ActorRef<WorkerBehavior.Command> worker = getContext().spawn(WorkerBehavior.create(),"worker");
                        worker.tell(new WorkerBehavior.Command(2, inpArr,0));
                    return this;
                })
                .build();
    }
}