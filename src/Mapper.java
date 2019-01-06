import java.io.*;


public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
    protected void setup(MapContextImpl<KEYIN, VALUEIN, KEYOUT, VALUEOUT> context)
            throws IOException, InterruptedException {
        // NOTHING
    }

    protected void map(KEYIN key, VALUEIN value,
                       MapContextImpl<KEYIN, VALUEIN, KEYOUT, VALUEOUT> context)
            throws IOException, InterruptedException {
       // context.write((KEYOUT) key, (VALUEOUT) value);
    }

    protected void cleanup(MapContextImpl<KEYIN, VALUEIN, KEYOUT, VALUEOUT> context)
            throws IOException, InterruptedException {
        // NOTHING
    }

    public void run(MapContextImpl<KEYIN, VALUEIN, KEYOUT, VALUEOUT> context)
            throws IOException, InterruptedException {
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
