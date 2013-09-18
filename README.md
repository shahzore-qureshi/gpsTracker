Android Tracker
==========

*Currently taken down from Play Store due to maintenance.*

Want to find the location of your phone via sending a secret text message? Well, now you can!

Directions:

1) Set up a special keyword that you can use to "ask" your phone for its location in the future. 
   - When you text this keyword to your phone, the application will send a text message back with a map of your phone's location.
   - This task is done secretly in the background. This is particularly useful in a situation where your phone is in the wrong hands.

2) Optional: Send a test SMS message with your phone's location to a phone number of your choosing for testing purposes.

3) Start the location tracking service. This service reads incoming SMS messages for the special keyword you specified. If the keyword is found, the service will attempt to locate your phone and send its location back to the phone number that originally sent the keyword.

NOTE: For the best accuracy, make sure that the GPS is on. The application will only use the GPS when it detects a secret keyword. In all other cases, the GPS will be kept in idle mode, which saves your battery. To report a very accurate location, the GPS is used for 60-120 seconds every time the application receives
the secret keyword.

License
==========

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
