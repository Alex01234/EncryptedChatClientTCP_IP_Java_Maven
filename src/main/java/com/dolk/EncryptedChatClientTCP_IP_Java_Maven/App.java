//Author: Alexander Dolk

package com.dolk.EncryptedChatClientTCP_IP_Java_Maven;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
Main:
The class App extends the class Application from JavaFX.
The class is used to create a JavaFX application that works as a chat client, which connects to a server.
The chat client sends and receives SealedObject:s to/from the server.
In order to encrypt outgoing SealedObject:s and decrypt incoming ones, a Cipher is used.
The Cipher used is "AES/CBC/PKCS5PADDING", with a 128 bit IvParamaterSpec and a 128 bit SecretKeySpec.
The IvParameterSpec and the SecretKeySpec are submitted by the user when the application is running.
As such the submitted IvParameterSpec and the SecretKeySpec has to be the same for all the connected users in order to decrypt encrypted messages.
*/
public class App extends Application {

	private InetAddress host;
	private int port;
	private Socket socket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private String message;
	private Thread receivingThread;
	private final AtomicBoolean running = new AtomicBoolean(false);

	// Northern HBox
	private Label hostLabel;
	private TextField hostField;
	private Label portLabel;
	private TextField portField;
	private Button connectButton;
	private Button disconnectButton;
	// Center HBox
	private TextArea serverArea;
	// Right VBox
	private Label usernameLabel;
	private TextField usernameField;
	private Label firstPasswordLabel;
	private TextField firstPasswordField;
	private Label secondPasswordLabel;
	private TextField secondPasswordField;
	// Bottom Hbox
	private TextArea messageArea;
	private Button messageButton;

	private static String welcomeText = "Welcome to the Sealed Chat Client." + "\n"
			+ "In order to use the chat client, do the following steps:" + "\n"
			+ "1. Fill in the host and port of the chat server you wish to connect to and press the button 'Connect to server'."
			+ "\n" + "2. Fill in your username." + "\n"
			+ "3. Fill in the two passwords that will be used to encrypt and decrypt messages to the chat server. "
			+ "The keys must be 128 bits in length and the same for all connected users to the chat server." + "\n"
			+ "4. Send messages below." + "\n";

	private static String connectionDialogString = "Host and/or Port is not submitted. ";
	private static String socketDialogString = "The chat client is not connected to the server.";
	private static String usernameDialogString = "Username is not submitted. ";
	private static String passwordDialogString = "Passwords are not submitted. ";
	private static String passwordBitString = "Password 1 and/or Password 2 is not 128 bits in length. ";
	private static String messageDialogString = "Message is empty. ";

	/*
	 * start: The method start is called when the JavaFX application is launched. In
	 * the method start, a number of Labels, TextFields, Buttons and TextArea are
	 * created and added to Hbox:es and a VBox. Those HBox:es and the VBox are added
	 * to a BorderPane. A Scene is created, which shows the BorderPane.
	 * 
	 * Additionally, values for the property setOnAction are declared for the
	 * Buttons that exist in the application which determine the functionality of
	 * the application. Please see the comments below for more specific details,
	 * where the values for the property setOnAction are declared.
	 */
	@Override
	public void start(Stage stage) {
		try {
			BorderPane root = new BorderPane();

			// Northern HBox:
			HBox northBox = new HBox();
			northBox.setPadding(new Insets(15, 12, 15, 12));
			northBox.setSpacing(10);
			hostLabel = new Label("Host:");
			hostField = new TextField(); hostField.setId("hostField");
			portLabel = new Label("Port:");
			portField = new TextField(); portField.setId("portField");
			connectButton = new Button("Connect to server"); connectButton.setId("connectButton");
			disconnectButton = new Button("Disconnect from server"); disconnectButton.setId("disconnectButton");
			northBox.getChildren().addAll(hostLabel, hostField, portLabel, portField, connectButton, disconnectButton);

			// Center HBox:
			HBox centerBox = new HBox();
			centerBox.setPadding(new Insets(15, 12, 15, 12));
			centerBox.setSpacing(10);
			serverArea = new TextArea();
			serverArea.setWrapText(true);
			serverArea.setEditable(false);
			serverArea.setId("serverArea");
			centerBox.getChildren().addAll(serverArea);
			serverArea.appendText(welcomeText);

			// Right VBox:
			VBox rightBox = new VBox();
			rightBox.setPadding(new Insets(10));
			rightBox.setSpacing(8);
			usernameLabel = new Label("Username:");
			usernameField = new TextField(); usernameField.setId("usernameField");
			firstPasswordLabel = new Label("Password 1:");
			firstPasswordField = new TextField(); firstPasswordField.setId("firstPasswordField");
			secondPasswordLabel = new Label("Password 2:");
			secondPasswordField = new TextField(); secondPasswordField.setId("secondPasswordField");
			rightBox.getChildren().addAll(usernameLabel, usernameField, firstPasswordLabel, firstPasswordField,
					secondPasswordLabel, secondPasswordField);

			// Bottom HBox:
			HBox bottomBox = new HBox();
			bottomBox.setPadding(new Insets(10));
			bottomBox.setSpacing(8);
			messageArea = new TextArea(); messageArea.setId("messageArea");
			messageButton = new Button("Send message"); messageButton.setId("messageButton");
			bottomBox.getChildren().addAll(messageArea, messageButton);

			root.setTop(northBox);
			root.setCenter(centerBox);
			root.setRight(rightBox);
			root.setBottom(bottomBox);

			Scene scene = new Scene(root, 700, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			stage.setTitle("Sealed Chat Client");

			/*
			 * connectButton: Here the method setOnAction is defined for the Button
			 * connectButton. First, the method checkConnectionDetails is called. If the
			 * returning value is true, the following is done: The InetAddress host is set
			 * to the text that has been submitted to the TextField hostField, the int port
			 * is set to the text that has been submitted to the TextFiedld portField, the
			 * Socket socket is set to an instance of the class Socket with the parameters
			 * host and port, the ObjectOutputStream is set to an instance of the class
			 * ObjectOutputStream with the parameter socket.getOutputStream(), the method
			 * startReceiver is called and a text is appended to the TextArea serverArea
			 * that indicates that a connection has been made to the server with the host
			 * and port submitted by the user.
			 * 
			 * If the method checkConncetionDetails returns false, the method displayDialog
			 * is called with the parameter of the static String connectionDialogString.
			 * 
			 * TODO: Update comments
			 */
			connectButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {
						boolean alreadyConnected = false;
						if(socket != null) {
							if(socket.isConnected() && !socket.isClosed()){
								displayDialog("Client already connected to server.");
								alreadyConnected = true;
							}
						}
						if (checkConnectionDetails() && !alreadyConnected) {
							host = InetAddress.getByName(hostField.getText());
							port = Integer.parseInt(portField.getText());
							setSocket(host, port);
							socket.setKeepAlive(true);
							outStream = new ObjectOutputStream(socket.getOutputStream());
							startReceiver();
							serverArea.appendText("\n" + "Connected to server with host: " + host + " and port: " + port);
						} else if (!checkConnectionDetails()){
							displayDialog(connectionDialogString);
						}
					} catch (ConnectException ex) {
						System.out.println("\n" + "An Exception occured connecting to the server: " + ex);
						displayDialog("The chat client failed to connect to the server: " + ex.toString() + ","
								+ " the host and/or port might be incorrect, or the server might not be on.");
						ex.printStackTrace();
					} catch (Exception ex) {
						System.out.println("\n" + "An Exception occured connecting to the server: " + ex);
						ex.printStackTrace();
						displayDialog("The chat client failed to connect to the server: " + ex.toString());
					}
				}
			});// connectButton

			/*
			 * messageButton: Here the method setOnAction is defined for the Button
			 * messageButton. First, the method checkAllDetails is called. If the returning
			 * value is true, the following is done: The String message is set to the text
			 * submitted to the TextField usernameField and the text submitted to the
			 * TextArea messageArea, the TextArea messageArea is then cleared of text.
			 * 
			 * An instance of the class Serializable is created using the String message, an
			 * instance of the class IvParameterSpec is created using the text submitted to
			 * the TextField firstPasswordField, an instance of the class SecretKeySpec is
			 * created using the text submitted to the TextField secondPasswordField, an
			 * instance of the class Cipher is created with the transformation
			 * "AES/CBC/PKCS5PADDING", the Cipher is initialized in encryption mode using
			 * the IvParameterSpec and SecretKeySpec.
			 * 
			 * A SealedObject is created using the instance of the class Serializable (the
			 * message) and the Cipher and the SealedObject is then written to the
			 * ObjectOutputStream outStream.
			 */
			messageButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {
						if (checkAllDetails()) {
							message = "[" + usernameField.getText() + "]" + ": " + messageArea.getText();
							messageArea.setText("");

							Serializable object = message;

							IvParameterSpec iv = new IvParameterSpec(firstPasswordField.getText().getBytes("UTF-8"));
							SecretKeySpec skeySpec = new SecretKeySpec(secondPasswordField.getText().getBytes("UTF-8"),
									"AES");
							Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
							cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

							SealedObject sealedObject = new SealedObject(object, cipher);
							outStream.writeObject(sealedObject);
						}
					} catch (Exception ex) {
						displayDialog("\n" + "An Exception occured sending message: " + ex + ". Try to disconnect and reconnect to the server.");
						System.out.println("\n" + "An Exception occured sending message: " + ex);
						ex.printStackTrace();
					}
				}
			});// messageButton

			/*
			 * disconnectButton: TODO: Comment this
			 */
			disconnectButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
				@SuppressWarnings("deprecation")
				@Override
				public void handle(ActionEvent e) {
					try {
						if (socket != null) {
							if (socket.isConnected() && !socket.isClosed()) {
								receivingThread.stop();
								socket.close();
								serverArea.appendText(
										"\n" + "Disconnected from server with host: " + host + " and port: " + port);
								running.set(false);
							} else {
								displayDialog("The chat client is not connected to the server.");
							}
						} else {
							displayDialog("The chat client is not connected to the server.");
						}
					} catch (Exception ex) {
						System.out.println("\n" + "An Exception occured disconnecting from the server: " + ex);
						ex.printStackTrace();
					}
				}
			});

		} catch (Exception ex) {
			System.out.println("\n" + "An Exception occured: " + ex);
			ex.printStackTrace();
		} // try-catch for whole start-method

	}// start

	/*
	 * startReceiver: The method startReceiver is used to create a thread and call
	 * the method start on that thread. The threads run-method then calls the method
	 * receivingTask.
	 */
	private void startReceiver() {
		try {
			Runnable runnable = new Runnable() {
				public void run() {
					receivingTask();

				}
			};

			receivingThread = new Thread(runnable);
			receivingThread.setDaemon(true);
			receivingThread.start();
		} catch (Exception ex) {
			displayDialog("An Exception occured trying to start the thread to receive messages: " + ex);
			System.out.println("An Exception occured trying to start the thread to receive messages: " + ex);
			ex.printStackTrace();
		}
	}// startReceiver

	/*
	 * receivingTask: The method receivingTask is used to run an infinite
	 * while-loop, which listens to the Socket socket. An ObjectInputStream is
	 * created from the Socket socket and from the ObjectInputStream, a SealedObject
	 * is read. A Cipher is created using the passwords that have been submitted to
	 * the TextFields firstPasswordField and secondPasswordField. Using the Cipher,
	 * the Object (which is a String) is retrieved and decrypted from the
	 * SealedObject. The decrypted String is then appended to the TextArea
	 * serverArea.
	 * 
	 * TODO: Update comments
	 */
	private void receivingTask() {
		try {
			running.set(true);
			while (running.get()) {

					inStream = new ObjectInputStream(socket.getInputStream());
					SealedObject sealedObject = (SealedObject) inStream.readObject();

					IvParameterSpec iv = new IvParameterSpec(firstPasswordField.getText().getBytes("UTF-8"));
					SecretKeySpec skeySpec = new SecretKeySpec(secondPasswordField.getText().getBytes("UTF-8"), "AES");
					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
					cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

					String decryptedText = (String) sealedObject.getObject(cipher);
					serverArea.appendText("\n" + decryptedText);

			}
		} catch (SocketException ex) {
			displayDialog(
					"A SocketException occured, as such the connection to the server was lost. Please try to reconnect.");
			running.set(false);
		} catch (Exception ex) {
			displayDialog(
					"An Exception occured in the thread receiving messages, as such the connection to the server was lost. Please try to reconnect.");
			running.set(false);
			ex.printStackTrace();
		}
	}// receivingTask

	/*
	 * checkConnectionDetails: The method checkConnectionDetails is used to check if
	 * the TextFields hostField and portField contains text or are empty. If either
	 * hostField or portField is empty, the color of the text on the Button
	 * connectButton is set to red and the method returns false. If both hostField
	 * and portField contains text, the color of the text on connectButton is set to
	 * green and the method returns true.
	 */
	boolean checkConnectionDetails() {
		if (hostField.getText() == null || hostField.getText().trim().isEmpty() || portField.getText() == null
				|| portField.getText().trim().isEmpty()) {
			connectButton.setStyle("-fx-text-fill: #ff0000"); // changes color of text in button to red
			return false;
		} else {
			connectButton.setStyle("-fx-text-fill: #009d00"); // changes color of text in button to green
			return true;
		}
	}// checkConnectionDetails

	/*
	 * checkUsernameDetails: The method checkUsernameDetails is used to check if the
	 * TextField usernameField contains text or is empty. If usernameField is empty,
	 * the method returns false. Otherwise, the method returns true.
	 */
	boolean checkUsernameDetails() {
		if (usernameField.getText() == null || usernameField.getText().trim().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}// checkUsernameDetails

	/*
	 * checkPasswordDetails: The method checkPasswordDetails is used to check if the
	 * TextFields firstPasswordField and secondPasswordField contains text or are
	 * empty. If firstPasswordField or secondPasswordField is empty, the method
	 * returns false. If both firstPasswordField and secondPasswordField contains
	 * text, the method returns true.
	 */
	boolean checkPasswordDetails() {
		if (firstPasswordField.getText() == null || firstPasswordField.getText().trim().isEmpty()
				|| secondPasswordField.getText() == null || secondPasswordField.getText().trim().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}// checkPasswordDetails

	/*
	 * checkPasswordBits: The method checkPasswordBits is used to check that the
	 * length of submitted texts in the TextFields firstPasswordField and
	 * secondPasswordField is 16 bytes in length, which is equivalent to 128 bits.
	 * If the submitted text to either firstPasswordField or secondPasswordField is
	 * not 16 bytes in length, the method returns false. If both of the submitted
	 * texts to firstPasswordField and secondPasswordField are 16 bytes in length,
	 * the method returns true.
	 */
	boolean checkPasswordBits() throws UnsupportedEncodingException {
		boolean control1 = true;
		boolean control2 = true;

		final byte[] password1Bytes = firstPasswordField.getText().getBytes("UTF-8");
		final byte[] password2Bytes = secondPasswordField.getText().getBytes("UTF-8");

		if (password1Bytes.length != 16) {
			control1 = false;
		}
		if (password2Bytes.length != 16) {
			control2 = false;
		}

		if (!control1 || !control2) {
			return false;
		} else {
			return true;
		}

	}// checkPasswordBits

	/*
	 * checkMessageDetails: The method checkMessageDetails is used to check if the
	 * TextArea messageArea is empty or if it contains text. If messageArea is
	 * empty, the color of text on the Button messageButton is changed to red and
	 * the method returns false. If messageArea contains text, the color of the text
	 * on messageButton is changed to green and the method returns true.
	 */
	boolean checkMessageDetails() {
		if (messageArea.getText() == null || messageArea.getText().trim().isEmpty()) {
			messageButton.setStyle("-fx-text-fill: #ff0000"); // changes color of text in button to red
			return false;
		} else {
			messageButton.setStyle("-fx-text-fill: #009d00"); // changes color of text in button to green
			return true;
		}
	}// checkMessageDetails

	/*
	 * checkSocketStatus: TODO: Comment this
	 */
	boolean checkSocketStatus() {
		boolean socketConnected = false;
		if (socket != null) {
			if (socket.isConnected() && !socket.isClosed()) {
				socketConnected = true;
			}
		}
		return socketConnected;
	}

	/*
	 * checkAllDetails: The method checkAllDetails is used to call a number of other
	 * utility methods, which are: the methods checkConnectionDetails,
	 * checkUsernameDetails, checkPasswordDetails, checkPasswordBits and
	 * checkMessageDetails. The returning boolean value of the above methods are
	 * saved locally and if a method returns false, a corresponding static String is
	 * appended to the local String dialog. If one of the methods above returns
	 * false, the method displayDialog is called with the parameter of the String
	 * dialog and the method checkAllDetails returns false. Otherwise, the method
	 * checkAllDetails returns true.
	 * 
	 * TODO: Update comments
	 */
	private boolean checkAllDetails() throws UnsupportedEncodingException {
		boolean connectionCheck = checkConnectionDetails();
		boolean socketCheck = checkSocketStatus();
		boolean usernameCheck = checkUsernameDetails();
		boolean passwordCheck = checkPasswordDetails();
		boolean passwordBitCheck = checkPasswordBits();
		boolean messageCheck = checkMessageDetails();

		String dialog = "";
		if (!connectionCheck) {
			dialog = dialog + connectionDialogString;
		}
		if (!socketCheck) {
			dialog = dialog + socketDialogString;
		}
		if (!usernameCheck) {
			dialog = dialog + usernameDialogString;
		}
		if (!passwordCheck) {
			dialog = dialog + passwordDialogString;
		}
		if (!passwordBitCheck) {
			dialog = dialog + passwordBitString;
		}
		if (!messageCheck) {
			dialog = dialog + messageDialogString;
		}
		if (!connectionCheck || !socketCheck || !usernameCheck || !passwordCheck || !passwordBitCheck
				|| !messageCheck) {
			displayDialog(dialog);
			return false;
		} else {
			return true;
		}

	}// checkAllDetails

	/*
	 * displayDialog: The method displayDialog is used to show an Alert (which is a
	 * subclass of the class Dialog), to the user. The text displayed to the user is
	 * the parameter (of the type String) which is passed to the method.
	 */
	private void displayDialog(String textToDisplay) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("An Error occured.");
		alert.setHeaderText(null);
		alert.setContentText(textToDisplay);
		alert.setResizable(true);
		alert.showAndWait();
	}// displayDialog
	
	void setSocket(InetAddress host, int port) {
		try {
			socket = new Socket(host, port);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	void closeSocket() {
		try {
			socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * main: The method main is used to call the method launch, which launches the
	 * JavaFX application.
	 */
	public static void main(String[] args) {
		launch(args);
	}// main

}// App
