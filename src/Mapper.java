import java.io.*;
import java.lang.*;
import java.net.*;

import org.apache.commons.lang.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;

import org.apache.log4j.Logger;

public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
    public abstract class Context
            implements MapperContext<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
    }

    protected void setup(Context context) throws IOException, InterruptedException {
        // NOTHING
    }

    protected void map(KEYIN key, VALUEIN value,
                       Context context) throws IOException, InterruptedException {
        context.write((KEYOUT) key, (VALUEOUT) value);
    }

    protected void cleanup(Context context) throws IOException, InterruptedException {
        // NOTHING
    }

    public void run(Context context) throws IOException, InterruptedException {
        setup(context);
        try {
            while (context.nextKeyValue()) {
                map(context.getCurrentKey(), context.getCurrentValue(), context);
            }
        } finally {
            cleanup(context);
        }
    }
}
