import play.jobs.Job;
import play.jobs.OnApplicationStop;

/**
 * Created by rafael on 13/06/15.
 */
@OnApplicationStop
public class BootstrapStop extends Job {

    @Override
    public void doJob() throws Exception {
        System.out.println("BYE!");
    }
}
