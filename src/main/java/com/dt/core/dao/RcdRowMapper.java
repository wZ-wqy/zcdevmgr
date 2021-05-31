package com.dt.core.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RcdRowMapper implements RowMapper<Rcd> {

    private RcdSet ownerSet;

    public RcdRowMapper(RcdSet set) {
        ownerSet = set;
    }

    public Rcd mapRow(ResultSet rs, int row) throws SQLException {
        // Statement stmt=null;

        if (!ownerSet.isMetaDataInited()) {
            ownerSet.initeMetaData(rs.getMetaData());
        }

        Rcd r = new Rcd(ownerSet);
        for (int i = 1; i <= ownerSet.getMetaData().getColumnCount(); i++) {
            r._setValue(i - 1, rs.getObject(i));
        }
        ownerSet.add(r);
        return r;
    }

}
