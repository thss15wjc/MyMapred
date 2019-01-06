import java.io.IOException;

public class MapContextImpl<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
    implements MapperContext<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

    private RecordReader<KEYIN,VALUEIN> reader;
    private KeyValueMap<KEYOUT,VALUEOUT> output = new KeyValueMap();

    public MapContextImpl(RecordReader<KEYIN,VALUEIN> reader)
            throws InterruptedException, IOException {
        this.reader = reader;
    }

    public boolean nextKeyValue() throws IOException, InterruptedException {
        return reader.nextKeyValue();
    }

    public KEYIN getCurrentKey() throws IOException, InterruptedException {
        return reader.getCurrentKey();
    }

    public VALUEIN getCurrentValue() throws IOException, InterruptedException {
        return reader.getCurrentValue();
    }

    public void write(KEYOUT key, VALUEOUT value) throws IOException, InterruptedException {
        output.putKeyValue(key, value);
    }
    
    public void next() throws IOException, InterruptedException {
    	reader.nextMap();
    }
    
    public KeyValueMap getoutput() throws IOException, InterruptedException {
    	return output;
    }
}
