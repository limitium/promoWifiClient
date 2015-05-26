package me.loc2.loc2me.ui.md;

import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.loc2.loc2me.R;

public class ColorGenerator {

    private static List<Integer> colors;
    private static Iterator<Integer> iter;

    static {
        colors = new ArrayList<>();
        colors.add(R.color.darkblue);
        colors.add(R.color.darkteal);
        colors.add(R.color.darkcyan);
        colors.add(R.color.darkgreen);
        colors.add(R.color.darklightblue);
        iter = Iterators.cycle(colors);
    }

    public static Integer getNextCardColor() {
        return iter.next();
    }
}
