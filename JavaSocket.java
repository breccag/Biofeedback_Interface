import java.io.*;
import java.net.*;
import java.io.DataInputStream;

// Enable Matlab to read from a stream more than one byte. The stream 
// has to be passed an will then be used by this class. Also the 
// transformation from Byte array to int32 or int16 is provided by
// this class to speed up calculation.
// The number of elements in the desired data type needs to be passed
// and will be received in bytes and transformed.
// The typecast is optimized for the 

class JavaSocket
{
    public JavaSocket(String address, int port)
    {
		try
        {
			this.NetworkSocket = new Socket(address, port);
		}
			catch (IOException e)
        {
            System.out.println("Connection could not be established");
        }
		this.createStreams();
		this.setReceiveBufferSize(4096);
    }
	public JavaSocket()
    {
		try
        {
			this.NetworkSocket = new Socket("localhost", 31000);
		}
			catch (IOException e)
        {
            System.out.println("Connection could not be established");
        }
		this.createStreams();
		this.setReceiveBufferSize(4096);
    }
	
	private void createStreams()
	{
		try
        {
			this.input_stream = this.NetworkSocket.getInputStream();
			this.output_stream = this.NetworkSocket.getOutputStream();
			NetworkStream = new DataInputStream(this.input_stream);
		}
			catch (IOException e)
        {
            System.out.println("Error while creating streams");
        }
	}
	
	public void close()
	{
		try
		{
			this.NetworkSocket.close();
			}
				catch (IOException e)
        {
            System.out.println("Error while closing connection");
        }
	}

	public void writeBytes(byte[] command)
	{
		try
        {
			this.output_stream.write(command);
		}
			catch (IOException e)
        {
            System.out.println("Error while writing bytes");
        }
	}
	
	public int checkBuffer()
	{
		try
        {
			return this.input_stream.available();
		}
			catch (IOException e)
        {
            System.out.println("Error while checking buffer");
        }
		return 0;
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
	public int[] readUshort(int length)
    {
	// Read bytes as ushort from the NetworkStream and typecast them to int32. 
	// As java does not support unsigned types, we need to live with this.
        byte[] bytes = this.readBytes(2*length);
        int[] buffer = new int[length];
        int temp = 0;
        for(int i=0; i<length; i++){
            temp = (   (bytes[2*i+1]& 0xFF) << 8) | 
                       (bytes[2*i]& 0xFF);
            buffer[i] =  (int)temp;
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
	
	public void setReceiveBufferSize(int buffersize)
	{
		try
		{
			this.NetworkSocket.setReceiveBufferSize(buffersize);
			this.NetworkSocket.setSoTimeout(500);
		}
		catch (java.net.SocketException e)
		{
			System.out.println("Could not set buffersize");
		}
	}
	
    private DataInput NetworkStream;
	private InputStream input_stream;
	private OutputStream output_stream;
	private Socket NetworkSocket;
}
