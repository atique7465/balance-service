# balance-service

### Description
This service is design to facilitate customers of a financial organisation who wants to see their monthly balance and cumulative balance.

# Table of Contents
- [Description](#description)
- [Technology](#technology)
- [Remote Deployment](#remote-deployment)
- [APIs](#apis)
- [External Service call](#external-service-call)
- [Optimization Scope](#optimization-scope)
- [Design](#design)
- [Annotations](#annotations)
- [Correlation](#correlation)
- [Logging](#logging)
- [HTTP](#http)
- [Circuit Breaker](#circuit-breaker)
- [Exception Resolver](#exception-resolver)

### Technology
1. Spring Boot:3.1.0
2. Java: 17
3. Gradle:7.5.1

### Remote Deployment
1. run the build task or go to terminal and execute
   ```
   //configure the termonal with gradle and jdk and execute
   $ gradle clean build
   
   //or
   $ ./gradlew clean build
   ```
2. after execution and executable jar **_[balance-service-1.0.jar]_** will be generated in _./build/libs_ directory
3. deploy the jar to remote server and execute bellow command to run the jar
   ```
   java -jar balance-service-1.0.jar
   ```

### APIs
1. GET /api/v1/balance/{accNo}
   1. Path Param _**accNo** Customer Account Number._
   2. Response with a summary of Balance sorted by months<br>
      Example: 
      ```
      {
        "content": [
           {
             "month": "05-2023", //Format: "MM-yyyy"
             "monthlyBalance": 1000.54,
             "cumulativeBalance": 2000.54
           }
        ]
      }
      ```
2. OpenAPI: http://localhost:8084/balance-service-1.0/swagger-ui/index.html
3. Actuator: http://localhost:8085/actuator/info

### External Service call
1. **transaction-history-service-1.0**
   1. API: GET /api/v1/transaction-history/{accNo}
   2. Response with a list of transactions <br>
      Example
      ```
      {
       "content": [
          {
            "transactionType": "CREDIT",
            "amount": 1000.75,
            "approvalDateTime": "01-08-2022 10:45:01" //Format: "dd-MM-yyyy HH:mm:ss"
          }
        ]
      }
      ```
      
### Optimization Scope
Currently, all the transaction history are pulled from external transaction-history-service on every balance summary enquiry call. Fetching huge amount of data can cause **network errors**, **memory issue** & **application performance** issues. 
1. **Database**: We can cache the balance summary for the customers by months in balance-service database.
2. **Preprocessing**: Previous transaction history can be preprocessed and balance summary can be stored in balance-service database.
3. So, we don't need to call external transaction-history-service every time if we already have the summary present in balance-service database.
4. We will call transaction-history-service for delta months for which we don't have the summary in balance-service database and also store them as well. 

### Design
1. **controller** layer
   1. GET /api/v1/balance/{accNo}
2. **service** layer
   1. Get Translation history form dao 
   2. Balancer Summary Calculation Logic
3. **dao** layer
   1. External Api Call to _transaction-history-service-1.0_
4. **infrastructure**
   1. **annotations** - _Custom Annotations_
   2. **correlation** - _Correlation filters & interceptors_
   3. **logging** - _Logging filter & interceptors_
   4. **http** - _RestTemplate configuration, Connection Pooling, Error Handler & Custom ApiGateWays_
   5. **circuitbreaker** - _Circuit Breaker Configurations_
   6. **exceptionresolver** - _Exceptions & resolvers_ 
   7. **util** - _Common utility functions_
   8. **others**

### Annotations
1. @AccountNoValidator
   1. To validate the customer account no based on configuration.

### Correlation
Main purpose is to propagate some correlation headers from up-stream service to down-stream service & vice-versa. <br>
1. **Headers**:
   1. x-bs-correlation-id: _an unique id for every incoming request_
   2. x-bs-caller-service: _to identify which up-stream service called the request_
2. **Filter**
   1. CorrelationFilter: _Generate & propagate unique correlation headers in HttpServletRequest & HttpServletResponse_
3. **Interceptor**
   1. CorrelationApiGatewayInterceptor: _propagate the correlation headers to out going call HttpRequest & ClientHttpRequestExecution_
4. **LoggerContext**
   1. has functionality to put the correlation fields _[correlationId, callerService]_ in MDC to log & identify each request uniquely.
   2. correlation ID is logged as CID in logback.xml by 
      ```
      <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS}.%thread> %-5level-CID[%X{correlationId}] -%msg%n</pattern> 
      ```

### Logging
Main purpose is to log incoming & out going url, headers, request, response, response time etc. for future debug.
1. **Filter**
   1. LoggingFilter: _Log the incoming HttpServletRequest & HttpServletResponse contents._
2. **Interceptor**
   1. LoggerApiGatewayInterceptor: _Log out going HttpRequest & ClientHttpResponse contents._
3. **LoggingProperties**: _[Customizable from property source. Defaults are given here.]_ <br>
   ```
   #################### APP HTTP log Configuration ####################
   app.http.log:
     enabled: true
     printHeader: false
     printPayload: true
     maxPayloadSize: 2000
     printableContent: application/json
   ```

### HTTP
1. **gateway**: _Custom interface with RestTemplate functions. Implementations of this interface can use custom configured RestTemplates._
2. **RestTemplateConfiguration**: _RestTemplate is configured here with custom configurations, PoolingHttpClientConnectionManager with pooling, Error Handler, Error Extractor, Interceptors._
3. **HttpConnectionPoolProperties**: _[Customizable from property source. Defaults are given here.]_<br>
   ```
   #################### HTTP Connection Pooling Configuration [times are in millis] ####################
   http.pool:
      maxTotalConnection: 200
      maxConnectionPerRoute: 100
      connectionRequestTimeout: 5000
      connectTimeout: 5000
      defaultKeepAliveTime: 30000
      timeToLive: 300000
      validateAfterInactivity: 60000
      responseTimeout: 5000
      socketTimeout: 5000
      evictIdlConnection: true
      evictIdlConnectionAfter: 30000
   ```
5. **HttpRouteProperties**: _External Service wise base path and max connection can be configured using these properties._ <br>
   Example:
   ```
   #################### External Route Configuration ####################
   http.route.map:
    THS:
      scheme: http
      host: localhost
      port: 8086
      contextPath: /transaction-history-service-1.0
      maxConnection: 200
   ```

### Circuit Breaker
resilience4j circuit breaker is used. Default configuration can be set from property source. 
1. **CircuitBreakerProperties**: _[Customizable from property source. Defaults are given here.]_<br>
   ```
   #################### Circuit Breaker Configuration [times are in millis] ####################
   app.circuit.breaker:
     slidingWindowSize: 50
     minimumNumberOfCalls: 50
     failureRateThreshold: 50
     slowCallRateThreshold: 50
     slowCallDurationThreshold: 5000
     waitDurationInOpenState: 10000
     permittedNumberOfCallsInHalfOpenState: 10
     slidingWindowType: COUNT_BASED
     recordExceptions: org.springframework.web.client.ResourceAccessException
     ignoreExceptions: com.atique.balanceservice.infrustructure.exceptionresolver.exception.BaseException, com.atique.balanceservice.infrustructure.exceptionresolver.exception.ExternalServiceException
   ```

### Exception Resolver
1. **exception**: - _Custom exception_
2. **model**: _Generic Error Response Model._ <br>
   Example:
   ```
   {
     "reason": "10_00_001", //Format: <Component Code>_<Feature Code>_<Error Code>
     "message": "Balance service. Invalid acc no."
   }
   ```
3. **BaseExceptionResolver**
   1. _Resolver with @ExceptionHandler annotated methods to handle different types of exceptions with proper Http Status Code & ErrorResponse._
   2. _Every controller must have a ExceptionResolver with extends BaseExceptionResolver and set corresponding FeatureCode._
4. **Special Errors**:
   ```
   reason: 10_00_997 | message: Balance Summary. Can not parse external service error response.
   reason: 10_00_996 | message: Balance Summary. External service - unavailable.
   reason: 10_00_995 | message: Balance Summary. External service - read time out.
   reason: 10_00_994 | message: Balance Summary. External service - call not permitted. Circuit is open.
   ```