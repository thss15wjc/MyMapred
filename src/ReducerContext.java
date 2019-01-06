import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;


@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface ReducerContext<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {

  public boolean nextKey() throws IOException,InterruptedException;

  public List<VALUEIN> getValues() throws IOException, InterruptedException;
  public KEYIN getCurrentKey();
  public void write(KEYOUT key, VALUEOUT value) throws IOException, InterruptedException;
  public void next() throws IOException, InterruptedException;
}
