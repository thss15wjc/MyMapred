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

public class RecordReader <KEYIN, VALUEIN> {
	public class LineRecordReader extends RecordReader<LongWritable, Text> {
		private long start;
		private long pos;
		private long end;
		private LongWritable key = null;
	    private Text value = null;
	    private FSDataInputStream fin = null;
	    private LineReader reader = null;
		public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
			FileSplit fileSplit = (FileSplit) split;
			start = fileSplit.getStart();
			end = start + fileSplit.getLength();
			Configuration conf = context.getConfiguration();
			Path path = fileSplit.getPath();
			FileSystem fileSystem = path.getFileSystem(conf);
			fin = fileSystem.open(path);
			fin.seek(start);
			reader = new LineReader(fin);
			pos = start;
		}
		public boolean nextKeyValue() throws IOException, InterruptedException {
			if(key == null){
                key = new LongWritable();
            }
            key.set(pos);
            if(value == null){
                value = new Text();
            }
            int newSize = 0;
            if (pos < end) {
            	newSize = reader.readLine(value);
            	pos += newSize;
            }
            if(newSize==0){
                return false;
            } else return true;

		}
		public LongWritable getCurrentKey() throws IOException, InterruptedException {
			return key;
		}
		public Text getCurrentValue() throws IOException, InterruptedException {
			return value;
		}
		public float getProgress() throws IOException, InterruptedException {
			if (start ==end) 
				return 0.0f;
			else 
				return Math.min(1.0f, (pos -start) / (float)(end -start));
		}
		public void close() throws IOException {
	        fin.close();
	    }
	}
}
