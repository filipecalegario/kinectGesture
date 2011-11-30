package music;
import basic.TimerThread;
import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;


public class MusicModule{

	OscP5 oscP5;
	NetAddress myRemoteLocation;
	
	public MusicModule() {
		this.oscP5 = new OscP5(this, 9998);
		this.myRemoteLocation = new NetAddress("127.0.0.1", 9999);
	}

	public void sendNoteOn(int pitch, int velocity) {
		OscMessage msg = new OscMessage("/hpm/1/note");
		msg.add(pitch);
		msg.add(velocity);
		msg.add(1);
		oscP5.send(msg, myRemoteLocation);
	}

	public void sendNoteOff(int pitch, int velocity) {
		OscMessage msg = new OscMessage("/hpm/1/note");
		msg.add(pitch);
		msg.add(velocity);
		msg.add(0);
		oscP5.send(msg, myRemoteLocation);
	}
	
	public void playNote(int pitch, int velocity, int miliseconds){
		sendNoteOn(pitch, velocity);
		sendNoteOff(pitch, velocity);
	}

	/**
	 * @param cc
	 *            Controller Number [0 - 127]
	 * @param value
	 *            [0.0 - 1.0]
	 */
	public void sendCC(int cc, float value) {
		OscMessage msg = new OscMessage("/hpm/1/cc/" + cc);
		msg.add(value);
		oscP5.send(msg, myRemoteLocation);
	}
	
}
