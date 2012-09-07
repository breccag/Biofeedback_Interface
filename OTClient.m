classdef OTClient < handle
    
    properties (SetAccess=protected)
        ConnectorIP;
        ConnectorPort;
        hSocket;
        Samplerate;
        Channels;
        nEMG;
        nAux;
    end
    
    methods (Access=public)
        function obj = OTClient(host, port)
            % init connection:	obj = OTClient(host, port)
            %                   if connection fails
            if nargin == 0
                host = 'localhost';
                port = 31000;
            end

            % Change for individual recorder host
            obj.ConnectorIP = host;
            obj.ConnectorPort = port;
            disp('Trying to connect to OTBioLab');
            % the pid will take the com_socket
            obj.hSocket = JavaSocket(obj.ConnectorIP, obj.ConnectorPort);
            obj.Samplerate = int32(1000);
            obj.Channels = int32(16);
            obj.hSocket.setReceiveBufferSize(20*2048*16);
            % obj.hSocket.writeBytes(uint8('OBMatlabConnector'))
            
        end
        
        function newdata = readData(obj)
            bytesPerBlock = obj.Channels * 2;
            bytes_available = obj.hSocket.checkBuffer();
            TimeOut = tic();
            while(bytes_available < bytesPerBlock)
                pause(0.005);
                bytes_available = obj.hSocket.checkBuffer();
                if (toc(TimeOut) > 10)   % break after 10 seconds
                    break;
                end
            end
            blockCount = floor(bytes_available/bytesPerBlock);
            if (blockCount)
                newdata = obj.hSocket.readInt16(obj.Channels*blockCount)';
                newdata = reshape(newdata, obj.Channels, []);
            else
                disp('empty fetch');
                newdata = [];
            end
        end
        
        function control(obj, command)
            obj.hSocket.writeBytes(uint8(command));
        end
        
        function getConfig(obj)
            while(obj.checkbuffer())
                obj.clearbuffer();
            end
            obj.control('config');
            config = obj.hSocket.readInt16(3);
            obj.Samplerate = config(1);
            obj.nEMG = config(2);
            obj.nAux = config(3);
            obj.Channels = double(obj.nEMG + obj.nAux);
            
            disp(['System is configured for:']);
            disp(['Samplerate:  ', num2str(obj.Samplerate)]);
            disp(['EMG:  ', num2str(obj.nEMG), ' Channels']);
            disp(['AUX:  ', num2str(obj.nAux), ' Channels']);
        end
        
        function clearbuffer(obj)
            % disp('Clearing buffer');
            bytesAvailable = obj.hSocket.checkBuffer();
            while(bytesAvailable)
                dummy = obj.hSocket.readBytes(bytesAvailable);
                bytesAvailable = obj.hSocket.checkBuffer();
            end
        end
        function delete(obj)
            try
                obj.hSocket.writeBytes(int8('disconnect'));
            catch e
            end
            obj.hSocket.close();
        end
        function BytesAvailable = checkbuffer(obj)
            BytesAvailable = obj.hSocket.checkBuffer();
            %disp([num2str(BytesAvailable), ' Bytes in buffer']);
        end
    end
end