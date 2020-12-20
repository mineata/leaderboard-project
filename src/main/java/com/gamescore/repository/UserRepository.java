package com.gamescore.repository;

import com.gamescore.model.Score;
import com.gamescore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Repository
public class UserRepository extends JdbcDaoSupport {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initiliaze(){
        setDataSource(dataSource);
    }

    Long count = Long.valueOf(1);

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<Map<String, Object>>  rows = getJdbcTemplate().queryForList(sql);

        List<User> result = new ArrayList<>();
        for (Map<String, Object> row:rows) {
            User user = new User();
            byte [] bytes = (byte[]) row.get("user_id");
            UUID user_id = getUUIDFromBytes(bytes);
            user.setUser_id(user_id);
            user.setDisplay_name((String) row.get("display_name"));
            user.setPoints((Float) row.get("points"));
            user.setRank((Long) row.get("rank"));
            user.setCountry((String) row.get("country") );
            result.add(user);
        }
        return result;
    }

    public List<User> getAllUsersByCountry(String code) {
        String sql = "SELECT * FROM users WHERE country ='"+ code +"'";
        List<Map<String, Object>>  rows = getJdbcTemplate().queryForList(sql);

        List<User> result = new ArrayList<>();
        for (Map<String, Object> row:rows) {
            User user = new User();
            byte [] bytes = (byte[]) row.get("user_id");
            UUID user_id = getUUIDFromBytes(bytes);
            user.setUser_id(user_id);
            user.setDisplay_name((String) row.get("display_name"));
            user.setPoints((Float) row.get("points"));
            user.setRank((Long) row.get("rank"));
            user.setCountry((String) row.get("country"));
            result.add(user);
        }
        return result;
    }

    public User getUserById(UUID id){
        String sql = "SELECT * FROM users WHERE user_id = ?";
        byte [] bytes = getBytesFromUUID(id);
        return (User)getJdbcTemplate().queryForObject(sql, new Object[]{bytes}, new RowMapper<Object>() {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                User user = new User();
                user.setUser_id(id);
                user.setDisplay_name(rs.getString("display_name"));
                user.setPoints(rs.getFloat("points"));
                user.setRank(rs.getLong("rank"));
                user.setCountry(rs.getString("country"));
                return user;
            }
        });

    }

    public void addUser(User user){
        String sql = "INSERT INTO users " + "(user_id, display_name, points, rank, country) VALUES (?,?,?,?, ?)";

//        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = getBytesFromUUID(user.getUser_id());

        Integer points = 0;
//        Long rank = count;

        getJdbcTemplate().update(sql, new Object[] {uuidBytes, user.getDisplay_name(), points, user.getRank(), user.getCountry()});
        count +=1;
    }

    public void submitScore(Score score) {
        User user = getUserById(score.getUser_id());
        String sql ="UPDATE users SET points =? WHERE user_id =?";
        Float newPoint = user.getPoints() + score.getScore_worth();

        byte [] bytes_id = getBytesFromUUID(score.getUser_id());

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        score.setTimestamp(timestamp.getTime());
        getJdbcTemplate().update(sql, newPoint, bytes_id );
    }


    public byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }

    public UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Long high = byteBuffer.getLong();
        Long low = byteBuffer.getLong();

        return new UUID(high, low);
    }

}
