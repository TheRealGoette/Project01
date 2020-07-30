
import java.util.ArrayList;

/**
 * creates a unique ArrayList containing unique String values
 * @author marcogoette
 *
 */
public class Dictionary {

	private ArrayList<String> _wordList; // <String> added

	public Dictionary() {
		_wordList = new ArrayList<String>();
	}

	/**
	 * adds a unique String value to the ArrayList
	 * 
	 * @param word
	 */
	public void addWord(String word) {
		if (_wordList.contains(word) == false)
			_wordList.add(word);

		// sort list again --> done once when dictionary is finished for efficiency purposes
	}

	/**
	 * Sorts the dictionary using the natural ordering
	 */
	public void sortDictionary() {
		_wordList.sort(null);
	}

	/**
	 * Checks if the _wordList contains the String parameter
	 * 
	 * @param word
	 * @return boolean value - true it the _wordList contains the parameter
	 */
	public boolean containsWord(String word) {
		return _wordList.contains(word);
	}

	/**
	 * 
	 * @return int value representing the size of the _wordList
	 */
	public int length() {
		return _wordList.size();
	}

	/**
	 * returns the index of the String parameter if contained in the _wordList if
	 * not returns -1
	 * 
	 * @param word
	 * @return int index
	 */
	public int indexOfWord(String word) {
		return _wordList.indexOf(word);
	}

	public ArrayList<String> get_wordList() {
		return _wordList;
	}

	public void set_wordList(ArrayList<String> _wordList) {
		this._wordList = _wordList;
	}
}
