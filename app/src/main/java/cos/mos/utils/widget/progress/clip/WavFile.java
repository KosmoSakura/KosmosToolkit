package cos.mos.utils.widget.progress.clip;// Wav file IO class

import java.io.*;

public class WavFile {
    private enum IOState {READING, WRITING, CLOSED}

    private final static int BUFFER_SIZE = 4096;

    private final static int FMT_CHUNK_ID = 0x20746D66;
    private final static int DATA_CHUNK_ID = 0x61746164;
    private final static int RIFF_CHUNK_ID = 0x46464952;
    private final static int RIFF_TYPE_ID = 0x45564157;

    private IOState ioState;//指定Wav文件的IO状态（用于snaity检查）
    private int bytesPerSample;//存储单个样本所需的字节数
    private long numFrames; //数据部分中的帧数
    private InputStream iStream; //数据的输入流
    private double floatScale; //用于int < - > float转换的缩放
    private double floatOffset;//用于int < - > float转换的偏移

    // Wav Header
    private int numChannels;                // 2 bytes unsigned, 0x0001 (1) to 0xFFFF (65,535)
    private long sampleRate;                // 4 bytes unsigned, 0x00000001 (1) to 0xFFFFFFFF (4,294,967,295)
    private int blockAlign;                    // 2 bytes unsigned, 0x0001 (1) to 0xFFFF (65,535)
    private int validBits;                    // 2 bytes unsigned, 0x0002 (2) to 0xFFFF (65,535)

    // Buffering
    private byte[] buffer; //IO的本地缓冲区
    private int bufferPointer;//指向本地缓冲区中的当前位置
    private int bytesRead;//上次读入本地缓冲区后读取的字节数
    private long frameCounter; //前读取或写入的帧数

    // Cannot instantiate WavFile directly, must either use newWavFile() or openWavFile()
    private WavFile() {
        buffer = new byte[BUFFER_SIZE];
    }

    public int getNumChannels() {
        return numChannels;
    }

    public long getNumFrames() {
        return numFrames;
    }


    public static WavFile openWavFile(InputStream file) throws IOException, WavFileException {
        // 实例化新的Wavfile并存储文件引用
        WavFile wavFile = new WavFile();
        //wavFile.file = file;
        // 创建用于读取文件数据的新文件输入流
        wavFile.iStream = file;

        // 读取文件的前12个字节
        int bytesRead = wavFile.iStream.read(wavFile.buffer, 0, 12);
        if (bytesRead != 12) throw new WavFileException("Not enough wav file bytes for header");

        //从标题中提取部分
        long riffChunkID = getLE(wavFile.buffer, 0, 4);
        long chunkSize = getLE(wavFile.buffer, 4, 4);
        long riffTypeID = getLE(wavFile.buffer, 8, 4);

        // 检查标头字节包含正确的签名
        if (riffChunkID != RIFF_CHUNK_ID)
            throw new WavFileException("Invalid Wav Header data, incorrect riff chunk ID");
        if (riffTypeID != RIFF_TYPE_ID)
            throw new WavFileException("Invalid Wav Header data, incorrect riff type ID");

//		// 检查文件大小是否与标头中列出的字节数相匹配
//		if (file.length() != chunkSize+8) {
//			throw new WavFileException("Header chunk size (" + chunkSize + ") does not match file size (" + file.length() + ")");
//		}

        boolean foundFormat = false;
        boolean foundData = false;

        // 搜索格式和数据块
        while (true) {
            //读取块的前8个字节（ID和块大小）
            bytesRead = wavFile.iStream.read(wavFile.buffer, 0, 8);
            if (bytesRead == -1)
                throw new WavFileException("Reached end of file without finding format chunk");
            if (bytesRead != 8) throw new WavFileException("Could not read chunk header");

            // 提取块ID和大小
            long chunkID = getLE(wavFile.buffer, 0, 4);
            chunkSize = getLE(wavFile.buffer, 4, 4);

            // Word align the chunk size
            // chunkSize specifies the number of bytes holding data. However,
            // the data should be word aligned (2 bytes) so we need to calculate
            // the actual number of bytes in the chunk
            int numChunkBytes = (int) ((chunkSize % 2 == 1) ? chunkSize + 1 : chunkSize);

            if (chunkID == FMT_CHUNK_ID) {
                // 标记已找到格式块
                foundFormat = true;

                // Read in the header info
                bytesRead = wavFile.iStream.read(wavFile.buffer, 0, 16);

                // 检查是否是未压缩的数据
                int compressionCode = (int) getLE(wavFile.buffer, 0, 2);
                if (compressionCode != 1)
                    throw new WavFileException("Compression Code " + compressionCode + " not supported");

                // 提取格式信息
                wavFile.numChannels = (int) getLE(wavFile.buffer, 2, 2);
                wavFile.sampleRate = getLE(wavFile.buffer, 4, 4);
                wavFile.blockAlign = (int) getLE(wavFile.buffer, 12, 2);
                wavFile.validBits = (int) getLE(wavFile.buffer, 14, 2);

                if (wavFile.numChannels == 0)
                    throw new WavFileException("Number of channels specified in header is equal to zero");
                if (wavFile.blockAlign == 0)
                    throw new WavFileException("Block Align specified in header is equal to zero");
                if (wavFile.validBits < 2)
                    throw new WavFileException("Valid Bits specified in header is less than 2");
                if (wavFile.validBits > 64)
                    throw new WavFileException("Valid Bits specified in header is greater than 64, this is greater than a long can hold");

                // 计算保存1个样本所需的字节数
                wavFile.bytesPerSample = (wavFile.validBits + 7) / 8;
                if (wavFile.bytesPerSample * wavFile.numChannels != wavFile.blockAlign)
                    throw new WavFileException("Block Align does not agree with bytes required for validBits and number of channels");

                // Account for number of format bytes and then skip over
                // any extra format bytes
                numChunkBytes -= 16;
                if (numChunkBytes > 0) wavFile.iStream.skip(numChunkBytes);
            } else if (chunkID == DATA_CHUNK_ID) {
                // Check if we've found the format chunk,
                // If not, throw an exception as we need the format information
                // before we can read the data chunk
                if (foundFormat == false)
                    throw new WavFileException("Data chunk found before Format chunk");

                // Check that the chunkSize (wav data length) is a multiple of the
                // block align (bytes per frame)
                if (chunkSize % wavFile.blockAlign != 0)
                    throw new WavFileException("Data Chunk size is not multiple of Block Align");

                // Calculate the number of frames
                wavFile.numFrames = chunkSize / wavFile.blockAlign;

                // Flag that we've found the wave data chunk
                foundData = true;

                break;
            } else {
                // If an unknown chunk ID is found, just skip over the chunk data
                wavFile.iStream.skip(numChunkBytes);
            }
        }

        // Throw an exception if no data chunk has been found
        if (foundData == false) throw new WavFileException("Did not find a data chunk");

        // Calculate the scaling factor for converting to a normalised double
        if (wavFile.validBits > 8) {
            // If more than 8 validBits, data is signed
            // Conversion required dividing by magnitude of max negative value
            wavFile.floatOffset = 0;
            wavFile.floatScale = 1 << (wavFile.validBits - 1);
        } else {
            // Else if 8 or less validBits, data is unsigned
            // Conversion required dividing by max positive value
            wavFile.floatOffset = -1;
            wavFile.floatScale = 0.5 * ((1 << wavFile.validBits) - 1);
        }

        wavFile.bufferPointer = 0;
        wavFile.bytesRead = 0;
        wavFile.frameCounter = 0;
        wavFile.ioState = IOState.READING;

        return wavFile;
    }

    // 从本地缓冲区获取并放置小端数据
    private static long getLE(byte[] buffer, int pos, int numBytes) {
        numBytes--;
        pos += numBytes;
        long val = buffer[pos] & 0xFF;
        for (int b = 0; b < numBytes; b++) val = (val << 8) + (buffer[--pos] & 0xFF);
        return val;
    }


    private long readSample() throws IOException, WavFileException {
        long val = 0;
        for (int b = 0; b < bytesPerSample; b++) {
            if (bufferPointer == bytesRead) {
                int read = iStream.read(buffer, 0, BUFFER_SIZE);
                if (read == -1) throw new WavFileException("Not enough data available");
                bytesRead = read;
                bufferPointer = 0;
            }
            int v = buffer[bufferPointer];
            if (b < bytesPerSample - 1 || bytesPerSample == 1) v &= 0xFF;
            val += v << (b * 8);
            bufferPointer++;
        }
        return val;
    }


    public int readFrames(double[] sampleBuffer, int numFramesToRead) throws IOException, WavFileException {
        int offset = 0;
        if (ioState != IOState.READING) throw new IOException("Cannot read from WavFile instance");

        for (int f = 0; f < numFramesToRead; f++) {
            if (frameCounter == numFrames) return f;
            for (int c = 0; c < numChannels; c++) {
                sampleBuffer[offset] = floatOffset + (double) readSample() / floatScale;
                offset++;
            }
            frameCounter++;
        }
        return numFramesToRead;
    }


    public void close() throws IOException {
        // 关闭输入流并置空
        if (iStream != null) {
            iStream.close();
            iStream = null;
        }
        // 修改为流关闭状态
        ioState = IOState.CLOSED;
    }

}
