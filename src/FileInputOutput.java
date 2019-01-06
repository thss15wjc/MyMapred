import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.commons.lang.*;
import org.apache.log4j.Logger;

public class FileInputOutput {
    private String HDFSUri = "";
    private Logger logger = Logger.getLogger(Mapper.class);
    private Configuration conf = new Configuration();
    private FileSystem fsIn, fsOut;
    private FSDataInputStream hdfsInStream;
    private FSDataOutputStream hdfsOutStream;

    private byte[] ioBuffer;
    private int curIndex;
    private int atEOF;
    private char separator = '\n';
    private int bufferSize = 1024;

    public static void main(String[] args) throws Exception {
        FileInputOutput fileInputOutput = new FileInputOutput();
        String hdfsUri = "hdfs://localhost:9000";
        fileInputOutput.setHDFSUri(hdfsUri);
        //FileSystem fs = FileSystem.get(conf);
        //mkdir("/xxx");
        fileInputOutput.openFileIn("/input/testinput.txt");
        while (fileInputOutput.atEOF == 0) {
            System.out.println("# " + fileInputOutput.getLine() + " #");
        }
        fileInputOutput.closeFileIn();
        fileInputOutput.openFileOut("/output/testoutput.txt");
        fileInputOutput.putString("first line\nsecond line");
        fileInputOutput.closeFileOut();
    }

    public void setHDFSUri(String hdfsUri) {
        HDFSUri = hdfsUri;
        conf.set("fs.defaultFS", hdfsUri);
    }

    public int getAtEOF() {
        return atEOF;
    }

    public FileSystem getFileSystem() {
        FileSystem fs = null;
        String hdfsUri = HDFSUri;
        if (StringUtils.isBlank(hdfsUri)) {
            try {
                fs = FileSystem.get(conf);
            } catch (IOException e) {
                logger.error("", e);
            }
        } else {
            try {
                URI uri = new URI(hdfsUri.trim());
                fs = FileSystem.get(uri, conf);
            } catch (URISyntaxException | IOException e) {
                logger.error("", e);
            }
        }
        return fs;
    }

    public void openFileIn(String path) throws IOException {
        fsIn = getFileSystem();
        String hdfsUri = HDFSUri;
        if (StringUtils.isNotBlank(hdfsUri)) {
            path = hdfsUri + path;
        }
        hdfsInStream = fsIn.open(new Path(path));
        ioBuffer = new byte[bufferSize];
        curIndex = -1;
        atEOF = 0;
    }

    public void closeFileIn() throws IOException {
        hdfsInStream.close();
        fsIn.close();
    }

    public void openFileOut(String path) throws IOException {
        fsOut = getFileSystem();
        String hdfsUri = HDFSUri;
        if (StringUtils.isNotBlank(hdfsUri)) {
            path = hdfsUri + path;
        }
        hdfsOutStream = fsOut.create(new Path(path));
    }

    public void closeFileOut() throws IOException {
        hdfsOutStream.close();
        fsOut.close();
    }

    public void mkdir(String path) {
        try {
            FileSystem fs = getFileSystem();
            String hdfsUri = HDFSUri;
            if (StringUtils.isNotBlank(hdfsUri)) {
                path = hdfsUri + path;
            }

            fs.mkdirs(new Path(path));
            fs.close();
        } catch (IllegalArgumentException | IOException e) {
            logger.error("", e);
        }
    }

    public String getLine() throws IOException {
        if (atEOF == 1) {
            return "";
        }

        if (curIndex == -1) {
            int readLen = hdfsInStream.read(ioBuffer);
            if (readLen == -1) {
                atEOF = 1;
                return "";
            }
            curIndex = 0;
        }

        int nextIndex = curIndex;
        StringBuilder line = new StringBuilder();
        while (true) {
            while (nextIndex != bufferSize && ioBuffer[nextIndex] != separator) {
                line.append((char)ioBuffer[nextIndex]);
                nextIndex++;
            }
            if (nextIndex != bufferSize) {
                curIndex = nextIndex + 1;
                if (curIndex == bufferSize) {
                    curIndex = -1;
                }
                return line.toString();
            }
            else {
                int readLen = hdfsInStream.read(ioBuffer);
                if (readLen == -1) {
                    atEOF = 1;
                    return line.toString();
                }
                nextIndex = 0;
            }
        }
    }

    public void putString(String s) throws IOException {
        byte[] bytes = s.getBytes("UTF-8");
        hdfsOutStream.write(bytes, 0, bytes.length);
    }
}
