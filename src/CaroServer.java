
import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CaroServer {

	public static void main(String[] args) {
		new CaroServer();
		System.out.println("x");

	}

	int n = 15;
	Vector<Xuly> cls = new Vector<Xuly>();
	List<Point> dadanh = new ArrayList<Point>();
	int players = 0;
	List<Integer> a = new ArrayList<Integer>(); 

	public CaroServer() {
		ServerSocket theServer;
		Socket soc;
		try {
			theServer = new ServerSocket(5003);
			while (true) {
				soc = theServer.accept();
				Xuly x = new Xuly(soc, this);
				cls.add(x);
				x.start();
			}
		} catch (Exception e) {
			System.err.println(e);

		}
	}
}

class Xuly extends Thread {
	CaroServer server;
	Socket soc;

	public Xuly(Socket soc, CaroServer server) {
		this.soc = soc;
		this.server = server;
	}

	public void run() {

		try {
			String dadanhdau = "";
			for (Point p : server.dadanh) {
				dadanhdau += (p.x + " " + p.y + ",");
			}
			DataInputStream dis = new DataInputStream(soc.getInputStream());
			String s = dis.readUTF();
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			if (s.equals("want to join") && (server.players < 2)) {
				server.players++;
				dos.writeUTF("initital;," + dadanhdau + ";" + server.players);
				server.a.add(server.cls.size());
			} else {
				dos.writeUTF("initital;," + dadanhdau + ";" + 6);
			}

		} catch (Exception e) {
		}

		loop: while (true) {
			try {
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				String s = dis.readUTF();
				System.out.println("1" + s);
				int ix = Integer.parseInt(s.split(" ")[0]);
				int iy = Integer.parseInt(s.split(" ")[1]);

				if (server.dadanh.size() % 2 == 0 && this != server.cls.get(server.a.get(0)-1))
					continue;
				if (server.dadanh.size() % 2 == 1 && this != server.cls.get(server.a.get(1)-1))
					continue;
				for (Point p : server.dadanh) {
					if (ix == p.x && iy == p.y)
						continue loop;
				}
				if (ix < 0 || ix >= server.n)
					continue;
				if (iy < 0 || iy >= server.n)
					continue;
				server.dadanh.add(new Point(ix, iy));

				for (Xuly x : server.cls) {
					try {
						System.out.println("2" + s);
						DataOutputStream dos = new DataOutputStream(x.soc.getOutputStream());
						dos.writeUTF(s);
					} catch (Exception e1) {
					}
				}
			} catch (Exception e) {

			}
		}
	}
}
