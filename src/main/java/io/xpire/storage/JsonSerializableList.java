package io.xpire.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

/**
 * An Immutable Xpire that is serializable to JSON format.
 */
@JsonRootName(value = "xpire")
public class JsonSerializableList {

    private final JsonSerializableXpire xpire;
    private final JsonSerializableReplenishList replenishList;

    public JsonSerializableList(@JsonProperty("items") JsonSerializableXpire xpire,
                                @JsonProperty("replenishList") JsonSerializableReplenishList replenishList) {
        this.xpire = xpire;
        this.replenishList = replenishList;
    }

}
