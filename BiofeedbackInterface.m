%% Biofeedback Interface for Postural Correction using HD-sEMG Signals

%% ----------------- Open Connection with Device -----------------

% Open the communication with the socket open in OT BioLab on the port
% 31000. If OT BioLab is running on a different PC connected on the same
% network the 'localhost' have to be replaced by the IP addres of the PC
device = OTClient('localhost', 31000);

% Read the configuration from OT BioLab: Sample frequency, number of EMG
% channels and AUX channels
device.getConfig();

% Start reading data
device.control('start');

%% ----------------- Display Interface -----------------
Gain = 500;
%Read data from OT BioLab and plot it on a Matlab figure
% every 0.5 s.
for i = 1:500
    X = double((device.readData)*(5/2^12/100*1000));
    for j = 1:length(X)
        Y(i) = sum(X(1:64,j));
    end
    if Y >= 2500
        bar(abs(Y(:,i)),'r')
        axis([0 1 0 100])
    else Y <= 2500;
        bar(abs(Y(:,i)),'g')
        axis([0 1 0 100])
    end
end

%% ----------------- Close Connection with Device -----------------

% Stop receiving data
device.control('stop');

% Close the communication with OT BioLab
device.delete;