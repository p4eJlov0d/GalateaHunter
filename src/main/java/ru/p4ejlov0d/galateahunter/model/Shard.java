package ru.p4ejlov0d.galateahunter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.util.Identifier;

public class Shard {
    @JsonIgnore
    private Identifier texture;

    @JsonIgnore
    private String id;

    private String name;
    private String family;
    private String type;
    private String rarity;

    @JsonProperty("fuse_amount")
    private int fuseAmount;

    @JsonProperty("internal_id")
    private String internalId;

    public Identifier getTexture() {
        return texture;
    }

    public void setTexture(Identifier texture) {
        this.texture = texture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public int getFuseAmount() {
        return fuseAmount;
    }

    public void setFuseAmount(int fuseAmount) {
        this.fuseAmount = fuseAmount;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
}
