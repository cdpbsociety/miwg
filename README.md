Code repo:<br/>
https://github.com/cdpbsociety/miwg.git

Api GET Endpoints:<br/>
http://localhost:8080/items <br/>
http://localhost:8080/items/buy/Ale <br/>
Note: Ale is the item name

To build: <br/>
npm run build  <br/>
yarn build <br/>
mvn clean install -DskipTests
<br/><br/>To execute the tests:<br/>
npm run test <br/>
yarn test <br/>
mvn test
<br/><br/>To start the application on a Tomcat server at localhost:8080 :<br/>
npm run start <br/>
yarn start <br/>
mvn spring-boot:run

<br/><br/>
The architecture I used was MVC where the entry point is the InventoryController, which calls an InventoryService, which uses a fake repository to get the data.Security was handled using a spring servlet interceptor (SecurityHandler) that performs authentication based on the URI. For this exercise /items/buy is the only uri that is authenticated. Security is done via Basic Authorization, but it could easily be used for other token based mechanisms. So an "Authorization" header of "Basic xxx" is required to buy an item. Note that xxx is a base64 encoded username:password. SecurityService has a hard coded username="andy" and password="password" which is encoded as YW5keTpwYXNzd29yZA==I was a little confused with the surge pricing. The instructions said if 10 views are done then the price goes up 10%, so without bothering the business (you guys) I interpreted that as 10 get requests to /items (for all items and not each item individually since that service doesn't exist). When 10 views are called then a boolean state variable is set. If this was a production application I would have the surge pricing be in effect for each logged in user and item. For this application I didn't have a request object, just the path variable item name. The response is JSON and an http status code.Success is status code 200, as I defined it up above.Failures are status code 500 (an error), 401 (unauthorized credentials calling the buy), 404 (not found item). These responses are an ErrorResponse which is a JSON errorMessage. Errors are intercepted in the ExceptionHandlerController and processed by class type. There is a custom exception (NotFoundException) I used for the not found. I know not found is an argument for developers (is a 404 a bad url or an item not found?) but I went this direction.

