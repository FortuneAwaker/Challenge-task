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
