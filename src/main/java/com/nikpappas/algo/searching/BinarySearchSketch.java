package com.nikpappas.algo.searching;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BinarySearchSketch extends PApplet {

  private int SIZE = 16;

  int circleS = 35;
  int margin = 50;

  public static void main(String[] args) {
    PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
  }


  List<Integer> list = new ArrayList<>();
  int target = 0;
  int current = -1;
  int start = 0;
  int end = 0;
  int steps = 0;

  boolean moveCur = true;

  @Override
  public void settings() {
    size(600, 300);
  }

  private void reset() {
    list = new ArrayList<>();
    for (int i = 0; i < SIZE; i++) {
      list.add((int) (random(100)));
    }
    list.sort(Comparator.naturalOrder());
    target = list.get((int) random(list.size()));
    current = -1;
    moveCur = true;
    start = 0;
    end = list.size() - 1;
    steps = 0;
  }

  @Override
  public void keyPressed() {
    step();
  }

  private void step() {
    if (moveCur) {
      current = start + (end - start) / 2;

    } else {
      int cur = list.get(current);
      if (cur == target) {
//        SIZE = SIZE + 1;
//        reset();
        return;
      }
      if (cur < target) {
        start = current + 1;
      } else {
        end = current - 1;
      }
      steps++;
    }
    moveCur = !moveCur;
  }

  @Override
  public void setup() {
    background(40);

    reset();
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::step, 1, 1, TimeUnit.SECONDS);
  }


  @Override
  public void draw() {
    background(40);
    var gap = (width - 2 * margin - circleS * SIZE) / (SIZE - 1);
    var y = height / 2;
    ellipseMode(CENTER);
    textAlign(CENTER, CENTER);
    for (int i = 0; i < list.size(); i++) {
      var x = margin + i * (gap + circleS) + circleS/2;
      noStroke();
      if (current == i) {
        if (list.get(current) == target) {
          fill(55, 50, 150);
        } else {
          fill(155, 50, 50);
        }
      } else if (end == i) {
        fill(55, 150, 0);
      } else if (start == i) {
        fill(55, 150, 150);
      } else {
        fill(200);
      }

      circle(x, y, circleS);
      if (current == i && list.get(current) == target) {
        fill(200);
      } else {
        fill(60);
      }
      text("" + list.get(i), x, y);
    }
    fill(160);
    text(target, width / 2, height / 10);
    text("steps: " + steps, width - 40, height / 10);

  }
}