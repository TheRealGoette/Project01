
import java.util.ArrayList;
import java.util.HashMap;

/**
 * states in how many documents a specific word occures
 * 
 * @variable occurence - contains the int values for every word in the
 *           dictionary at the according index as int values
 * @author marcogoette
 *
 */
public class InvertedIndex {
	private int[] occurence;				// Field containing the number of texts the word occurs in at the same index as the word in the dictioanry
	private Dictionary dictionary;			// Dictionary serving as the base of the Inverted Index
	private HashMap<String, ArrayList<String>> invertedIndex;

	
	/**
	 * inizializes the occurence field
	 * 
	 * @param dictionary
	 *            - to initialze the dictionary variable
	 */
	public InvertedIndex(Dictionary dictionary) {
		this.dictionary = dictionary;				// implementing the dictioanry
		occurence = new int[dictionary.length()];	// implementing the occurence field with dictionary's length
		invertedIndex = new HashMap<>();
	}

	/**
	 * checks if the words of the dictionary appeare in the wordList and increases
	 * the count in occurence if wordList contains word
	 * 
	 * @param wordList
	 *            - the ArrayList of a DocumentModel
	 */
	public void addRelation(ArrayList<String> wordList, String docName) {
		int count = 0;
		ArrayList<String> x;
		for (String word : dictionary.get_wordList()) {
			if (wordList.contains(word)) {
				occurence[count]++;							// increasing the occurence of a word
				if (invertedIndex.containsKey(word)) {		// constructing the inverted - not necessary for the calculations but to show on the console
					x = invertedIndex.get(word);
					x.add(docName);
				} else
					x = new ArrayList<>();
					x.add(docName);
				invertedIndex.put(word, x);
			}
			count++;
		}
	}

	/**
	 * returns int value representing in how many documents the String parameter
	 * occurs
	 * 
	 * @param word
	 * @return -1 if the word is not found in the dictionary
	 */
	public int getDocumentFrequency(String word) {
		return occurence[dictionary.get_wordList().indexOf(word)];
	}

	public int[] getOccurence() {
		return occurence;
	}

	public void setOccurence(int[] occurence) {
		this.occurence = occurence;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	/**
	 * necessary for the "debug" command
	 */
	public void printInvertedIndex() {
		for (String key : invertedIndex.keySet()) {
			System.out.println(key + ": " + invertedIndex.get(key).toString());
		}
	}
}
