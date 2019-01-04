import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.io.serializer.Deserializer;
import org.apache.hadoop.io.serializer.SerializationFactory;
import org.apache.hadoop.io.serializer.Serializer;
import org.apache.hadoop.util.Progressable;

public class ReduceContextImpl<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
    implements ReducerContext<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
  private KeyValueMap<KEYIN,VALUEIN> input;
  private KEYIN key;                                  // current key
  private VALUEIN value;                              // current value              
  private boolean isSameKey = false;              // more w/ this key
  private ArrayList<VALUEIN> arr = new ArrayList<VALUEIN>();
  private RecordWriter<KEYOUT,VALUEOUT> writer;
  
  public ReduceContextImpl(KeyValueMap<KEYIN,VALUEIN> input, 
          RecordWriter<KEYOUT,VALUEOUT> output
         ) throws InterruptedException, IOException{
	input.sort();
	this.input = input;
	}

	public boolean nextKey() throws IOException,InterruptedException {
		while (input.it.hasNext() && isSameKey) nextKeyValue();
		if (input.it.hasNext()) {
			arr.clear();
			return nextKeyValue();
		} else return false;	
	}
	
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (!input.it.hasNext()) {
			key = null;
			value = null;
			return false;
		}
		
		KEYIN nextKey = input.nextKey();
		VALUEIN nextVal = input.nextValue();
	
		if (input.it.hasNext()) {
			nextKey = input.nextKey();
			if (key == nextKey) isSameKey = true;
		} else isSameKey = false;	
		key = nextKey;
		value = nextVal;
		arr.add(value);
		return true;
	}
	
	public KEYIN getCurrentKey() {
		return key;
	}

	public VALUEIN getCurrentValue() {
		return value;
	}
	
	public Iterable<VALUEIN> getValues() throws IOException, InterruptedException {
		Iterable<VALUEIN> valuesIt = (Iterable<VALUEIN>) arr.iterator();
		return valuesIt;
	}

	public void write(KEYOUT key, VALUEOUT value) throws IOException, InterruptedException {
		writer.write(key, value);
	}
}
