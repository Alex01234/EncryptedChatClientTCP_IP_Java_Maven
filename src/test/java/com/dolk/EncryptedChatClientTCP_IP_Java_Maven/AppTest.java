package com.dolk.EncryptedChatClientTCP_IP_Java_Maven;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


//Arrange, Act, Assert
//MethodName_StateUnderTest_ExpectedBehavior
//example: isAdult_AgeLessThan18_False

/* TODO:
 * - Create tests for "check..." methods
 * - startReceiver
 * - recevingTask
 * - buttons
 * */

/* Resources: 
 * https://www.vogella.com/tutorials/JUnit/article.html
 * https://www.vogella.com/tutorials/EclipseMaven/article.html#exercise-converting-a-java-project-create-with-eclipse-to-maven
 * https://www.baeldung.com/java-unit-testing-best-practices
 * https://github.com/TestFX/TestFX
 * https://jenkov.com/tutorials/java-unit-testing/io-testing.html
 * https://github.com/TestFX/TestFX
 * https://github.com/TestFX/TestFX/issues/638
 * */

/*
 * tests for connectButton.SetOnAction:
 * happy path
 * if socket is null
 * if socket is not connected
 * if socket is closed
 * if checkConnectionDetails returns false
 * if already connected
 * test connection to the server, or should it be tested in via the startReceiver method?
*/

/*
 * tests for messageButton.setOnAction:
 * happy path
 * if checkAllDetails returns false
 * test to writeObject
 * */

/*
 * tests for disconnectButton.setOnAction:
 * happy path
 * if socket is null
 * if socket is not connected
 * if socket is closed
 * 
 * */

/*
 * tests for startReceiver:
 * test that the receiving thread starts
 * */

/*
 * tests for receivingTask:
 * happy path
 * socket not created
 * */

/* tests for checkConnectionDetails:
 * happy path
 * if hostField is null
 * if hostField is empty
 * if portField is empty
 * if portField is null
 * */

/* tests for checkUsernameDetails:
 * happy path
 * if usernameField is null
 * if usernameField is empty
 * */

/* tests for checkPasswordDetails:
 * happy path
 * if firstPasswordField is null
 * if firstPasswordFiled is empty
 * if secondPasswordField is null
 * if secondPasswordField is empty
 * */

/* tests for checkPasswordBits
 * happy path
 * if password1Bytes is not 16 
 * if password2Bytes is not 16
 * if control1 is false
 * if control2 is false
 * */

/* tests for checkMessageDetails
 * happy path
 * if messageArea is null
 * if messageAres is empty
 * */

/* tests for checkAllDetails:
 * happy path
 * if connectionCheck returns false
 * if socketCheck returns false
 * if usernameCheck returns false
 * if passwordCheck returns false
 * if passwordBitCheck returns false
 * if messageCheck returns false
 * */

@ExtendWith(ApplicationExtension.class)
class AppTest {

    private Button button;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    private void start(Stage stage) {
        button = new Button("click me!");
        button.setId("myButton");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void should_contain_button_with_text(FxRobot robot) {
        FxAssert.verifyThat(button, LabeledMatchers.hasText("click me!"));
        // or (lookup by css id):
        FxAssert.verifyThat("#myButton", LabeledMatchers.hasText("click me!"));
        // or (lookup by css class):
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("click me!"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void when_button_is_clicked_text_changes(FxRobot robot) {
        // when:
        robot.clickOn(".button");

        // then:
        FxAssert.verifyThat(button, LabeledMatchers.hasText("clicked!"));
        // or (lookup by css id):
        FxAssert.verifyThat("#myButton", LabeledMatchers.hasText("clicked!"));
        // or (lookup by css class):
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("clicked!"));
    }
    
    /*Tests for checkSocketStatus*/
    @Test
    void checkSocketStatus_SocketIsConnected_True() throws IOException {
    	//Arrange
    	InetAddress host = InetAddress.getByName("localhost");
    	int port = 4847;
    	App app = new App();
    	
    	//TODO: Create a server socket that accepts connections at port 4848
    	//TODO Start the server socket in a separate thread
    	ServerSocket serverSocket = new ServerSocket(port);
    	serverSocket.accept();
    	
    	//Act
    	app.setSocket(host, port);
    	
    	//Assert
    	assertTrue(app.checkSocketStatus());
    	
    	//Cleanup
    	app.closeSocket();
    	serverSocket.close();
    }
    
    //@Test
    //checkSocketStatus_SocketIsNull_False
    
    //@Test
    //checkSocketStatus_SocketIsNotConnected_False
    
    //@Test
    //checkSocketStatus_SocketIsClosed_False
    
}
