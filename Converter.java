import java.io.*;
import java.nio.*;

// Enable Matlab to read from a stream more than one byte. The stream 
// has to be passed an will then be used by this class. Also the 
// transformation from Byte array to int32 or int16 is provided by
// this class to speed up calculation.
// The number of elements in the desired data type needs to be passed
// and will be received in bytes and transformed.
// The typecast is optimized for the 

class Converter
{
    public Converter()
    {
        
    }

	public short[] convert(byte[] bytes)
    {
	// Read bytes from the NetworkStream and typecast them to int16. 
	// The function needs the number of int16 elements to retrieve.
        //byte[] bytes = this.readBytes(2*length);
		int length = bytes.length/2;
        short[] buffer = new short[length];
        int temp = 0;
        for(int i=0; i<length; i++){
            temp = 	( 0xFF & bytes[2*i+1] ) << 8;
            temp |= (bytes[2*i]& 0xFF);
            buffer[i] =  (short)temp;
        }
        return buffer;
    }
    
	public short[] convert2(byte[] bytes)
    {
	// Read bytes from the NetworkStream and typecast them to int16. 
	// The function needs the number of int16 elements to retrieve.
        //byte[] bytes = this.readBytes(2*length);
		int length = bytes.length/2;
        short[] buffer = new short[length];
        int temp = 0;
        for(int i=0; i<length; i++){
            temp = 	( 0xFF & bytes[2*i+1] ) << 8;
            temp += (bytes[2*i]& 0xFF);
            buffer[i] =  (short)temp;
        }
        return buffer;
    }
	
	public short convert3(byte[] bytes)
    {
	// Read bytes from the NetworkStream and typecast them to int16. 
	// The function needs the number of int16 elements to retrieve.
        //byte[] bytes = this.readBytes(2*length);
		int length = bytes.length/2;
		ByteBuffer buffer = ByteBuffer.allocate(2*length);        
        buffer.put(bytes);
        return buffer.getShort(0);
    }
	
/*     public int[] readInt32(int length)
    {
	// Read out int32 values, number of in32 elements needs to be passed.
        byte[] bytes = this.readBytes(4*length);
        int[] buffer = new int[length];
        for(int i=0; i<length; i++){
            buffer[i] = (bytes[4*i+3] << 24) | 
                        ((bytes[4*i+2] & 0xFF) << 16) |
                        ((bytes[4*i+1] & 0xFF) << 8) | 
                        (bytes[4*i] & 0xFF);
        }
        return buffer;
    } */
	

    //private DataInput NetworkStream;
}
