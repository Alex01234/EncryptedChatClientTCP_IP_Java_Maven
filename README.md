# EncryptedChatClientTCP_IP_Java_Maven
## Description
The chat client is a JavaFX application, connects to a server and sends/receives SealedObject:s to/from the server. 
In order to encrypt outgoing SealedObject:s and decrypt incoming ones, a Cipher is used.
The Cipher used is "AES/CBC/PKCS5PADDING", with a 128 bit IvParamaterSpec and a 128 bit SecretKeySpec.
The IvParameterSpec and the SecretKeySpec are submitted by the user when the application is running.
As such the submitted IvParameterSpec and the SecretKeySpec has to be the same for all the connected users in order to decrypt encrypted messages.
As demonstrated in the GIF, the four clients are all connected to the same server. However, User1 and User2 can only decrypt messages from each other since they have the same two 16 bit passwords submitted, which are used for the IvParameterSpec and the SecretKeySpec. As such, they can not read the messages from User3 and User4 which have submitted two different 16 bit passwords, even though the messages pass through the same server. The same goes for User3 and User4. 

![GIF showing demo of the chat client](https://github.com/Alex01234/EncryptedChatClientTCP_IP_Java_Maven/blob/master/EncryptedChatClient_demo.gif?)

## Testing and test coverage 
![Image showing test coverage of 80,8% for class App.java](https://github.com/Alex01234/EncryptedChatClientTCP_IP_Java_Maven/blob/master/EncryptedChatClientTCP_IP_Java_Maven_test_coverage.PNG?raw=true)

## Dependencies
