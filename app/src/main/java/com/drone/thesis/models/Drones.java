package com.drone.thesis.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Drones {
    private int id;
    private int userID;
    private String droneName;
    private String ip;
    private String status;

    public Drones(DroneBuilder builder) {
        this.id = builder.id;
        this.userID = builder.userID;
        this.droneName = builder.droneName;
        this.ip = builder.ip;
        this.status = builder.status;
    }

    public static class DroneBuilder {

        private int id;
        private int userID;
        private String droneName;
        private String ip;
        private String status;

        public DroneBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public DroneBuilder setUserID(int userID) {
            this.userID = userID;
            return this;
        }

        public DroneBuilder setDroneName(String droneName) {
            this.droneName = droneName;
            return this;
        }

        public DroneBuilder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public DroneBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Drones build() {
            return new Drones(this);
        }
    }
}
