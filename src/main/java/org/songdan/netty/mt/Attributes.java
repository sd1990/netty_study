package org.songdan.netty.mt;

import io.netty.util.AttributeKey;

public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
