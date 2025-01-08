package com.drone.thesis.commons;

import com.drone.thesis.repository.LocalSQL;
import com.github.MakMoinee.library.services.MSqlite;

public class LocalDefaults {
    public static boolean isBound = false;
    public static MSqlite sql = new LocalSQL();
}
