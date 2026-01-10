package ru.p4ejlov0d.galateahunter.model;

import net.minecraft.util.Identifier;

public class Shard {
    private final Identifier texture;
    private final String name;

    public Shard(Identifier texture, String name) {
        this.texture = texture;
        this.name = name;
    }

    public Identifier getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }
}
