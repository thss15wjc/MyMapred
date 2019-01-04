import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;


@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface ReducerContext<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {

  public boolean nextKey() throws IOException,InterruptedException;

  public Iterable<VALUEIN> getValues() throws IOException, InterruptedException;

  interface ValueIterator<VALUEIN> {
    void resetBackupStore() throws IOException;
  }
  public KEYIN getCurrentKey();
  public void write(KEYOUT key, VALUEOUT value) throws IOException, InterruptedException;
}
