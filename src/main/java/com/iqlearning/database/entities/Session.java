package com.iqlearning.database.entities;
/*****
 * Author: Ewa Jasinska
 */
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @Column(name = "session_id")
    private String sessionID;
    @Column(name = "user_id")
    private Long userID;
    @Column(name = "creation_time")
    private Timestamp creationTime = new Timestamp(System.currentTimeMillis());

    public Session(){

    }

    public Session(String sessionID, Long userID, Timestamp creationTime) {
        this.sessionID = sessionID;
        this.userID = userID;
        this.creationTime = creationTime;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (sessionID != null ? !sessionID.equals(session.sessionID) : session.sessionID != null) return false;
        if (userID != null ? !userID.equals(session.userID) : session.userID != null) return false;
        return creationTime != null ? creationTime.equals(session.creationTime) : session.creationTime == null;
    }

    @Override
    public int hashCode() {
        int result = sessionID != null ? sessionID.hashCode() : 0;
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionID='" + sessionID + '\'' +
                ", userID=" + userID +
                ", creationTime=" + creationTime +
                '}';
    }
}
