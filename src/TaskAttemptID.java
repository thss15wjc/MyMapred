import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TaskAttemptID  {
  protected static final String ATTEMPT = "attempt";
  private String taskId;
  public TaskAttemptID(String taskId) {
	    if(taskId == null) {
	      throw new IllegalArgumentException("taskId cannot be null");
	    }
	    this.taskId = taskId;
	  }
}
