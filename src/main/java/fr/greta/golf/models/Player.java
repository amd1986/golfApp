package fr.greta.golf.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Player {
    @NotNull
    private String name;
    @NotNull
    private int licence;
    @NotNull
    private double idx;
    @NotNull
    private String serie;
    @NotNull
    private String rep;
    @NotNull
    private String nat;
    @NotNull
    private String club;
    private String comment;


}
