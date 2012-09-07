import java.io.*;

// Enable Matlab to read from a stream more than one byte. The stream 
// has to be passed an will then be used by this class. Also the 
// transformation from Byte array to int32 or int16 is provided by
// this class to speed up calculation.
// The number of elements in the desired data type needs to be passed
// and will be received in bytes and transformed.
// The typecast is optimized for the 

class StreamReader
{
    public StreamReader(DataInput data_input)
    {
        NetworkStream = data_input;
    }

	public short[] readInt16(int length)
    {
	// Read bytes from the NetworkStream and typecast them to int16. 
	// The function needs the number of int16 elements to retrieve.
        byte[] bytes = this.readBytes(2*length);
        short[] buffer = new short[length];
        int temp = 0;
        for(int i=0; i<length; i++){
            temp = (   (bytes[2*i]& 0xFF) << 8) | 
                       (bytes[2*i+1]& 0xFF);
            buffer[i] =  (short)temp;
        }
        return buffer;
    }
    
    public int[] readInt32(int length)
    {
	// Read out int32 values, number of in32 elements needs to be passed.
        byte[] bytes = this.readBytes(4*length);
        int[] buffer = new int[length];
        for(int i=0; i<length; i++){
            buffer[i] = (bytes[4*i] << 24) | 
                        ((bytes[4*i+1] & 0xFF) << 16) |
                        ((bytes[4*i+2] & 0xFF) << 8) | 
                        (bytes[4*i+3] & 0xFF);
        }
        return buffer;
    }
	
    public byte[] readBytes(int length)
    {
        byte[] buffer = new byte[length];
        try
        {
            NetworkStream.readFully(buffer, 0, length);
        }

        catch (StreamCorruptedException e)
        {
            System.out.println("Stream Corrupted Exception Occured");
            buffer = new byte[0];
        }
        catch (EOFException e)
        {
            System.out.println("EOF Reached");
            buffer = new byte[0];
        }
        catch (IOException e)
        {
            System.out.println("IO Exception Occured");
            buffer = new byte[0];
        }
        return buffer;
    }
    private DataInput NetworkStream;
}
