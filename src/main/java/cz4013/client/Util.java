package cz4013.client;

import java.util.Scanner;

/**
 * This class provides utility to read user input via stdin.
 */
public class Util {
  private static Scanner reader = new Scanner(System.in);

  /**
   * Reads an integer from stdin.
   * This method will attempt to read user input until it encounters
   * a valid integer.
   *
   * @return an integer from stdin
   */
  public static int safeReadInt() {
    try {
      return Integer.parseInt(reader.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Please input an integer number!");
      return safeReadInt();
    }
  }

  /**
   * Reads a double from stdin.
   * This method will attempt to read user input until it encounters
   * a valid double.
   *
   * @return an double from stdin
   */
  public static double safeReadDouble() {
    try {
      return Double.parseDouble(reader.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Please input a number!");
      return safeReadDouble();
    }
  }

  /**
   * Reads a line from stdin.
   *
   * @return a line from stdin
   */
  public static String readLine() {
    return reader.nextLine();
  }

  /**
   * Closes the associated reader. Used when exiting from main execution.
   */
  public static void closeReader() {
    reader.close();
  }
}
