package com.dolk.EncryptedChatClientTCP_IP_Java_Maven;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//NEXT: https://medium.com/information-and-technology/test-driven-development-in-javafx-with-testfx-66a84cd561e0

//Arrange, Act, Assert
//MethodName_StateUnderTest_ExpectedBehavior
//example: isAdult_AgeLessThan18_False

/* Resources: 
 * https://www.vogella.com/tutorials/JUnit/article.html
 * https://www.vogella.com/tutorials/EclipseMaven/article.html#exercise-converting-a-java-project-create-with-eclipse-to-maven
 * https://www.baeldung.com/java-unit-testing-best-practices
 * https://github.com/TestFX/TestFX
 * https://jenkov.com/tutorials/java-unit-testing/io-testing.html
 * https://github.com/TestFX/TestFX
 * https://github.com/TestFX/TestFX/issues/638
 * */

@ExtendWith(ApplicationExtension.class)
class AppTest {
	
	App app;
		
	//Will be called with {@code @Before} semantics, i. e. before each test method.
	//@param stage - Will be injected by the test runner.
    @Start
    private void start(Stage stage) {
    	app = new App();
    	app.start(stage);
    }
	
	
//    private Button button;

//    /**
//     * Will be called with {@code @Before} semantics, i. e. before each test method.
//     *
//     * @param stage - Will be injected by the test runner.
//     * @throws UnsupportedEncodingException 
//     */
//    @Start
//    private void start(Stage stage) {
//        button = new Button("click me!");
//        button.setId("myButton");
//        button.setOnAction(actionEvent -> button.setText("clicked!"));
//        stage.setScene(new Scene(new StackPane(button), 100, 100));
//        stage.show();
//    }
    
//    
//  /**
//  * @param robot - Will be injected by the test runner.
//  */
//    @Test
//    void test(FxRobot robot) {
//    	robot.clickOn("#usernameField").write("Alex");
//    	
//    	FxAssert.verifyThat("#usernameField", TextInputControlMatchers.hasText("Alex"));
//    }
    

//
//    /**
//     * @param robot - Will be injected by the test runner.
//     */
//    @Test
//    void should_contain_button_with_text(FxRobot robot) {
//        FxAssert.verifyThat(button, LabeledMatchers.hasText("click me!"));
//        // or (lookup by css id):
//        FxAssert.verifyThat("#myButton", LabeledMatchers.hasText("click me!"));
//        // or (lookup by css class):
//        FxAssert.verifyThat(".button", LabeledMatchers.hasText("click me!"));
//    }
//
//    /**
//     * @param robot - Will be injected by the test runner.
//     */
//    @Test
//    void when_button_is_clicked_text_changes(FxRobot robot) {
//        // when:
//        robot.clickOn(".button");
//
//        // then:
//        FxAssert.verifyThat(button, LabeledMatchers.hasText("clicked!"));
//        // or (lookup by css id):
//        FxAssert.verifyThat("#myButton", LabeledMatchers.hasText("clicked!"));
//        // or (lookup by css class):
//        FxAssert.verifyThat(".button", LabeledMatchers.hasText("clicked!"));
//    }
	
	//@Test
	//void connectButton_ClientBecomesConnected_x()
	//@Test
	//void connectButton_SocketIsNull_x()
	//@Test
	//void connectButton_SocketDoesNotBecomeConnected_x()
	//@Test
	//void connectButton_SocketRemainsClosed_x()
	//@Test
	//void connectButton_checkConnectionDetailsReturnsFalse_x()
	//@Test
	//void connectButton_SocketAlreadyConnected_x()
	
	//@Test
	//void messageButton_MessageIsSent_x()
	//@Test
	//void messageButton_CheckAllDetailsIsFalseMessageNotSent_x()
	
	void disconnectButton_ClientAlreadyDisconnected(FxRobot robot) {
		robot.clickOn("#disconnectButton");
		FxAssert.verifyThat("#disconnect", isEnabled());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	void receivingTask_ClientReceivesMessage(FxRobot robot) throws Exception {		
		Thread server = new Thread() {
			public void run() {
				Server.main();
			}
		};
		server.start();
		
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
		
		server.stop();
		app.stop();
	}
	
	@Test
	void checkConnectionDetails_AllDetailsPresent_True(FxRobot robot) {
		robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write("4848");
		FxAssert.verifyThat("#hostField", TextInputControlMatchers.hasText("localhost"));
		FxAssert.verifyThat("#portField", TextInputControlMatchers.hasText("4848"));
		assertTrue(app.checkConnectionDetails());
	}
    
	@Test
	void checkConnectionDetails_hostFieldIsEmpty_False(FxRobot robot) {
		robot.clickOn("#hostField").write(" ");
		robot.clickOn("#portField").write("4848");
		FxAssert.verifyThat("#hostField", TextInputControlMatchers.hasText(" "));
		FxAssert.verifyThat("#portField", TextInputControlMatchers.hasText("4848"));
		assertFalse(app.checkConnectionDetails());
	}

	@Test
	void checkConnectionDetails_portFieldIsEmpty_False(FxRobot robot) {
		robot.clickOn("#hostField").write("localhost");
		robot.clickOn("#portField").write(" ");
		FxAssert.verifyThat("#hostField", TextInputControlMatchers.hasText("localhost"));
		FxAssert.verifyThat("#portField", TextInputControlMatchers.hasText(" "));
		assertFalse(app.checkConnectionDetails());
	}
	
	@Test
	void checkUsernameDetails_UsernamePresent_True(FxRobot robot) {
		robot.clickOn("#usernameField").write("John Doe");
		FxAssert.verifyThat("#usernameField", TextInputControlMatchers.hasText("John Doe"));
		assertTrue(app.checkUsernameDetails());
	}
	
	@Test
	void checkUsernameDetails_usernameFieldIsEmpty_False(FxRobot robot) {
		robot.clickOn("#usernameField").write(" ");
		FxAssert.verifyThat("#usernameField", TextInputControlMatchers.hasText(" "));
		assertFalse(app.checkUsernameDetails());
	}
	
	@Test
	void checkPasswordDetails_BothPasswordsExist_True(FxRobot robot) {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU548"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2k"));
		assertTrue(app.checkPasswordDetails());
	}
    
	@Test
	void checkPasswordDetails_firstPasswordFieldIsEmpty_False(FxRobot robot){
		robot.clickOn("#firstPasswordField").write(" ");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText(" "));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2k"));
		assertFalse(app.checkPasswordDetails());
	}
    
	@Test
	void checkPasswordDetails_secondPasswordFieldIsEmpty_False(FxRobot robot) {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write(" ");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU548"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText(" "));
		assertFalse(app.checkPasswordDetails());
	}
    
	
	@Test
	void checkPasswordBits_BothPasswordsAre16Bytes_True(FxRobot robot) throws UnsupportedEncodingException {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU548"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2k"));
		assertTrue(app.checkPasswordBits());
	}
    
	@Test
	void checkPasswordBits_firstPasswordFieldNot16Bytes_False(FxRobot robot) throws UnsupportedEncodingException {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU54");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2k");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU54"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2k"));
		assertFalse(app.checkPasswordBits());
	}
	
	@Test
	void checkPasswordBits_secondPasswordFieldNot16Bytes_False(FxRobot robot) throws UnsupportedEncodingException {
		robot.clickOn("#firstPasswordField").write("0JZGv0hwh7PiU548");
		robot.clickOn("#secondPasswordField").write("95FdIBeP46LyIo2");
		FxAssert.verifyThat("#firstPasswordField",TextInputControlMatchers.hasText("0JZGv0hwh7PiU548"));
		FxAssert.verifyThat("#secondPasswordField",TextInputControlMatchers.hasText("95FdIBeP46LyIo2"));
		assertFalse(app.checkPasswordBits());
	}
	
	@Test
	void checkMessageDetails_MessageAreaHasText_True(FxRobot robot) {	
		robot.clickOn("#messageArea").write("A new message");
		FxAssert.verifyThat("#messageArea", TextInputControlMatchers.hasText("A new message"));
		assertTrue(app.checkMessageDetails());
	}
    
	@Test
	void checkMessageDetails_MessageAreaGetTextIsEmpty_False(FxRobot robot) {
		robot.clickOn("#messageArea").write(" ");
		FxAssert.verifyThat("#messageArea", TextInputControlMatchers.hasText(" "));
		assertFalse(app.checkMessageDetails());
	}
	
	@Test
    void checkSocketStatus_SocketIsConnected_True() throws IOException {
    	//Arrange
    	InetAddress host = InetAddress.getByName("localhost");
    	App app = new App();
    	ServerSocketMock scs = new ServerSocketMock();
    	int port = 4001;
    	//Act
    	scs.start(port);
    	app.setSocket(host, port);
    	//Assert
    	assertTrue(app.checkSocketStatus());
    	//Cleanup
    	app.closeSocket();
    	scs.stop();
    }
    
    @Test
    void checkSocketStatus_SocketIsNull_False() throws UnknownHostException {
    	//Arrange
    	InetAddress host = InetAddress.getByName("localhost");
    	App app = new App();
    	//Assert
    	assertFalse(app.checkSocketStatus());
    }
    
    @Test
    void checkSocketStatus_SocketIsNotConnected_False() throws UnknownHostException {
    	//Arrange
    	InetAddress host = InetAddress.getByName("localhost");
    	App app = new App();
    	int port = 4003;
    	//Act
    	app.setSocket(host, port);
    	//Assert
    	assertFalse(app.checkSocketStatus());
    }
    
    @Test
    void checkSocketStatus_SocketIsClosed_False() throws UnknownHostException {
    	//Arrange
    	InetAddress host = InetAddress.getByName("localhost");
    	App app = new App();
    	ServerSocketMock scs = new ServerSocketMock();
    	int port = 4004;
    	//Act
    	scs.start(port);
    	app.setSocket(host, port);
    	app.closeSocket();
    	//Assert
    	assertFalse(app.checkSocketStatus());
    	//Cleanup
    	scs.stop();
    } //This test throws generates an exception via the method setSocket in the class App. 
    
    @Test
    void checkAllDetails_AllDetailsAreFilled_True(FxRobot robot) throws UnsupportedEncodingException, UnknownHostException {
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
    	ServerSocketMock scs = new ServerSocketMock();
    	int port = 4005;
    	scs.start(port);
    	app.setSocket(host, port);
    	
    	assertTrue(app.checkAllDetails());
    	
    	app.closeSocket();
    	scs.stop();
    }
        
}
