package ru.p4ejlov0d.galateahunter.repo;

import ru.p4ejlov0d.galateahunter.model.Shard;

import java.io.File;
import java.util.Map;

public interface ShardRepo {
    File[] getShardImages();

    File getShardData();

    Map<String, Shard> getShards();
}
