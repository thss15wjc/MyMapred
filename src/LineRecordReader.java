import java.io.IOException;

import org.apache.hadoop.io.IntWritable;

public class LineRecordReader extends RecordReader<IntWritable, String>{
	KeyValueMap<IntWritable, String> kvmap = new KeyValueMap<IntWritable, String>();
	public void initialize(FileInputOutput input) throws IOException, InterruptedException {
		int count = 0;
		while (input.getAtEOF() == 0) {
            String str = input.getLine();            
            String[] strlist = str.split(" ");
            for (int i = 0 ; i <strlist.length ; i++ ) {
            	count++;
            	IntWritable key = new IntWritable(count);
            	kvmap.putKeyValue(key, strlist[i]);
            }      	
        }
	}
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (!kvmap.hasNextMap()) {
			return false;
		}
		return true;
	}
	@Override
	public IntWritable getCurrentKey() throws IOException, InterruptedException {
		return kvmap.getSinglekey();
	}
	@Override
	public String getCurrentValue() throws IOException, InterruptedException {
		return kvmap.getSinglevalue();
	}
	public void nextMap() throws IOException, InterruptedException {
		kvmap.nextMap();
    }
    

}
