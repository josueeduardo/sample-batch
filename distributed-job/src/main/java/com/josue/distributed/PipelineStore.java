package com.josue.distributed;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.stage.StageChunkConfig;
import com.josue.batch.agent.stage.StageChunkExecutor;
import com.josue.distributed.job.SampleListener;
import com.josue.distributed.job.SampleProcessor;
import com.josue.distributed.job.SampleReader;
import com.josue.distributed.job.SampleWriter;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by Josue on 27/04/2016.
 */
@ApplicationScoped
public class PipelineStore {

    private static final Logger logger = Logger.getLogger(PipelineStore.class.getName());

    @Resource
    ManagedThreadFactory threadFactory;

    private static final Map<String, ChunkExecutor> pipelines = new ConcurrentHashMap<>();

    public ChunkExecutor getExecutor(String name) {
        ChunkExecutor chunkExecutor = pipelines.get(name);
        if (pipelines.containsKey(name)) {
            return chunkExecutor;
        }

        logger.info(":: Creating new job pipeline " + name + " ::");
        ChunkExecutor executor = newExecutor();
        pipelines.put(name, executor);
        return executor;
    }

    public Map<String, ChunkExecutor> getPipelines() {
        return new HashMap<>(pipelines);
    }

    private ChunkExecutor newExecutor() {
        StageChunkConfig config = new StageChunkConfig(SampleReader.class, SampleProcessor.class, SampleWriter.class);
        config.getListeners().add(SampleListener.class);
        config.instanceProvider(new CDIInstanceProvider());
        config.executorThreadFactory(threadFactory);
        config.executorCorePoolSize(10);
        config.executorMaxPoolSize(10);


        return new StageChunkExecutor(config);
    }

}
