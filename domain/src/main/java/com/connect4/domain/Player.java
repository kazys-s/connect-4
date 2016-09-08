package com.connect4.domain;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Wither;

@Data
@Wither
public class Player implements WithId {
    @NonNull
    private final Integer id;
    @NonNull
    private final String name;
}
