import java.io.IOException;

public abstract class RecordReader <KEYIN, VALUEIN> {
	public abstract void initialize(FileInputOutput input) throws IOException, InterruptedException;
	public abstract boolean nextKeyValue() throws IOException, InterruptedException;
	public abstract KEYIN getCurrentKey() throws IOException, InterruptedException;
	public abstract VALUEIN getCurrentValue() throws IOException, InterruptedException;
	public abstract void nextMap() throws IOException, InterruptedException;
}