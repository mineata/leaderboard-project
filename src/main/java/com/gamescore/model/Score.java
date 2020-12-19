package com.gamescore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Score extends JdkSerializationRedisSerializer implements Serializable {

    private Float score_worth;
    private UUID user_id;
    private Long timestamp;

    public Score(){}

    public Score(Float score_worth, UUID user_id, Long timestamp) {
        this.score_worth = score_worth;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public Float getScore_worth() {
        return score_worth;
    }

    public void setScore_worth(Float score_worth) {
        this.score_worth = score_worth;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
