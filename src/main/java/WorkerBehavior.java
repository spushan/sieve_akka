import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;

public class WorkerBehavior extends AbstractBehavior<WorkerBehavior.Command> {

    public static class Command implements Serializable {

		private static final long serialVersionUID = 1L;
		private int i;
        private boolean[] inpArr;
        private long time;

        public Command(int i, boolean[] inpArr, long time) {
            this.i = i;
            this.inpArr = inpArr;
            this.time = time;
        }

        public int getI() {
            return i;
        }

        public boolean[] getInpArr() {
            return inpArr;
        }

        public long getTime() {
            return time;
        }
    }

    private WorkerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(WorkerBehavior::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
            .onAnyMessage(Command -> {
                long start = System.currentTimeMillis();
                int index = Command.getI();
                boolean[] newInpArr = Command.getInpArr();
                if((index*index) <= 1000000) {
                    if(newInpArr[index]) {
                        for(int j = (index*index); j<=1000000; j += index) {
                            newInpArr[j] = false;
                        }
                        ActorRef<WorkerBehavior.Command> worker = getContext().spawn(WorkerBehavior.create(),"worker");
                        long end  =System.currentTimeMillis();
                        worker.tell(new Command((index+1),newInpArr, Command.getTime() + (end-start)));
                    }
                    else {
                        ActorRef<WorkerBehavior.Command> worker = getContext().spawn(WorkerBehavior.create(),"worker");
                        long end  =System.currentTimeMillis();
                        worker.tell(new Command((index+1),newInpArr,Command.getTime() + (end-start)));
                    }
                }
                else {
                    for(int k = 2; k<1000000; k++) {
                        if(newInpArr[k])
                            System.out.println(k);
                    }
                    long end  =System.currentTimeMillis();
                    System.out.println("\nExecution Time: " + Command.getTime() + (end-start) + " ms");
                }
            return this;
            })
            .build();
    }
}