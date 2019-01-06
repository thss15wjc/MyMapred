import java.net.URI;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class WordCount {
    static final String INPUT_PATH = "hdfs://localhost:9000/input";
    static final String OUT_PATH = "hdfs://localhost:9000/output";

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf);
        job.initFile("hdfs://localhost:9000");
        job.openFileIn("/input/testinput.txt");
        job.openFileOut("/output/testoutput.txt");
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);
        job.run();
        job.closeFileIn();
        job.closeFileOut();
    }

    static class MyMapper extends
            Mapper<LongWritable, Text, Text, LongWritable> {
        protected void map(LongWritable k1, Text v1, MapContextImpl<LongWritable, Text, Text, LongWritable> context)
                throws java.io.IOException, InterruptedException {
            String[] splited = v1.toString().split(" ");
            for (String word : splited) {
                context.write(new Text(word), new LongWritable(1));
            }
        }
    }

    static class MyReducer extends
            Reducer<Text, LongWritable, Text, LongWritable> {
        protected void reduce(Text k2, List<LongWritable> v2s,
                              ReduceContextImpl<Text, LongWritable, Text, LongWritable> ctx) throws java.io.IOException,
                InterruptedException {
            long times = 0L;
            for (int i=0; i<v2s.size(); i++) {
            	LongWritable count = v2s.get(i);
                times += count.get();
            }
            ctx.write(k2, new LongWritable(times));
        };
    }
}
