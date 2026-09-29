package com.example.pipedetection.dto;

/** Optional request body when dispatching a drone to a leak. If droneId is null, the
 *  system automatically picks the first AVAILABLE drone. */
public class DispatchDroneRequest {

    private Long droneId;

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }
}
