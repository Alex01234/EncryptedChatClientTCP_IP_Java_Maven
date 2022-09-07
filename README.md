# EncryptedChatClientTCP_IP_Java_Maven
## Description
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
![GIF showing demo of the chat client](https://github.com/Alex01234/EncryptedChatClientTCP_IP_Java_Maven/blob/master/EncryptedChatClient_demo.gif?)

## Testing and test coverage 
![Image showing test coverage of 80,8% for class App.java](https://github.com/Alex01234/EncryptedChatClientTCP_IP_Java_Maven/blob/master/EncryptedChatClientTCP_IP_Java_Maven_test_coverage.PNG?raw=true)

## Dependencies
