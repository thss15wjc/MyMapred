import java.io.IOException;
import java.util.List;

public class ReduceContextImpl<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
    implements ReducerContext<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
  private KeyValueMap<KEYIN,VALUEIN> input;
  private FileInputOutput writer = new FileInputOutput();
  
  public ReduceContextImpl(KeyValueMap<KEYIN,VALUEIN> input, 
          FileInputOutput output
         ) throws InterruptedException, IOException{
	//input.sort();
	this.input = input;
	this.writer = output;
	}

	public boolean nextKey() throws IOException,InterruptedException {
		return input.hasNextReduce();	
	}
	
	public KEYIN getCurrentKey() {
		return input.getkey();
	}

	
	public List<VALUEIN> getValues() throws IOException, InterruptedException {
		return input.getvalues();
	}

	public void write(KEYOUT key, VALUEOUT value) throws IOException, InterruptedException {
		String writestr = "key:" + key.toString() + " value:" + value.toString()+"\n";
		writer.putString(writestr);
	}
	
	public void next() throws IOException, InterruptedException {
		input.nextReduce();
	}
}
