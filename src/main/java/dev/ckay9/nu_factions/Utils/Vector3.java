package dev.ckay9.nu_factions.Utils;

public class Vector3 {
  public int x;
  public int y;
  public int z;

  public Vector3(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3 addToAll(int change) {
    this.x -= change;
    this.y -= change;
    this.z -= change;
    return this;
  }
}
