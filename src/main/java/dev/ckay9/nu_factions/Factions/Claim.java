package dev.ckay9.nu_factions.Factions;

import dev.ckay9.nu_factions.Utils.Vector3;

public class Claim {
  Vector3 starting_positon;
  Vector3 ending_position;
  String claim_name;

  public Claim(Vector3 start, Vector3 end, String name) {
    this.starting_positon = start;
    this.ending_position = end;
    this.claim_name = name;
  } 
}
