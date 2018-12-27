import java.io.IOException;

public interface MapperInput<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
    public boolean nextKeyValue() throws IOException, InterruptedException;
    public KEYIN getCurrentKey() throws IOException, InterruptedException;
    public VALUEIN getCurrentValue() throws IOException, InterruptedException;
    public void write(KEYOUT key, VALUEOUT value)
            throws IOException, InterruptedException;
}
