//Author: Alexander Dolk

package com.dolk.EncryptedChatClientTCP_IP_Java_Maven;

import static org.junit.jupiter.api.Assertions.*;
import java.net.InetAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextInputControlMatchers;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class AppTest {
	
	App app;
	
    @Start
    private void start(Stage stage) {
    	app = new App();
    	app.start(stage);
    }
	
	@Test
	void connectButton_ClientBecomesConnected(FxRobot robot) throws Exception {
		Server server = new Server();
		
		Thread t = new Thread() {
			public void run() {
				server.runServer();
			}
		};
		t.start();
		
		robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write("4848");
		robot.clickOn("#usernameField").write("TestUser");
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		robot.clickOn("#connectButton");
				
		assertTrue(app.checkSocketStatus());
		
		server.closeServerSocket();
		server.stopServer();
		app.stop();
	}
    
	@Test
	void connectButton_SocketDoesNotBecomeConnected(FxRobot robot) throws Exception {
		
		robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write("4848");
		robot.clickOn("#usernameField").write("TestUser");
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		robot.clickOn("#connectButton");
		
		FxAssert.verifyThat("#connect", isEnabled());
		robot.clickOn("#connect");
		assertFalse(app.checkSocketStatus());
		
		app.stop();
	}
    
	@Test
	void connectButton_SocketAlreadyConnected(FxRobot robot) throws Exception {
		Server server = new Server();
		
		Thread t = new Thread() {
			public void run() {
				server.runServer();
			}
		};
		t.start();
		
		robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write("4848");
		robot.clickOn("#usernameField").write("TestUser");
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		robot.clickOn("#connectButton");
		robot.clickOn("#connectButton");
		
		FxAssert.verifyThat("#connect", isEnabled());
		robot.clickOn("#connect");
		assertTrue(app.checkSocketStatus());
		
		server.closeServerSocket();
		server.stopServer();
		app.stop();
	}
	
	@Test
    void messageButton_MessageIsSent(FxRobot robot) throws Exception {
		
	Server server = new Server();
		
	Thread t = new Thread() {
		public void run() {
			server.runServer();
		}
	};
	t.start();
	
	robot.clickOn("#hostField").write("localhost");
	robot.clickOn("#portField").write("4848");
	robot.clickOn("#usernameField").write("TestUser");
	robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
	robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
	robot.clickOn("#messageArea").write("Test message");
	robot.clickOn("#connectButton");
	robot.clickOn("#messageButton");
	
	String messageAreaText = app.getServerArea().getText();
	String[] lines = messageAreaText.split("\n");
	String lastLine = lines[lines.length -1];
	assertEquals(lastLine, "[TestUser]: Test message");
	assertEquals(app.getMessageArea().getText(), "");
	
	server.closeServerSocket();
	server.stopServer();
	app.stop();
	}
	
	@Test
	void messageButton_CheckAllDetailsIsFalseMessageNotSent(FxRobot robot) throws Exception {
		Server server = new Server();
		
		Thread t = new Thread() {
			public void run() {
				server.runServer();
			}
		};
		t.start();
		
		robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write("4848");
		robot.clickOn("#usernameField").write("TestUser");
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");  
		robot.clickOn("#connectButton");
		robot.clickOn("#messageButton");
		
		FxAssert.verifyThat("#checkAllDetails", isEnabled());
		robot.clickOn("#checkAllDetails");
		
		server.closeServerSocket();
		server.stopServer();
		app.stop();
	}
    
    @Test
	void disconnectButton_ClientAlreadyDisconnected(FxRobot robot) throws Exception {
		robot.clickOn("#disconnectButton");
		FxAssert.verifyThat("#disconnect", isEnabled());
		robot.clickOn("#disconnect");
		app.stop();
	}
	
	
	
	@Test
	void receivingTask_ClientReceivesMessage(FxRobot robot) throws Exception {		
		Server server = new Server();
		
		Thread t = new Thread() {
			public void run() {
				server.runServer();
			}
		};
		t.start();
		
		robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write("4848");
		robot.clickOn("#usernameField").write("TestUser");
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		robot.clickOn("#messageArea").write("Test message");
		robot.clickOn("#connectButton");
		robot.clickOn("#messageButton");
		
		String messageAreaText = app.getServerArea().getText();
		String[] lines = messageAreaText.split("\n");
		String lastLine = lines[lines.length -1];
		assertEquals(lastLine, "[TestUser]: Test message");
		
		server.closeServerSocket();
		server.stopServer();
		app.stop();
	}
	
	@Test
	void checkConnectionDetails_AllDetailsPresent_True(FxRobot robot) throws Exception {
		robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write("4848");
		FxAssert.verifyThat("#hostField", TextInputControlMatchers.hasText("localhost"));
		FxAssert.verifyThat("#portField", TextInputControlMatchers.hasText("4848"));
		assertTrue(app.checkConnectionDetails());
		app.stop();
	}
    
	@Test
	void checkConnectionDetails_hostFieldIsEmpty_False(FxRobot robot) throws Exception {
		robot.clickOn("#hostField").write(" ");
		robot.clickOn("#portField").write("4848");
		FxAssert.verifyThat("#hostField", TextInputControlMatchers.hasText(" "));
		FxAssert.verifyThat("#portField", TextInputControlMatchers.hasText("4848"));
		assertFalse(app.checkConnectionDetails());
		app.stop();
	}

	@Test
	void checkConnectionDetails_portFieldIsEmpty_False(FxRobot robot) throws Exception {
		robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write(" ");
		FxAssert.verifyThat("#hostField", TextInputControlMatchers.hasText("localhost"));
		FxAssert.verifyThat("#portField", TextInputControlMatchers.hasText(" "));
		assertFalse(app.checkConnectionDetails());
		app.stop();
	}
	
	@Test
	void checkUsernameDetails_UsernamePresent_True(FxRobot robot) throws Exception {
		robot.clickOn("#usernameField").write("John Doe");
		FxAssert.verifyThat("#usernameField", TextInputControlMatchers.hasText("John Doe"));
		assertTrue(app.checkUsernameDetails());
		app.stop();
	}
	
	@Test
	void checkUsernameDetails_usernameFieldIsEmpty_False(FxRobot robot) throws Exception {
		robot.clickOn("#usernameField").write(" ");
		FxAssert.verifyThat("#usernameField", TextInputControlMatchers.hasText(" "));
		assertFalse(app.checkUsernameDetails());
		app.stop();
	}
	
	@Test
	void checkPasswordDetails_BothPasswordsExist_True(FxRobot robot) throws Exception {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU548"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2k"));
		assertTrue(app.checkPasswordDetails());
		app.stop();
	}
    
	@Test
	void checkPasswordDetails_firstPasswordFieldIsEmpty_False(FxRobot robot)throws Exception {
		robot.clickOn("#firstPasswordField").write(" ");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText(" "));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2k"));
		assertFalse(app.checkPasswordDetails());
		app.stop();
	}
    
	@Test
	void checkPasswordDetails_secondPasswordFieldIsEmpty_False(FxRobot robot) throws Exception {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write(" ");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU548"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText(" "));
		assertFalse(app.checkPasswordDetails());
		app.stop();
	}
    
	
	@Test
	void checkPasswordBits_BothPasswordsAre16Bytes_True(FxRobot robot) throws Exception {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU548"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2k"));
		assertTrue(app.checkPasswordBits());
		app.stop();
	}
    
	@Test
	void checkPasswordBits_firstPasswordFieldNot16Bytes_False(FxRobot robot) throws Exception {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU54");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU54"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2k"));
		assertFalse(app.checkPasswordBits());
		app.stop();
	}
	
	@Test
	void checkPasswordBits_secondPasswordFieldNot16Bytes_False(FxRobot robot) throws Exception {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU548"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2"));
		assertFalse(app.checkPasswordBits());
		app.stop();
	}
	
	@Test
	void checkMessageDetails_MessageAreaHasText_True(FxRobot robot) throws Exception {	
		robot.clickOn("#messageArea").write("A new message");
		FxAssert.verifyThat("#messageArea", TextInputControlMatchers.hasText("A new message"));
		assertTrue(app.checkMessageDetails());
		app.stop();
	}
    
	@Test
	void checkMessageDetails_MessageAreaGetTextIsEmpty_False(FxRobot robot) throws Exception {
		robot.clickOn("#messageArea").write(" ");
		FxAssert.verifyThat("#messageArea", TextInputControlMatchers.hasText(" "));
		assertFalse(app.checkMessageDetails());
		app.stop();
	}
	
	@Test
    void checkSocketStatus_SocketIsConnected_True() throws Exception {
    	InetAddress host = InetAddress.getByName("localhost");
    	int port = 4848;
		
		Server server = new Server();
		Thread t = new Thread() {
			public void run() {
				server.runServer();
			}
		};
		t.start();
		
    	app.setSocket(host, port);
    	assertTrue(app.checkSocketStatus());

    	server.closeServerSocket();
    	server.stopServer();
    	app.stop();
    }
    
    @Test
    void checkSocketStatus_SocketIsNull_False() throws Exception {
    	InetAddress host = InetAddress.getByName("localhost");
    	App app = new App();
    	assertFalse(app.checkSocketStatus());
    	app.stop();
    }
    
    @Test
    void checkSocketStatus_SocketIsNotConnected_False() throws Exception {
    	InetAddress host = InetAddress.getByName("localhost");
    	App app = new App();
    	int port = 4848;
    	
    	app.setSocket(host, port);
    	
    	assertFalse(app.checkSocketStatus());
    	app.stop();
    }
    
    @Test
    void checkSocketStatus_SocketIsClosed_False() throws Exception {
    	InetAddress host = InetAddress.getByName("localhost");
    	int port = 4848;
    	App app = new App();
    	
		Server server = new Server();
		Thread t = new Thread() {
			public void run() {
				server.runServer();
			}
		};
		t.start();

    	app.setSocket(host, port);
    	app.closeSocket();

    	assertFalse(app.checkSocketStatus());

    	server.closeServerSocket();
    	server.stopServer();
    	app.stop();
    }
    
    @Test
    void checkAllDetails_AllDetailsAreFilled_True(FxRobot robot) throws Exception {
    	robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write("4848");
		FxAssert.verifyThat("#hostField", TextInputControlMatchers.hasText("localhost"));
		FxAssert.verifyThat("#portField", TextInputControlMatchers.hasText("4848"));
		robot.clickOn("#usernameField").write("John Doe");
		FxAssert.verifyThat("#usernameField", TextInputControlMatchers.hasText("John Doe"));
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU548"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2k"));
		robot.clickOn("#messageArea").write("A new message");
		FxAssert.verifyThat("#messageArea", TextInputControlMatchers.hasText("A new message"));
		
    	InetAddress host = InetAddress.getByName("localhost");
    	int port = 4848;
		
		Server server = new Server();
		Thread t = new Thread() {
			public void run() {
				server.runServer();
			}
		};
		t.start();
		
    	app.setSocket(host, port);
    	
    	assertTrue(app.checkAllDetails());
    	
    	server.closeServerSocket();
    	server.stopServer();
    	app.stop();
    }
        
}
