import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

public final class InputManager {
	public static Scanner scanner = new Scanner(System.in);

	public static String getString() {
		return scanner.next();
	}

	public static BigDecimal getBigDecimal() {
		return scanner.nextBigDecimal();
	}

	public static BigInteger getBigInteger() {
		return scanner.nextBigInteger();
	}

	public static boolean getBoolean() {
		return scanner.nextBoolean();
	}

	public static byte getByte() {
		return scanner.nextByte();
	}

	public static double getDouble() {
		return scanner.nextDouble();
	}

	public static float getFloat() {
		return scanner.nextFloat();
	}

	public static int getInt() {
		return scanner.nextInt();
	}

	public static long getLong() {
		return scanner.nextLong();
	}

	public static short getShort() {
		return scanner.nextShort();
	}

	public static void pressEnterToContinue() {
		System.out.println("按回车键继续...");
		scanner.nextLine();
		scanner.nextLine();
	}

	public static void close() {
		scanner.close();
	}
}
