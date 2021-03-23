# Calculate amount of fuel has been filled and corresponding fuel cost.

**Tech stack**: Java, SpringBoot, Kafka.

From system point of view, mainly there are two modules:

1. **PublishingAgent**: sits on the vehicle and periodically publishes beacon signal messages onto topic. This agent can be located on any type of vehicle. Each vehicle have its own unique key, vehicleId, a unique identifier for every single vehicle. Beacon signal is triggered every 2 minutes and publishes message on the topic.

2. **IngestionEngine**: This is the heart of the system which handles the various business logic and calculation. This module fetches messages from the kafka topic, processes it and provides details such as how many litres of fuel is filled in the tank and the total cost of it based on the location of the vehicle.

## Publisher Agent:
	- Features: 
		- Authorization: Modified Caesar Cipher encryption is enforced. As mentioned earlier, there will be unique identifier, vehicleId, with which encrytion code will be generated. As stated, it is modified hashing function so it won't be as much easy to crack it. Due to time limitation, I have gone ahead with this approach. I could have gone with Jwt with OAuth2.0 but such level sophistication was not required.
		- Throttling/Rate Limiting: To mitigate DOS attacks, module has the Security Filter which allows other modules to invoke 3 requests per second at max. It tracks the IP address and keeps a counter against it.  
		- API Documentation: Swagger2.0 
		- Scheduler : every 2 minutes, beacon signal is published with latest location along lid status (true when lid is open, false when lid is closed)
		- Asynchronous Message publishing: both, scheduled and instant API call publishes message on Kafka topic which is robust, reliable and efficient.
	- Consideration:
		- In the original problem statement, only city was to be provided. However, taking liberty to modify it and add additional parameters for robust system. Instead of city, district is to be provided along with the state. Hence caller is responsible for providing 3 Request parameters (lidStatus, district, state) when it invokes /publish API.
		- Authorization header: caller needs to provide "Authorization" within header along with the encrypted key. Encryption logic is commented within SecurityFilter in the code base. For simplicity, token is provided in the application.properties if anyone wants to test
		- Post Request: /publish API is exposed. One can also go through Swagger API link: http://localhost:20001/swagger-ui.html
		- When lid is closed, it is expected to invoke /publish service with lidStatus as false to accurately calculate details.
		- Kafka Topic Name: For this sample project, "my_topic" topic was created. Anyone who wants to test out the application should create the same.
		- Other configuration are mentioned in the application.properties file

		

## Ingestion Engine:
	- Features:
		- Caching fuel price for district and state for 24 hours and evict policy
		- Retry mechanism: when external API is down, retrial mechanism is in place to hit it 3 times 
		- API Documentation: Swagger2.0
		- External API for latest fuel price
		- Multi vehicle tracking 
		- Calculates the amount of filled fuel and total cost of it
		- In case external API is down or unavailable, fallback mechanism is in place which provides mock fuel price for that state
	- Consideration:
		- Total fuel cost and amount of fuel filled in vechile will be calculated once the lid is closed.

		


## Assumptions:
	- Connection is uninterrupted to get the precise tracking of location
	- lid is properly closed
	- Edge case: Cache Eviction happens on midnight. When it is in progress, fuel price of day prior will be calculated
	- Both modules resides in the same timezone.

## Steps to run the code:
	- Links: 
		-Publishing Agent: http://localhost:20001/swagger-ui.html
		-Ingestion Engine: http://localhost:20002/swagger-ui.html
	- Setup Kakfa on 9092 port with group-id as group-id.
	- PublishingAgent is listening to 20001 port.
	- Run IngestionEngineApplication.java and PublishingAgentApplication.java
	- Either fetch authorization token provided for simplicity in application.properties of PublishingAgent module or generate token from the code provided in the Security filter of the same module. For different vehicle, vehicleId needs to be changed in application.properties and so will change the authorization token.
	- Hit url= http://localhost:20001/agent/publish?lidStatus={}&district={}&state={}
		where lidStatus is either false when lid is closed or true when lid is open,
		district is from where vehicle is fueling,
		state is where district is located.
		e.g. http://localhost:20001/agent/publish?lidStatus=false&district=WEST GODAVARIA&state=Andhra Pradesh
	- Above url is of type POST and will require Authorization Header to pass the token to authenticate.
	- First pass the lidStatus as true then, after sometime, pass value false for the same district and state combination. 
	- Notice on the IngestionEngine logs, Total cost and the Amount of Fuel filled in the tank is printed.

## Limitations:
	- Due to time constriant, these features ( SSL, OAuth2.0 , Jwt , Message encrption and SSL on kafka queues) are not incorporated.
	- External API is not robust and intermittently fails to provide data. Hence, while booting up the Ingestion Engine, BootstrapLoader.java class loads sample values of 

## TODO:
	- Dockerization
	- Test Cases (I have tested with Postman with wide variety of use cases but didn't get time to write test cases)
	- Proper Authentication
	- In-depth documentation (both Readme.md and Swagger)
	- Granual modularization of code to keep it more generic

## External Service Reference:
	-https://fuelprice-api-india.herokuapp.com/