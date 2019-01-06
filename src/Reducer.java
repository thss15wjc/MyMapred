import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.util.Iterator;
import java.util.List;


public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {
  protected void setup(ReduceContextImpl<KEYIN,VALUEIN,KEYOUT,VALUEOUT> context
                       ) throws IOException, InterruptedException {
    // NOTHING
  }

  protected void reduce(KEYIN key, List<VALUEIN> values, ReduceContextImpl<KEYIN,VALUEIN,KEYOUT,VALUEOUT> context
                        ) throws IOException, InterruptedException {
	  for (int i=0;i<values.size();i++) {
		  context.write((KEYOUT) key, (VALUEOUT) values.get(i));
    }
  }

  protected void cleanup(ReduceContextImpl<KEYIN,VALUEIN,KEYOUT,VALUEOUT> context
                         ) throws IOException, InterruptedException {
    // NOTHING
  }

  public void run(ReduceContextImpl<KEYIN,VALUEIN,KEYOUT,VALUEOUT> context) throws IOException, InterruptedException {
    setup(context);
    try {
      while (context.nextKey()) {
        reduce(context.getCurrentKey(), context.getValues(), context);
        context.next();
      }
    } finally {
      cleanup(context);
    }
  }
}
