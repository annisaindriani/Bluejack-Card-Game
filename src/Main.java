import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Main {
	Scanner scan = new Scanner(System.in);
	File dataCollection = new File("SuperS3cr3tFile.dat");
	ArrayList<User> listUser = new ArrayList<>();
	Random rand = new Random();

	String username;
	String password;
	int point = 100;
	int choice;

	private static SecretKeySpec secretKey;
	private static byte[] key;
	final String mySecretKey = "Us3rS3CR3TD4T4SS";

	public static void setKey(final String theKey) {
		MessageDigest sha = null;
		try {
			key = theKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String encryption(final String strToEncrypt, final String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public static String decryption(final String strToDecrypt, final String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public void login() {
		cls();
		System.out.print("Input username : ");
		username = scan.nextLine();

		System.out.print("Input password : ");
		password = scan.nextLine();

		readData();
		mergeSort(0, listUser.size() - 1);

		for (int i = 0; i < listUser.size(); i++) {
			if (username.compareTo(listUser.get(i).getUsername()) == 0) {
				if (password.compareTo(listUser.get(i).getPassword()) == 0) {
					System.out.println("[*] Successfully logged in");
					System.out.print("Press enter to continue...");
					scan.nextLine();
					choice = i;
					gameMenu();
					return;
				}
			}
		}

		System.out.println("[!] Invalid Username and Password");
		System.out.print("Press enter to continue...");
		scan.nextLine();

	}

	public void gamePlay() {
		cls();
		String symbol[] = { "♥", "♠", "♦", "♣" };
		String spcSymbol[] = { "K", "Q", "J", "A" };
		Integer card1;
		Integer card2;
		Integer card3;
		Integer pNum[] = new Integer[5];
		Integer dNum[] = new Integer[5];
		String pCard[] = new String[10];
		String dCard[] = new String[10];
		int idxP = 0;
		int idxD = 0;
		int pPoint = 0;
		int dPoint = 0;
		int bet;
		int totalBet;

		while (true) {
			System.out.print("Input your bet [max " + listUser.get(choice).getScore() + "]: ");
			bet = scan.nextInt();
			scan.nextLine();
			totalBet = bet * 2;

			if (bet >= 1 && bet <= listUser.get(choice).getScore())
				break;

			System.out.println("[!] Input must be between 1 and " + listUser.get(choice).getScore());
		}

		while (true) {
			for (idxP = 0; idxP < 2; idxP++) {
				if (rand.nextInt(2) == 0) {
					String temp = spcSymbol[rand.nextInt(spcSymbol.length)];
					if (temp.compareTo("A") == 0) {
						pNum[idxP] = 11;
						pCard[idxP] = temp.concat(symbol[rand.nextInt(symbol.length)]);
					} else {
						pNum[idxP] = 10;
						pCard[idxP] = temp.concat(symbol[rand.nextInt(symbol.length)]);
					}
				} else {
					do {
						pNum[idxP] = rand.nextInt(11);
					} while (pNum[idxP] < 2);

					pCard[idxP] = pNum[idxP].toString().concat(symbol[rand.nextInt(symbol.length)]);
				}

			}

			if (pCard[0].compareTo(pCard[1]) != 0) {
				break;
			}
		}

		pPoint = pNum[0] + pNum[1];

		while (true) {
			for (idxD = 0; idxD < 2; idxD++) {
				if (rand.nextInt(2) == 0) {
					String temp = spcSymbol[rand.nextInt(spcSymbol.length)];
					if (temp.compareTo("A") == 0) {
						dNum[idxD] = 11;
						dCard[idxD] = temp.concat(symbol[rand.nextInt(symbol.length)]);
					} else {
						dNum[idxD] = 10;
						dCard[idxD] = temp.concat(symbol[rand.nextInt(symbol.length)]);
					}
				} else {
					do {
						dNum[idxD] = rand.nextInt(11);
					} while (dNum[idxD] < 2);

					dCard[idxD] = dNum[idxD].toString().concat(symbol[rand.nextInt(symbol.length)]);
				}

			}

			if (dCard[0].compareTo(dCard[1]) != 0) {
				break;
			}
		}

		dPoint = dNum[0] + dNum[1];

		while (true) {
			System.out.println("Dealer Card :");
			for (int i = 0; i < idxD; i++) {
				if (dCard[i] != null) {
					if (dCard[i + 1] == null) {
						System.out.println("??");
					} else {
						System.out.printf("%-3s | ", dCard[i]);
					}
				}
			}

			System.out.println("Player Card :");
			for (int i = 0; i < idxP; i++) {
				if (pCard[i + 1] == null) {
					System.out.printf("%-3s\n", pCard[i]);
				} else {
					System.out.printf("%-3s | ", pCard[i]);
				}
			}

			System.out.println("====================");
			System.out.println("| Choose your move |");
			System.out.println("====================");
			System.out.println("| 1. Hit           |");
			System.out.println("| 2. Stand         |");
			System.out.println("====================");
			System.out.print("Choose[1 - 2] >> ");
			int choose = scan.nextInt();
			scan.nextLine();

			int flag = 0;
			switch (choose) {
			case 1:
				if (rand.nextInt(2) == 0) {
					String temp = spcSymbol[rand.nextInt(spcSymbol.length)];
					if (temp.compareTo("A") == 0) {
						pNum[idxP] = 11;
						pCard[idxP] = temp.concat(symbol[rand.nextInt(symbol.length)]);
					} else {
						pNum[idxP] = 10;
						pCard[idxP] = temp.concat(symbol[rand.nextInt(symbol.length)]);
					}
				} else {
					do {
						pNum[idxP] = rand.nextInt(11);
					} while (pNum[idxP] < 2);

					pCard[idxP] = pNum[idxP].toString().concat(symbol[rand.nextInt(symbol.length)]);
				}
				pPoint += pNum[idxP];
				idxP++;
				break;
			case 2:
				if (dPoint < 17) { // hit
					if (rand.nextInt(2) == 0) {
						String temp = spcSymbol[rand.nextInt(spcSymbol.length)];
						if (temp.compareTo("A") == 0) {
							dNum[idxD] = 11;
							dCard[idxD] = temp.concat(symbol[rand.nextInt(symbol.length)]);
						} else {
							dNum[idxD] = 10;
							dCard[idxD] = temp.concat(symbol[rand.nextInt(symbol.length)]);
						}
					} else {
						do {
							dNum[idxD] = rand.nextInt(11);
						} while (dNum[idxD] < 2);

						dCard[idxD] = dNum[idxD].toString().concat(symbol[rand.nextInt(symbol.length)]);
					}
					dPoint += dNum[idxP];
					idxD++;
				} else {
					System.out.println("Dealer Card :");
					for (int i = 0; i < dCard.length; i++) {
						if (dCard[i] != null) {
							if (dCard[i + 1] == null) {
								System.out.printf("%-3s\n", dCard[i]);
							} else {
								System.out.printf("%-3s | ", dCard[i]);
							}
						}
					}

					System.out.println("Player Card :");
					for (int i = 0; i < idxP; i++) {
						if (pCard[i + 1] == null) {
							System.out.printf("%-3s\n", pCard[i]);
						} else {
							System.out.printf("%-3s | ", pCard[i]);
						}
					}

					System.out.println("======================================");
					if (pPoint == dPoint) { // tie
						System.out.println("[!] It's tie, you got nothing");
						System.out.print("Press enter to continue...");
						scan.nextLine();
					} else if (pPoint > 21 && dPoint > 21) {
						if (pPoint < dPoint) { // player menang
							int result = listUser.get(choice).getScore() + totalBet;
							listUser.get(choice).setScore(result);
							System.out.println("[!] The Dealer busted, you won " + totalBet + " point(s)");
							System.out.print("Press enter to continue...");
							scan.nextLine();
						} else {
							int result = listUser.get(choice).getScore() - bet;
							listUser.get(choice).setScore(result);
							System.out.println("[!] " + listUser.get(choice).getUsername() + " busted, you lose " + bet
									+ " point(s)");
							System.out.print("Press enter to continue...");
							scan.nextLine();
						}
					} else if (pPoint < 21 && dPoint < 21) {
						if (pPoint > dPoint) { // player menang
							int result = listUser.get(choice).getScore() + totalBet;
							listUser.get(choice).setScore(result);
							System.out.println("[!] The Dealer busted, you won " + totalBet + " point(s)");
							System.out.print("Press enter to continue...");
							scan.nextLine();
						} else {
							int result = listUser.get(choice).getScore() - bet;
							listUser.get(choice).setScore(result);
							System.out.println("[!] " + listUser.get(choice).getUsername() + " busted, you lose " + bet
									+ " point(s)");
							System.out.print("Press enter to continue...");
							scan.nextLine();
						}
					} else {
						if (dPoint > 21) { // player menang
							int result = listUser.get(choice).getScore() + totalBet;
							listUser.get(choice).setScore(result);
							System.out.println("[!] The Dealer busted, you won " + totalBet + " point(s)");
							System.out.print("Press enter to continue...");
							scan.nextLine();
						} else {
							int result = listUser.get(choice).getScore() - bet;
							listUser.get(choice).setScore(result);
							System.out.println("[!] " + listUser.get(choice).getUsername() + " busted, you lose " + bet
									+ " point(s)");
							System.out.print("Press enter to continue...");
							scan.nextLine();
						}
					}
					flag = 1;
				}
				break;
			default:
				System.out.println("[!] Input must be between 1 and 2");
				break;
			}
			if (flag == 1)
				break;
		}
	}

	public void viewScore() {
		cls();
//		readData();
		mergeSort(0, listUser.size() - 1);

		System.out.println("===========================");
		System.out.println("|        HIGHSCORE        |");
		System.out.println("===========================");
		System.out.println("| Username   | Point      |");
		System.out.println("===========================");

		for (int i = 0; i < listUser.size(); i++) {
			System.out.printf("| %-10s | %-10d |\n", listUser.get(i).getUsername(), listUser.get(i).getScore());
			if (username.compareTo(listUser.get(i).getUsername()) == 0) {
					choice = i;
			}
		}

		System.out.println("===========================");
		System.out.print("Press enter to continue...");
		scan.nextLine();

	}

	public void mergeSort(int left, int right) {
		if (left < right) {
			int mid = left + (right - left) / 2;

			mergeSort(left, mid);
			mergeSort(mid + 1, right);

			merge(left, mid, right);
		}
	}

	public void merge(int left, int mid, int right) {
		int nLeft = mid - left + 1;
		int nRight = right - mid;

		User[] arrL = new User[nLeft];
		User[] arrR = new User[nRight];

		for (int i = 0; i < nLeft; i++) {
			arrL[i] = listUser.get(left + i);
		}

		for (int i = 0; i < nRight; i++) {
			arrR[i] = listUser.get(mid + 1 + i);
		}

		int idxLeft = 0;
		int idxRight = 0;
		int idxCurrent = left;

		while (idxLeft < nLeft && idxRight < nRight) {
			if (arrL[idxLeft].getScore() >= arrR[idxRight].getScore()) {
				listUser.set(idxCurrent, arrL[idxLeft]);
				idxLeft++;
			} else {
				listUser.set(idxCurrent, arrR[idxRight]);
				idxRight++;
			}

			idxCurrent++;
		}

		while (idxLeft < nLeft) {
			listUser.set(idxCurrent, arrL[idxLeft]);
			idxLeft++;
			idxCurrent++;
		}

		while (idxRight < nRight) {
			listUser.set(idxCurrent, arrR[idxRight]);
			idxRight++;
			idxCurrent++;
		}
	}

	public void gameMenu() {
		while (true) {
			cls();
			System.out.println("|===================|");
			System.out.printf("| Hello, %-10s |\n", listUser.get(choice).getUsername());
			System.out.printf("| Point : %-9d |\n", listUser.get(choice).getScore());
			System.out.println("====================|");
			System.out.println("| 1. Play           |");
			System.out.println("| 2. High Score     |");
			System.out.println("| 3. Save & Logout  |");
			System.out.println("=====================");
			System.out.print("Choose[1 - 3] >> ");

			int menu = scan.nextInt();
			scan.nextLine();

			int flag = 0;

			switch (menu) {
			case 1:
				gamePlay();
				break;
			case 2:
				viewScore();
				break;
			case 3:
				for (int i = 0; i < listUser.size(); i++) {
					if (i == 0) {
						String originalString = serializeData(listUser.get(i).getUsername(),
								listUser.get(i).getPassword(), listUser.get(i).getScore());
						String encryptedString = encryption(originalString, mySecretKey);

						rewriteData(encryptedString + "\n");
					} else {
						String originalString = serializeData(listUser.get(i).getUsername(),
								listUser.get(i).getPassword(), listUser.get(i).getScore());
						String encryptedString = encryption(originalString, mySecretKey);

						insertData(encryptedString + "\n");
					}
				}
				flag = 1;
				break;
			default:
				break;
			}

			if (flag == 1)
				break;
		}
	}

	public void register() {
		cls();

		while (true) {
			System.out.print("Input username : ");
			username = scan.nextLine();

			if (username.length() >= 4 && username.length() <= 10)
				break;

			System.out.println("[!] Username must be between 4 and 10 characters");
		}

		while (true) {
			System.out.print("Input password : ");
			password = scan.nextLine();

			if (password.length() >= 8 && password.length() <= 16) {
				char check[] = password.toCharArray();

				int count = 0;

				for (char c : check) {
					if (Character.isDigit(c)) {
						count++;
					}
				}

				if (count >= 0)
					break;
			}

			System.out.println("[!] Password must be between 8 and 16 characters");
			System.out.println("[!] Password must be alphanumeric");
		}

		try {
			if (!dataCollection.exists()) {
				dataCollection.createNewFile();
			}
		} catch (Exception e) {

		}

		String originalString = serializeData(username, password, point);
		String encryptedString = encryption(originalString, mySecretKey);

		insertData(encryptedString + "\n");
		System.out.println("[*] Successfully registered an account");
		System.out.print("Press enter to continue...");
		scan.nextLine();

	}

	String serializeData(String username, String password, int point) {
//		return username + "#" + password + "#" + point + "\n";
		return username + "#" + password + "#" + point;
	}

	String[] deserializeData(String serializeData) {
		return serializeData.split("#");
	}

	public void insertData(String data) {
		try {
			FileWriter fw = new FileWriter(dataCollection, true);
			fw.write(data);
			fw.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void rewriteData(String data) {
		try {
			FileWriter fw = new FileWriter(dataCollection, false);
			fw.write(data);
			fw.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void readData() {
		listUser.clear();

		try {
			Scanner scanFile = new Scanner(dataCollection);

			while (scanFile.hasNextLine()) {
				String result = scanFile.nextLine();

				String resultCar[] = deserializeData(decryption(result, mySecretKey));

				if (resultCar.length != 0) {
					String username = resultCar[0];
					String password = resultCar[1];
					String score = resultCar[2];

					User newUser = new User(username, password, Integer.parseInt(score));
					listUser.add(newUser);

				}
			}

			scanFile.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void cls() {
		for (int i = 0; i < 25; i++) {
			System.out.println();
		}
	}

	public Main() {
		// TODO Auto-generated constructor stub
		while (true) {
			cls();
			System.out.println("=====================");
			System.out.println("| ♥   BlueJack    ♠ |");
			System.out.println("| ♦   Card Game   ♣ |");
			System.out.println("=====================");
			System.out.println("| 1. Login          |");
			System.out.println("| 2. Register       |");
			System.out.println("| 3. Exit           |");
			System.out.println("=====================");
			System.out.print("Choose[1 - 3] >> ");

			int menu = scan.nextInt();
			scan.nextLine();

			int flag = 0;

			switch (menu) {
			case 1:
				login();
				break;
			case 2:
				register();
				break;
			case 3:
				flag = 1;
				break;
			default:
				break;
			}

			if (flag == 1)
				break;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}
}