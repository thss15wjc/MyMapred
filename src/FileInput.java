import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.commons.lang.*;
import org.apache.log4j.Logger;

public class FileInput {
    private String HDFSUri = "";
    private Logger logger = Logger.getLogger(Mapper.class);
    private Configuration conf = new Configuration();
    private FileSystem fs;
    private FSDataInputStream hdfsInStream;

    private byte[] ioBuffer;
    private int curIndex;
    private int atEOF;
    private char separator = '\n';
    private int bufferSize = 1024;

    public static void main(String[] args) throws Exception {
        FileInput fileInput = new FileInput();
        String hdfsUri = "hdfs://localhost:9000";
        fileInput.setHDFSUri(hdfsUri);
        fileInput.conf.set("fs.defaultFS", hdfsUri);
        //FileSystem fs = FileSystem.get(conf);
        //mkdir("/xxx");
        fileInput.openFile("/input/testinput.txt");
        while (fileInput.atEOF == 0) {
            System.out.println("# " + fileInput.getLine() + " #");
        }
        fileInput.closeFile();
    }

    public void setHDFSUri(String hdfsUri) {
        HDFSUri = hdfsUri;
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

    public void openFile(String path) throws IOException {
        fs = getFileSystem();
        String hdfsUri = HDFSUri;
        if (StringUtils.isNotBlank(hdfsUri)) {
            path = hdfsUri + path;
        }
        hdfsInStream = fs.open(new Path(path));
        ioBuffer = new byte[bufferSize];
        curIndex = -1;
        atEOF = 0;
    }

    public void closeFile() throws IOException {
        hdfsInStream.close();
        fs.close();
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
}
