package com.drone.thesis.repository;

import com.github.MakMoinee.library.services.MSqlite;

public class LocalSQL extends MSqlite {

    private static final String dbName = "copterDb";

    public LocalSQL() {
        super(dbName);
    }


}
