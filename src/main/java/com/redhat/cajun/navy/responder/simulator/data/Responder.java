package com.redhat.cajun.navy.responder.simulator.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.core.json.Json;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Responder {

    private String responderId = null;

    private String missionId = null;

    private String incidentId = null;

    @JsonIgnore
    private Queue<MissionStep> queue = null;

    private boolean isHuman = false;

    private boolean isContinue = true;

    private Status status = Status.RECEIVED;

    public enum Status {
        RECEIVED("RECEIVED"),
        PREP("PREP"),
        READY("READY"),
        MOVING("MOVING"),
        STUCK("STUCK"),
        PICKEDUP("PICKEDUP"),
        DROPPED("DROPPED");

        private String actionType;

        Status(String actionType) {
            this.actionType = actionType;
        }

        public String getActionType() {
            return actionType;
        }
    }



    public Responder() {
        queue = new LinkedList<>();
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean aContinue) {
        isContinue = aContinue;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public Queue<MissionStep> getQueue() {
        return queue;
    }

    public void setQueue(Queue<MissionStep> queue) {
        this.queue = queue;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    public MissionStep peek(){
        return queue.peek();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }
    public MissionStep nextLocation() {
        return queue.poll();
    }

    public void addNextLocation(MissionStep step) {
        if (queue != null) {
            queue.add(step);
        }
    }


    public void setLocation(MissionStep location) {
        addNextLocation(location);
    }

    public MissionStep getLocation() {
        return queue.peek();
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Responder responder = (Responder) o;
        return Objects.equals(responderId, responder.responderId);
    }


    @Override
    public int hashCode() {
        return responderId.hashCode();
    }

    @Override
    public String toString() {
        return Json.encode(this);
    }
}
