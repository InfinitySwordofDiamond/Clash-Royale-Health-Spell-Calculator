import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	
	static Scanner input = new Scanner(System.in);
	static int spellLevel = 10;
	static int mirrorSpellLevel;
	
	static final int[] rocket = {0, 0, 700, 770, 847, 931, 1022, 1120, 1232, 1351, 1484, 1631, 1792, 1967, 2163, 2373};
	static final int[] lightning = {0, 0, 0, 0, 0, 660, 726, 798, 877, 963, 1056, 1161, 1273, 1399, 1537, 1689};
	static final int[] poison = {0, 0, 0, 0, 0, 456, 496, 544, 600, 664, 728, 800, 880, 960, 1056, 1160};
	static final int[] fireball = {0, 0, 325, 357, 393, 432, 474, 520, 572, 627, 689, 757, 832, 913, 1004, 1101};
	static final int[] arrows = {114, 156, 174, 189, 210, 228, 252, 276, 303, 333, 366, 402, 444, 486, 534, 588};
	static final int[] theLog = {0, 0, 0, 0, 0, 0, 0, 0, 240, 264, 290, 319, 350, 384, 422, 463};
	static final int[] earthquake = {0, 0, 117, 126, 141, 153, 168, 186, 204, 225, 246, 270, 297, 327, 360, 396};
	static final int[] zapOrGiantSnowball = {75, 82, 90, 99, 109, 120, 132, 144, 159, 174, 192, 210, 231, 254, 279, 306};
	static final int[] tornado = {0, 0, 0, 0, 0, 106, 116, 128, 140, 154, 169, 186, 204, 224, 246, 271};
	static final int[] freeze = {0, 0, 0, 0, 0, 72, 79, 87, 95, 105, 115, 126, 138, 152, 167, 184};
	
	static int[] spells;
	static int[] mirrorSpells;
	static int[] allSpells;
	
	static ArrayList<Integer> spellDamage = new ArrayList<Integer>();
	static ArrayList<Integer> spellDuplicates = new ArrayList<Integer>();
	static ArrayList<String> spellCombos = new ArrayList<String>();

	public static void main(String[] args) {
		System.out.println("||Clash Royale Health Spell Calculator||");
		boolean loop = true;
		
		while (loop) {
			loop = spellCombo();
		}
	}
	
	public static boolean spellCombo() {
		if (yesNo("\nTournament Level Spell? ")) {
			spellLevel = 10;
		}
		
		else {
			spellLevel = typeInt("\nSpell Level: ", "\nSpell Level must be in between Level 1 and Level 16", 1, 16) - 1;
		}
		
		spells = new int[]{rocket[spellLevel], lightning[spellLevel], poison[spellLevel], fireball[spellLevel], arrows[spellLevel], theLog[spellLevel], earthquake[spellLevel], zapOrGiantSnowball[spellLevel], tornado[spellLevel], freeze[spellLevel]};
		boolean mirror = false;
		mirrorSpellLevel = -1;
		
		if (spellLevel > 4 && spellLevel < 14) {
			if (yesNo("\nInclude Mirror Spells? ")) {
				mirrorSpellLevel = spellLevel + 2;
				mirrorSpells = new int[]{rocket[mirrorSpellLevel], lightning[mirrorSpellLevel], poison[mirrorSpellLevel], fireball[mirrorSpellLevel], arrows[mirrorSpellLevel], theLog[mirrorSpellLevel], earthquake[mirrorSpellLevel], zapOrGiantSnowball[mirrorSpellLevel], tornado[mirrorSpellLevel], freeze[mirrorSpellLevel]};
				allSpells = new int[spells.length + mirrorSpells.length];
				mirror = true;
				
				for(int i = 0; i < spells.length; i++) {
					allSpells[i] = spells[i];
				}
				
				for(int i = 0; i < mirrorSpells.length; i++) {
					allSpells[spells.length + i] = mirrorSpells[i];
				}
			}
		}
		
		int initialHealth = 0;
		int finalHealth = 0;
		
		while (true) {
			initialHealth = typeInt("\nTroop Initial Health: ", "\nInitial Health cannot be a negative number", 0, Integer.MAX_VALUE);
			finalHealth = typeInt("\nTroop Final Health: ", "\nFinal Health cannot be a negative number", 0, Integer.MAX_VALUE);
			
			if (initialHealth <= finalHealth) {
				System.out.println("\nFinal Health cannot be greater than or equal to the Initial Health");
			}
			
			else
				break;
		}
		
		boolean found = false;
		System.out.println("\nFinding Spell Combos...");
		
		if (initialHealth - finalHealth < freeze[spellLevel]) {
			found = false;
		}
		
		else {
			for (int i = 1; i < 50; i++) {
				int health = 0;
				
				if (mirror) {
					found = detectedSpells(initialHealth, finalHealth, health, i, mirror, allSpells);	
				}
				
				else {
					found = detectedSpells(initialHealth, finalHealth, health, i, mirror, spells);
				}
				
				if (found) {
					break;
				}
			}
		}
		
		if (!found) {
			System.out.println("\nNo Spell Combos Found");
		}
		
		for (int remove = 0; remove < spellDamage.size(); remove++) {
			if (spellDamage.get(remove) == 0) {
				spellDamage.remove(remove);
			}
		}
		
		System.out.println();
		for (int i = 0; i < spellCombos.size(); i++) {
			System.out.println("Spell #" + (i + 1) + ": " + spellCombos.get(i));
		}
		
		spellCombos.clear();
		
		return yesNo("\nRestart? ");
	}
	
	public static boolean detectedSpells(int initialHealth, int finalHealth, int health, int spellCount, boolean mirror, int[] spells) {
		for (int i = 0; i < 1000000; i++) {
			for (int j = 0; j < spellCount; j++) {
				spellDamage.add(spells[(int) (Math.random() * spells.length)]);
				
				health += spellDamage.get(j);
				
				if (initialHealth - health == finalHealth) {	
					Collections.sort(spellDamage);
					Collections.reverse(spellDamage);
					convertToSpell(spellDamage,spellLevel,mirrorSpellLevel);
					return true;
				}
			}
			
			spellDamage.clear();
			health = 0;
		}
		
		return false;
	}
	
	public static void convertToSpell(ArrayList<Integer> sDamage, int spellLevel, int mirrorSpellLevel) {
		for (int damage : sDamage) {
			if (damage == rocket[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Rocket");
			}
			
			else if (damage == lightning[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Lightning");
			}

			else if (damage == poison[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Poison");
			}

			else if (damage == fireball[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Fireball");
			}

			else if (damage == arrows[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Arrows");
			}

			else if (damage == theLog[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Log");
			}

			else if (damage == earthquake[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Earthquake");
			}

			else if (damage == zapOrGiantSnowball[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Zap or Giant Snowball");
			}

			else if (damage == tornado[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Tornado");
			}

			else if (damage == freeze[spellLevel]) {
				spellCombos.add("Level " + (spellLevel + 1) + " Freeze");
			}

			else if (mirrorSpellLevel != -1) {
				if (damage == rocket[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Rocket");
				}
				
				else if (damage == lightning[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Lightning");
				}

				else if (damage == poison[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Poison");
				}

				else if (damage == fireball[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Fireball");
				}

				else if (damage == arrows[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Arrows");
				}

				else if (damage == theLog[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Log");
				}

				else if (damage == earthquake[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Earthquake");
				}

				else if (damage == zapOrGiantSnowball[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Zap or Giant Snowball");
				}

				else if (damage == tornado[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Tornado");
				}

				else if (damage == freeze[mirrorSpellLevel]) {
					spellCombos.add("Level " + (mirrorSpellLevel + 1) + " Freeze");
				}
			}
		}
	}

	public static boolean yesNo(String text) {
		while (true) {
			System.out.print(text);
			String answer = input.next().toLowerCase();
			
			if (answer.contains("yes") || answer.equals("y")) {
				return true;
			}
			
			else if (answer.contains("no") || answer.equals("n")) {
				return false;
			}
			
			else
				System.out.println("\nPlease type in (Yes or No)");
		}
	}
	
	public static int typeInt(String question, String warn, int lowerBounds, int upperBounds) {
		while (true) {
			int answer = 0;
			
			try {
				System.out.print(question);
				answer = input.nextInt();
				
				if (answer < lowerBounds || answer > upperBounds) {
					System.out.println(warn);
				}
				
				else
					return answer;
			}
			
			catch (Exception e) {
				System.out.println("\nInvalid Input");
				input.next();
			}
		}
	}
}
