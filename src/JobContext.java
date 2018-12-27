import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.RawComparator;


public class JobContext {
	private Configuration conf;
	public JobContext(Configuration conf) {
	    this.conf = conf;
	  }
	public Configuration getConfiguration() {
	    return conf;
	  }
}
