import java.io.IOException;
import java.io.FileNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;

public class OutputCommitter {
	private Path workPath = null;
	private Path outputPath = null;
	public OutputCommitter(Path outputPath, 
			MapperContext context) throws IOException {
		if (outputPath != null) {
			workPath = getOutputPath(context);
		}
	}

	private Path getOutputPath(MapperContext context) {
		return this.outputPath;
	}
}
