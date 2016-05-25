import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * Created by rafael on 13/06/15.
 */
@OnApplicationStart
public class BootstrapStart extends Job {

    @Override
    public void doJob() throws Exception {
        System.out.println("TAPDATASS!");
    }
}
