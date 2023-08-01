# LoverManagerApplication
*** Hello every one!!!
This is a simple app that I created during my studies. This file of mine contains two parts, `the mobile app` and `the server`. 
- First is `the server`, I build a simple server using `NodeJS` + `ExpressJS` framework.
  + There are a few libraries like:
    - `Json Web Token ( JWT )`: `npm install jsonwebtoken`
    - `Socket.io`: `npm install socket.io`
    - `Multer`: `npm install --save multer`
    - `Bcrypt`: `npm i bcrypt`,...
  + Besides, I use `MongoDB` as the database for the application: `npm install mongoose`
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

This app is just simple app. I will update them in the future. Thank all for reading <3
     
