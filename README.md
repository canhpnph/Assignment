# LoverManagerApplication
*** Hello every one!!!
This is a simple app that I created during my studies. This file of mine contains two parts, `the mobile app` and `the server`. 
- First is `the server`, I build a simple server using `NodeJS` + `ExpressJS` framework.

  ( If you don't have NodeJS installed on your computer, you can refer from here:
  
  `https://www.twilio.com/docs/usage/tutorials/how-to-set-up-your-node-js-and-express-development-environment` )
  + There are a few libraries like:
    - `Json Web Token ( JWT )`: `npm install jsonwebtoken`
    - `Socket.io`: `npm install socket.io`
    - `Multer`: `npm install --save multer`
    - `Bcrypt`: `npm i bcrypt`
    - `Firebase Admin SDK `: `npm install firebase-admin`
  + Besides, I use `MongoDB` as the database for the application: `npm install mongoose`
  + As mentioned before, I use the Firebase Admin library to push notifications from the server to the app client. I'll leave the instructions here:
    - Add the Firebase Admin SDK to your server: `https://firebase.google.com/docs/admin/setup#node.js_1`
    - Build app server send requests: `https://firebase.google.com/docs/cloud-messaging/send-message#node.js`
    - Authorize send requests: `https://firebase.google.com/docs/cloud-messaging/auth-server`
- About the `application`:
  + I use `Java` programming language to create it.
  + As well as use `RESTful API` to be able to interact with the Server.
  + To make this easier, I use the `Retrofit2` library:
    
       `implementation 'com.squareup.retrofit2:retrofit:2.9.0'`
    
       `implementation 'com.squareup.retrofit2:converter-gson:2.9.0'`
    
   This library makes it easy for me to CRUD. In addition, I also use `Socket.io` to make a `Chat` available to everyone using the application:
  
    `implementation ('io.socket:socket.io-client:2.0.0') {
         exclude group: 'org.json', module: 'json'
       }`

   In addition to `Chat`, I also use `Socket.io` to check the login status of a user. That means each user is only allowed to log in on a single mobile device
   
  + In order to get notified when the server arrives, I also read the Firebase docs. If you don't understand them, you can find them in the `LoginActivity.java` file and the `FireBaseCloudMessageService.java` file.

  

This app is just simple app. I will update them in the future. Thank all for reading <3
     
