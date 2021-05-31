package com.dt.core.dao.sql;

import com.dt.core.dao.SpringDAO;

public interface ExecutableSQL extends SQL {
    Integer execute();

    SpringDAO getDao();

    ExecutableSQL setDao(SpringDAO dao);
}
