# vertx_socket_issue_replicate


Actual Response: <br />

Response From HTTP 1 1 For the Request 1<br />
Response From HTTP 2 2 For the Request 2<br />
Response From HTTP 3 3 For the Request 3<br />
Response From Socket 1 1 For the Request 3<br />

<br />
<br />
Expected Response:<br />

Response From HTTP 1 1 For the Request 1<br />
Response From HTTP 2 2 For the Request 2<br />
Response From HTTP 3 3 For the Request 3<br />
Response From Socket 1 1 For the Request 1<br />
Response From Socket 2 2 For the Request 2<br />
Response From Socket 3 3 For the Request 3<br />

<br /><br />
If the messages are delayed, then the output is as expected.<br /> 
Here, uncomment the ```Line: 26 in App.java``` to delay the messages. 


