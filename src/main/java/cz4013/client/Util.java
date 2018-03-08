package cz4013.client;

import java.util.Scanner;

public class Util {
  private static Scanner reader = new Scanner(System.in);

  public static int safeReadInt() {
    try {
      return Integer.parseInt(reader.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Please input an integer number!");
      return safeReadInt();
    }
  }

  public static double safeReadDouble() {
    try {
      return Double.parseDouble(reader.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Please input a number!");
      return safeReadDouble();
    }
  }

  public static String readLine() {
    return reader.nextLine();
  }

  public static void closeReader() {
    reader.close();
  }
}
