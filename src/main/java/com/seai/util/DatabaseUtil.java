package com.seai.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class DatabaseUtil {

    public static Instant getInstant(ResultSet rs, String field) {
        try {
            return rs.getTimestamp(field).toInstant();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Timestamp saveInstant(Instant instant) {
        return Timestamp.from(instant);
    }

    public static Date getDate(ResultSet rs, String field) {
        try {
            return new Date(rs.getObject(field, java.sql.Date.class).getTime());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UUID getUuid(ResultSet rs, String field) {
        try {
            return UUID.fromString(rs.getString(field));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
