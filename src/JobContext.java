import java.io.IOException;
import org.apache.hadoop.conf.Configuration;

public class JobContext {
	private Configuration conf;
	public JobContext(Configuration conf) {
	    this.conf = conf;
	  }
	public Configuration getConfiguration() {
	    return conf;
	  }
}
