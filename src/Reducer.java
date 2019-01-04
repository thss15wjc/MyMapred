import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.util.Iterator;


public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {

  /**
   * The <code>Context</code> passed on to the {@link Reducer} implementations.
   */
  public abstract class Context 
    implements ReducerContext<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {
  }

  static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	  protected void reduce(Text key, Iterable<IntWritable> values, Context context
              ) throws IOException, InterruptedException {
		  int outvalue = 0;
		for(IntWritable value: values) {
		outvalue += value.get();
		context.write(key, new IntWritable(outvalue));
		}
}
  }
  /**
   * Called once at the start of the task.
   */
  protected void setup(Context context
                       ) throws IOException, InterruptedException {
    // NOTHING
  }

  protected void reduce(KEYIN key, Iterable<VALUEIN> values, Context context
                        ) throws IOException, InterruptedException {
	  int outvalue = 0;
	  for(VALUEIN value: values) {
		  context.write((KEYOUT) key, (VALUEOUT) value);
    }
  }

  protected void cleanup(Context context
                         ) throws IOException, InterruptedException {
    // NOTHING
  }

  public void run(Context context) throws IOException, InterruptedException {
    setup(context);
    try {
      while (context.nextKey()) {
        reduce(context.getCurrentKey(), context.getValues(), context);
        // If a back up store is used, reset it
        Iterator<VALUEIN> iter = context.getValues().iterator();
        if(iter instanceof ReducerContext.ValueIterator) {
          ((ReducerContext.ValueIterator<VALUEIN>)iter).resetBackupStore();        
        }
      }
    } finally {
      cleanup(context);
    }
  }
}
