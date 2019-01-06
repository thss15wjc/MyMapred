import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class Job {
    public static final String MAP_CLASS_ATTR = "mapreduce.job.map.class";
    public static final String REDUCE_CLASS_ATTR = "mapreduce.job.reduce.class";

    private Configuration conf;

    private FileInputOutput fileInput = new FileInputOutput();
    private FileInputOutput fileOutput = new FileInputOutput();


    public Job(Configuration conf) {
        this.conf = conf;
    }
    public Configuration getConfiguration() {
        return conf;
    }

    public void initFile(String hdfsUri) {
        fileInput.setHDFSUri(hdfsUri);
        fileOutput.setHDFSUri(hdfsUri);
    }

    public void openFileIn(String path) throws IOException {
        fileInput.openFileIn(path);
    }

    public void openFileOut(String path) throws IOException {
        fileOutput.openFileOut(path);
    }

    public void closeFileIn() throws IOException {
        fileInput.closeFileIn();
    }

    public void closeFileOut() throws IOException {
        fileOutput.closeFileOut();
    }

    public void setMapperClass(Class<? extends Mapper<LongWritable, Text, Text, LongWritable>> cls)
            throws IllegalStateException {
        conf.setClass(MAP_CLASS_ATTR, cls, Mapper.class);
    }

    public void setReducerClass(Class<? extends Reducer<Text, LongWritable, Text, LongWritable>> cls)
            throws IllegalStateException {
        conf.setClass(REDUCE_CLASS_ATTR, cls, Reducer.class);
    }

    public void run() throws ClassNotFoundException, IOException, InterruptedException {
        RecordReader<LongWritable, Text> recordReader = new RecordReader<LongWritable, Text>() {
            private FileInputOutput fileInput;
            Text line = new Text();
            LongWritable index = new LongWritable();

            @Override
            public void initialize(FileInputOutput input) throws IOException, InterruptedException {
                fileInput = input;
                index.set(-1);
            }

            @Override
            public boolean nextKeyValue() throws IOException, InterruptedException {
                if (fileInput.getAtEOF() == 1) {
                    return false;
                }
                else {
                    line.set(fileInput.getLine());
                    index.set(index.get() + 1);
                    return true;
                }
            }

            @Override
            public LongWritable getCurrentKey() throws IOException, InterruptedException {
                return index;
            }

            @Override
            public Text getCurrentValue() throws IOException, InterruptedException {
                return line;
            }

			@Override
			public void nextMap() throws IOException, InterruptedException {
				// TODO Auto-generated method stub
				
			}
        };
        recordReader.initialize(fileInput);
        MapContextImpl<LongWritable, Text, Text, LongWritable> mapContext =
                new MapContextImpl<>(recordReader);
        WordCount.MyMapper mapper = new WordCount.MyMapper();
        mapper.run(mapContext);

        System.out.println("hahaha! Map finish.");

        ReduceContextImpl<Text, LongWritable, Text, LongWritable> reduceContext =
                new ReduceContextImpl<Text, LongWritable, Text, LongWritable>(mapContext.getoutput(), fileOutput);
        WordCount.MyReducer reducer = new WordCount.MyReducer();
        reducer.run(reduceContext);
    }
}
