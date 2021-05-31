//package com.dt.module.om.term.websocket;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import com.dt.core.tool.util.ToolUtil;
//import com.dt.module.om.term.entity.Machine;
//
//@RequestMapping("/term")
//public class SshShellHandler extends TextWebSocketHandler {
//
//	private static Logger _log = LoggerFactory.getLogger(SshShellHandler.class);
//	public static final Map<String, List<WebSocketSession>> userSocketSessionMap;
//	private Machine currentMachine = null;
//	private SshClient sshClient = null;
//	private List<WebSocketSession> sessionList = null;
//
//	static {
//		userSocketSessionMap = new HashMap<String, List<WebSocketSession>>();
//	}
//
//	@Override
//	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//		String user_id = session.getAttributes().get("user_id") + "";
//		super.handleTextMessage(session, message);
//		try {
//			if (sshClient != null) {
//				// receive a close cmd ?
//				if (Arrays.equals("exit".getBytes(), message.asBytes())) {
//					if (sshClient != null) {
//						sshClient.disconnect();
//					}
//					session.close();
//					return;
//				}
//				log.info("user:" + user_id + " cmd: " + new String(message.asBytes(), "UTF-8"));
//				sshClient.write(new String(message.asBytes(), "UTF-8"));
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			session.sendMessage(new TextMessage("An error occured, websocket is closed."));
//			session.close();
//		}
//	}
//
//	@Override
//	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//		log.info("WebSocketSession Closed");
//		super.afterConnectionClosed(session, status);
//		if (sshClient != null) {
//			sshClient.disconnect();
//		}
//	}
//
//	@Override
//	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//		String user_id = session.getAttributes().get("user_id") + "";
//		currentMachine = (Machine) session.getAttributes().get("currentMachine");
//		super.afterConnectionEstablished(session);
//		log.info("user:" + user_id + " to connect " + currentMachine.getHostname());
//		if (ToolUtil.isEmpty(user_id)) {
//			session.sendMessage(new TextMessage("Can't find user_id in session.\r"));
//			return;
//		}
//		if (ToolUtil.isEmpty(currentMachine.getHostname())) {
//			session.sendMessage(new TextMessage("Can't find Hostname in session.\r"));
//			return;
//		}
//
//		// first close other shell
//		Iterator<Entry<String, List<WebSocketSession>>> it = userSocketSessionMap.entrySet().iterator();
//		while (it.hasNext()) {
//			Entry<String, List<WebSocketSession>> entry = it.next();
//			// close self other terminal connection.
//			if (entry.getKey().equals(user_id)) {
//				sessionList = entry.getValue();
//				for (int i = sessionList.size() - 1; i >= 0; i--) {
//					WebSocketSession wss = sessionList.get(i);
//					if (wss.isOpen()) {
//						wss.sendMessage(
//								new TextMessage("Another shell terminal is open, so this terminal changed to closed."));
//						wss.close();
//					}
//					sessionList.remove(i);
//				}
//			}
//		}
//		// update current in using machine
//		if (currentMachine != null) {
//			if (sessionList == null) {
//				sessionList = new ArrayList<>();
//			}
//			sessionList.add(session);
//			userSocketSessionMap.put(user_id, sessionList);
//			sshConnect(session, currentMachine);
//		}
//	}
//
//	private void sshConnect(WebSocketSession session, Machine machine) {
//		sshClient = new SshClient();
//		log.info("sshConnect");
//		try {
//			session.sendMessage(new TextMessage("Try to connect...\r"));
//			if (sshClient.connect(machine)) {
//				sshClient.startShellOutPutTask(session);
//			} else {
//				sshClient.disconnect();
//				session.sendMessage(
//						new TextMessage("Connect failed, please confirm the username or password try agin."));
//				session.close();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//}
