# RapidPush Java library
This Java lib is used to send notifications to your android devices using the RapidPush-Service.

# What is RapidPush?
RapidPush is an easy-to-use push notification service.
You can receive notifications from third-party applications like nagios, github or flexget directly to your smartphone.
With our simple API you can also implement RapidPush to your own software.

# How to use
Look at the included example to check how this JAVA-library works.

# SSL
If you want to use SSL on the requests, make sure that your JVM KeyStore includes the StartCom CA certificates.
You will find all needed certificates at http://www.startssl.com/certs/
How to manually install the ca-certificates have a look at http://www.sslshopper.com/article-most-common-java-keytool-keystore-commands.html which has some nice help for different commands.
We have included a script which will import the StartSSL ca certificates for you (import-startssl.sh).  __`MANY thanks to Klaus Reimer`__

If you want to provide RapidPush within your application, which will be used by customers, you should tell your customers that they need to include the 
SSL-Certificates.

# Example
Make sure that you have an account on [RapidPush](http://rapidpush.net) and got an API-Key which you can create within your user interface.
After you checked this compile the code and just execute the example by **java example.class**.
In the output, you will find all needed informations what parameter can be used for this example.
