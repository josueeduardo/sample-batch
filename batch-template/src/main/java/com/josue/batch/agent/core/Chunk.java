package com.josue.batch.agent.core;

import com.josue.batch.agent.metric.Meter;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created by Josue on 20/04/2016.
 */
public abstract class Chunk implements Serializable {

    public Chunk() {
    }

    public void init(Properties properties, Meter meter) throws Exception {

    }

    public void close() throws Exception {

    }

}
