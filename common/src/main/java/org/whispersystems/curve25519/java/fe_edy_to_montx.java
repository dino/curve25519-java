package org.whispersystems.curve25519.java;

public class fe_edy_to_montx {

//CONVERT #include "fe.h"

/*
u = (y + 1) / (1 - y)

NOTE: u=-1 is converted to y=0 since fe_invert is mod-exp
*/
public static void fe_edy_to_montx(int[] u, int[] y)
{
  int[] one = new int[10];
  int[] onepy = new int[10];
  int[] onemy = new int[10];

  fe_1.fe_1(one);
  fe_sub.fe_sub(onemy, one, y);
  fe_add.fe_add(onepy, one, y);
  fe_invert.fe_invert(onemy, onemy);
  fe_mul.fe_mul(u, onepy, onemy);
}

}
