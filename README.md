Emergency response Responder simulator
=========

Simulates the responders location based on the kafka messages recieved from mission service. The internval settings are in the configmap.


Pre-requisites: Running Kafka Cluster, Configmap

To deploy the service to a running single-node OpenShift cluster:

   ```
$ oc login -u developer -p developer

$ oc new-project MY_PROJECT_NAME

$ oc create -f config/configmap.yml

$ mvn clean fabric8:deploy -Popenshift

   ```

To test this service as a standalone module, add the following steps:

1. Once your Kafka cluster is running, install the Kafka Bridge module (using the Operator)
2. Expose your Kafka Bridge module service as an OCP route
3. The following example is a test `MissionStartedEvent` JSON message to trigger the Responder Simulator.  Base64 encode this message and copy it to a text editor:

```
{
	"id": "78e452f0-bc93-47b1-bee9-eecf161c06ef",
	"messageType": "MissionStartedEvent",
	"invokingService": "MissionService",
	"timestamp": 1578424673505,
	"body": {
		"id": "d35a54ef-d7eb-4a14-bb09-94aab42abb1c",
		"incidentId": "a0fe62eb-7448-42de-a280-74ccd801bd2e",
		"responderId": "11",
		"responderStartLat": 34.1681,
		"responderStartLong": -77.8851,
		"incidentLat": 34.2216,
		"incidentLong": -77.8227,
		"destinationLat": 34.2461,
		"destinationLong": -77.9519,
		"responderLocationHistory": [],
		"status": "CREATED",
		"steps": [{
			"lat": 34.1681,
			"lon": -77.8851,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.1653,
			"lon": -77.8855,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.1673,
			"lon": -77.8914,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2103,
			"lon": -77.8868,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2199,
			"lon": -77.8323,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2204,
			"lon": -77.8229,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2213,
			"lon": -77.8224,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2215,
			"lon": -77.8228,
			"destination": false,
			"wayPoint": true
		}, {
			"lat": 34.2215,
			"lon": -77.8228,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2222,
			"lon": -77.8242,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2216,
			"lon": -77.8247,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2244,
			"lon": -77.8302,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2277,
			"lon": -77.8297,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2551,
			"lon": -77.8709,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2508,
			"lon": -77.9474,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2484,
			"lon": -77.9477,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2467,
			"lon": -77.9488,
			"destination": false,
			"wayPoint": false
		}, {
			"lat": 34.2463,
			"lon": -77.9514,
			"destination": true,
			"wayPoint": false
		}]
	}
}
```
4. Using SOAPUi or Curl, run the following command using your base64 encoded message and the route for your Kafka bridge:

```
cat << EOF | curl -X POST \
  http://kafka-bridge-simon-responder-simulator.apps.cluster-e222.e222.example.opentlc.com/topics/topic-mission-event \
  -H 'content-type: application/vnd.kafka.json.v2+json' \
  -d '{
    "records": [
        {
            "value": "ewoJImlkIjogIjc4ZTQ1MmYwLWJjOTMtNDdiMS1iZWU5LWVlY2YxNjFjMDZlZiIsCgkibWVzc2FnZVR5cGUiOiAiTWlzc2lvblN0YXJ0ZWRFdmVudCIsCgkiaW52b2tpbmdTZXJ2aWNlIjogIk1pc3Npb25TZXJ2aWNlIiwKCSJ0aW1lc3RhbXAiOiAxNTc4NDI0NjczNTA1LAoJImJvZHkiOiB7CgkJImlkIjogImQzNWE1NGVmLWQ3ZWItNGExNC1iYjA5LTk0YWFiNDJhYmIxYyIsCgkJImluY2lkZW50SWQiOiAiYTBmZTYyZWItNzQ0OC00MmRlLWEyODAtNzRjY2Q4MDFiZDJlIiwKCQkicmVzcG9uZGVySWQiOiAiMTEiLAoJCSJyZXNwb25kZXJTdGFydExhdCI6IDM0LjE2ODEsCgkJInJlc3BvbmRlclN0YXJ0TG9uZyI6IC03Ny44ODUxLAoJCSJpbmNpZGVudExhdCI6IDM0LjIyMTYsCgkJImluY2lkZW50TG9uZyI6IC03Ny44MjI3LAoJCSJkZXN0aW5hdGlvbkxhdCI6IDM0LjI0NjEsCgkJImRlc3RpbmF0aW9uTG9uZyI6IC03Ny45NTE5LAoJCSJyZXNwb25kZXJMb2NhdGlvbkhpc3RvcnkiOiBbXSwKCQkic3RhdHVzIjogIkNSRUFURUQiLAoJCSJzdGVwcyI6IFt7CgkJCSJsYXQiOiAzNC4xNjgxLAoJCQkibG9uIjogLTc3Ljg4NTEsCgkJCSJkZXN0aW5hdGlvbiI6IGZhbHNlLAoJCQkid2F5UG9pbnQiOiBmYWxzZQoJCX0sIHsKCQkJImxhdCI6IDM0LjE2NTMsCgkJCSJsb24iOiAtNzcuODg1NSwKCQkJImRlc3RpbmF0aW9uIjogZmFsc2UsCgkJCSJ3YXlQb2ludCI6IGZhbHNlCgkJfSwgewoJCQkibGF0IjogMzQuMTY3MywKCQkJImxvbiI6IC03Ny44OTE0LAoJCQkiZGVzdGluYXRpb24iOiBmYWxzZSwKCQkJIndheVBvaW50IjogZmFsc2UKCQl9LCB7CgkJCSJsYXQiOiAzNC4yMTAzLAoJCQkibG9uIjogLTc3Ljg4NjgsCgkJCSJkZXN0aW5hdGlvbiI6IGZhbHNlLAoJCQkid2F5UG9pbnQiOiBmYWxzZQoJCX0sIHsKCQkJImxhdCI6IDM0LjIxOTksCgkJCSJsb24iOiAtNzcuODMyMywKCQkJImRlc3RpbmF0aW9uIjogZmFsc2UsCgkJCSJ3YXlQb2ludCI6IGZhbHNlCgkJfSwgewoJCQkibGF0IjogMzQuMjIwNCwKCQkJImxvbiI6IC03Ny44MjI5LAoJCQkiZGVzdGluYXRpb24iOiBmYWxzZSwKCQkJIndheVBvaW50IjogZmFsc2UKCQl9LCB7CgkJCSJsYXQiOiAzNC4yMjEzLAoJCQkibG9uIjogLTc3LjgyMjQsCgkJCSJkZXN0aW5hdGlvbiI6IGZhbHNlLAoJCQkid2F5UG9pbnQiOiBmYWxzZQoJCX0sIHsKCQkJImxhdCI6IDM0LjIyMTUsCgkJCSJsb24iOiAtNzcuODIyOCwKCQkJImRlc3RpbmF0aW9uIjogZmFsc2UsCgkJCSJ3YXlQb2ludCI6IHRydWUKCQl9LCB7CgkJCSJsYXQiOiAzNC4yMjE1LAoJCQkibG9uIjogLTc3LjgyMjgsCgkJCSJkZXN0aW5hdGlvbiI6IGZhbHNlLAoJCQkid2F5UG9pbnQiOiBmYWxzZQoJCX0sIHsKCQkJImxhdCI6IDM0LjIyMjIsCgkJCSJsb24iOiAtNzcuODI0MiwKCQkJImRlc3RpbmF0aW9uIjogZmFsc2UsCgkJCSJ3YXlQb2ludCI6IGZhbHNlCgkJfSwgewoJCQkibGF0IjogMzQuMjIxNiwKCQkJImxvbiI6IC03Ny44MjQ3LAoJCQkiZGVzdGluYXRpb24iOiBmYWxzZSwKCQkJIndheVBvaW50IjogZmFsc2UKCQl9LCB7CgkJCSJsYXQiOiAzNC4yMjQ0LAoJCQkibG9uIjogLTc3LjgzMDIsCgkJCSJkZXN0aW5hdGlvbiI6IGZhbHNlLAoJCQkid2F5UG9pbnQiOiBmYWxzZQoJCX0sIHsKCQkJImxhdCI6IDM0LjIyNzcsCgkJCSJsb24iOiAtNzcuODI5NywKCQkJImRlc3RpbmF0aW9uIjogZmFsc2UsCgkJCSJ3YXlQb2ludCI6IGZhbHNlCgkJfSwgewoJCQkibGF0IjogMzQuMjU1MSwKCQkJImxvbiI6IC03Ny44NzA5LAoJCQkiZGVzdGluYXRpb24iOiBmYWxzZSwKCQkJIndheVBvaW50IjogZmFsc2UKCQl9LCB7CgkJCSJsYXQiOiAzNC4yNTA4LAoJCQkibG9uIjogLTc3Ljk0NzQsCgkJCSJkZXN0aW5hdGlvbiI6IGZhbHNlLAoJCQkid2F5UG9pbnQiOiBmYWxzZQoJCX0sIHsKCQkJImxhdCI6IDM0LjI0ODQsCgkJCSJsb24iOiAtNzcuOTQ3NywKCQkJImRlc3RpbmF0aW9uIjogZmFsc2UsCgkJCSJ3YXlQb2ludCI6IGZhbHNlCgkJfSwgewoJCQkibGF0IjogMzQuMjQ2NywKCQkJImxvbiI6IC03Ny45NDg4LAoJCQkiZGVzdGluYXRpb24iOiBmYWxzZSwKCQkJIndheVBvaW50IjogZmFsc2UKCQl9LCB7CgkJCSJsYXQiOiAzNC4yNDYzLAoJCQkibG9uIjogLTc3Ljk1MTQsCgkJCSJkZXN0aW5hdGlvbiI6IHRydWUsCgkJCSJ3YXlQb2ludCI6IGZhbHNlCgkJfV0KCX0KfQ=="
        }
    ]
}'
EOF
```
5. Monitor the *Responder Simulator* pod logs. You should notice that the process has kicked-off.
