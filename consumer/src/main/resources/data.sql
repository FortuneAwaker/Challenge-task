DROP TABLE IF EXISTS trip_waypoint cascade ;
DROP TABLE IF EXISTS booking cascade ;

CREATE TABLE booking
(
    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
    passenger_name           varchar(50) NOT NULL,
    passenger_contact_number varchar(50) NOT NULL,
    pickup_time              TIME        NOT NULL,
    asap                     BOOLEAN     NOT NULL,
    waiting_time             TIME        NOT NULL,
    number_of_passengers     INT         NOT NULL,
    price                    DOUBLE      NOT NULL,
    rating                   DOUBLE      NOT NULL,
    created_on               DATE,
    last_modified_on         DATE
);

CREATE TABLE trip_waypoint
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    locality  varchar(50) NOT NULL,
    latitude  DOUBLE      NOT NULL,
    longitude DOUBLE      NOT NULL,
    booking_id BIGINT
);

ALTER TABLE trip_waypoint
    ADD FOREIGN KEY (booking_id)
        REFERENCES booking (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT;


INSERT INTO booking(passenger_name, passenger_contact_number, pickup_time, asap, waiting_time, number_of_passengers,
                    price, rating, created_on, last_modified_on)
VALUES ('Alex', '123456789123', '10:00', true, '10:30', 15, 30.5, 9.7, '2021-05-12', '2021-05-12'),
       ('Happy Tourist', '098765432156', '12:00', true, '12:30', 4, 27, 9.2, '2021-05-13', '2021-05-13');

INSERT INTO trip_waypoint(locality, latitude, longitude, booking_id)
VALUES ('Minsk', 53.893009, 27.567444, 1),
       ('Brest', 52.097622, 23.734051, 1),
       ('Grodno', 53.669353, 23.813131, 1),
       ('Vitebsk', 55.187222, 30.205116, 2),
       ('Mogilev', 53.900716, 30.33136, 2),
       ('Gomel', 52.4411761, 30.9878462, 2);
