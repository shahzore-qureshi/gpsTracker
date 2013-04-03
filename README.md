Android Tracker
==========

Want to find the location of your phone through GPS? This application will report the location via texting secret keywords from another phone.

Directions:

1) Set up two special keywords that you can use to text your phone in the future. 
   - When you text the first keyword to your phone, the application will send an email to a secret email address of your choosing with your phone's GPS Coordinates and a map of your phone's location.
   - When you text the second keyword to your phone, the application will send a text message back to you with a map of your phone's location.
   - Both tasks are done secretly in the background. This is particularly useful in a situation where your phone is lost and/or in the wrong hands.

2) Add an email account that will
   - create email messages with the phone's location
   - send them to an email address of your choosing.

You will need the SMTP server information of your email account, such as server name/port/security. Just look it up on Google. For example, if you look up "Gmail SMTP server settings", you will get many results. Gmail's SMTP server name is smtp.gmail.com and its SMTP port is 465. Gmail also uses SSL security.

3) Choose an email address that the application will send your location to. 

4) For testing purposes, press the "Send Email" or "Send SMS" buttons to send your location. For SMS, specify a phone number first.

5) If you so desire, you can clear your settings at any time.

NOTE: Please keep the GPS on. The application will only use the GPS when it detects a secret keyword. In all other cases, the GPS will be kept in idle mode, which saves your battery. To report an accurate location, the GPS is used for 60 seconds every time the application receives
a secret keyword.

To Do: 
- Sometimes, application is unable to receive SMS broadcasts. I will need to investigate further.
- Create button for disabling text parsing. 
