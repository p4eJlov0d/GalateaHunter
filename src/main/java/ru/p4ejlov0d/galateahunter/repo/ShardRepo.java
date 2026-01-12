package ru.p4ejlov0d.galateahunter.repo;

import ru.p4ejlov0d.galateahunter.model.Shard;

import java.io.File;
import java.util.List;

public interface ShardRepo {
    File[] getShardImages();

    File getShardData();

    List<Shard> getShards();
}
