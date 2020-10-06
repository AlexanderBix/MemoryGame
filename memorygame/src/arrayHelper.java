import java.util.Arrays;

public class arrayHelper {

    // Pushes new value onto array
    public static String[] addArray(String[] list, String value) {
        String[] newList = Arrays.copyOf(list, list.length + 1);
        newList[newList.length - 1] = value;
        return newList;
    }

    // Pops array value at a particular index
    public static String[] removeArray(String[] list, int index) {
        String[] newList = {};
        for(int i = 0; i < list.length; i++) {
            if (i != index) newList = addArray(newList, list[i]);
        }
        return newList;
    }

}
