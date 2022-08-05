package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


//Arrange, Act, Assert
//given_when_then, e.g. givenDoubleMaxValueAsRadius_whenCalculateArea_thenReturnAreaAsInfinity()

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

/* tests for checkSocketStatus:
 * happy path
 * if socket is null
 * if socket is not connected
 * if socket is closed
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

class AppTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
