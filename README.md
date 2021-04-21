Description. This is backend API layer to manage bookings. Message broker RabbitMQ was used to manage the events produced and consumed by and from this application. To launch RabbitMQ docker-compose file was added to project.

Code is provided as Maven multi-module project. This aplication consists of three modules: producer, consumer and model. Model contains common dto classes which are used by both other modules. Producer is REST application that sends messages to event broker. If it is add or edit method, producer converts dtos to json and send them as messages. Consumer layer processes messages from message broker using listeners. It also contains configuration for RabbitMQ. Producer and Consumer are spring boot applications and can build and start independently from each other.

There are five queues to handle messages: add, edit, delete, audit and exception. Add, edit and delete are used for actions with bookings. Audit queue is for simple logging messages. Exception queue is used to process exceptions of Consumer application (for example ResourceNotFoundException, ConstraintViolationException). Advice controller is used to handle exceptions of Producer.

Workflow.

1. Open folder with this project, open CLI.

2. Run command "mvn clean install"

3. Run command "docker-compose up"

Producer(producer-app) will be started on port 9090 and Consumer(consumer-app) on port 8090.
Send request to producer, it will send message to message broker.
Consumer will process this message.
Results of processing can be seen in console of Consumer.

You can then access the rabbitMQ web interface at http://localhost:15672 with username:"guest" and password:"guest".

You can then access the H2 console at http://localhost:8090/h2-console with JDBC url:"jdbc:h2:mem:testdb", username:"username" and password:"password".
