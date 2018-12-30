import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

public abstract class RecordReader <KEYIN, VALUEIN> {
	public abstract void initialize(InputSplit split, JobContext context) throws IOException, InterruptedException;
	public abstract boolean nextKeyValue() throws IOException, InterruptedException;
	public abstract KEYIN getCurrentKey() throws IOException, InterruptedException;
	public abstract VALUEIN getCurrentValue() throws IOException, InterruptedException;
	public abstract float getProgress() throws IOException, InterruptedException;
	public abstract void close() throws IOException;
	
}