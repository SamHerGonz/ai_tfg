package collections;

import java.util.ArrayList;
import java.util.Collections;

public class PosList extends ArrayList<Double> {
    ArrayList<Integer> index = new ArrayList<>();

    @Override
    public boolean add(Double aDouble) {
        return add(aDouble, false);
    }

    public boolean add(Double aDouble, boolean replace) {
        boolean b = super.add(aDouble);
        if (b) {
            if (replace) {
                if (index.isEmpty()) {
                    index.add(0);
                }
                else {
                    int n = -1;
                    for (int i = 0; i < Collections.max(index); i++) {
                        if (index.contains(i)) {
                            n = i;
                            break;
                        }
                    }
                    b = n == -1 ? index.add(n) : index.add(Collections.max(index) + 1);
                }
            }
            else if (index.isEmpty()) {
                index.add(0);
            }
            else {
                b = index.add(Collections.max(index) + 1);
            }
        }
        return b;
    }

    @Override
    public Double remove(int n) {
        Double d = super.remove(index.indexOf(n));
        index.remove((Integer) n);
        return d;
    }

    public void addValue(int i, Double val){
        set(i, get(i) + val);
    }
}
