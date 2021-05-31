//package com.dt.module.om.term.websocket;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//
//import org.springframework.web.socket.WebSocketSession;
//
//import com.dt.module.om.term.entity.Machine;
//
//import ch.ethz.ssh2.Connection;
//
//public class SshClient {
//
//	private Connection conn;
//	private ch.ethz.ssh2.Session sess;
//	private InputStream in;
//	private OutputStream out;
//	private BufferedWriter inToShell;
//
//	public boolean connect(Machine machine) {
//		try {
//			conn = new Connection(machine.getHostname(), machine.getPort());
//			conn.connect();
//			if (!conn.authenticateWithPassword(machine.getUsername(), machine.getPassword()))
//				return false;
//			sess = conn.openSession();
//			sess.requestPTY("xterm", 90, 30, 0, 0, null);
//			sess.startShell();
//			in = sess.getStdout();
//			out = sess.getStdin();
//			inToShell = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	public void write(String text) throws IOException {
//		if (inToShell != null) {
//			inToShell.write(text);
//			inToShell.flush();
//		}
//	}
//
//	public void startShellOutPutTask(WebSocketSession session) {
//		new ShellOutPutTask(session, in).start();
//	}
//
//	public void disconnect() {
//		if (conn != null)
//			conn.close();
//		if (sess != null)
//			sess.close();
//		conn = null;
//		sess = null;
//	}
//}
