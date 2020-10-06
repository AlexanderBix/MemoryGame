import java.util.Arrays;

public class CardProperties {
    // Variables to store data of a card
    String path; // Directory of Image
    int index; // Index in Grid Layout
    boolean isActive; // If card is flipped
    boolean isMatched; // If card has been matched

    // Adds entry to the Card Properties List
    public static CardProperties[] addEntry(CardProperties[] list, CardProperties value) {
        CardProperties[] newList = Arrays.copyOf(list, list.length + 1);
        newList[newList.length - 1] = value;
        return newList;
    }

    // Finds array entry based on index
    public static int findEntry(int index, CardProperties[] cardIndex) {
        for(int i = 0; i < cardIndex.length; i++) {
            if(cardIndex[i].index == index) {
                return i;
            }
        }
        return -1;
    }

    // Determines if a match has been made
    public static boolean findIfMatched(CardProperties entry, CardProperties[] cardIndex) {
        for(int i = 0; i < cardIndex.length; i++) {
            // If two different entries are the same image
            if(cardIndex[i].path.equals(entry.path) && i != entry.index) {
                if(cardIndex[i].isActive && entry.isActive) {
                    return true;
                }
            }
        }
        return false;
    }

    // Determines if a match has been made
    public static int findIfMatchedValue(CardProperties entry, CardProperties[] cardIndex) {
        for(int i = 0; i < cardIndex.length; i++) {
            // If two different entries are the same image
            if(cardIndex[i].path.equals(entry.path) && i != entry.index) {
                if(cardIndex[i].isActive && entry.isActive) {
                    return i;
                }
            }
        }
        return -1;
    }

}
