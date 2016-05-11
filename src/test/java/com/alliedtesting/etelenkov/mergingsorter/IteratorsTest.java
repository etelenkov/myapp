package com.alliedtesting.etelenkov.mergingsorter;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static com.alliedtesting.etelenkov.mergingsorter.Iterators.sortingCombiner;
import static org.testng.Assert.assertEquals;

public class IteratorsTest {

    /**
     * Test for sortingCombiner method with TestNG DataProvider.
     *
     * @param comparator   Comparator of elements of type <T>.
     * @param expSeq       Expecting resulting sequence of elements of type <T>
     *                     of sortingCombiner method.
     * @param listOfIncSeq List of lists of incoming sequences of elements of type <T>
     *                     sorted with comparator rules.
     * @param testNo       Test number from DataProvider (just for convenient usage)
     * @param exception    Expected Exception type which is a result of sortingCombiner call
     *                     (null - if no exception expected)
     * @param <T>          Type of elements of sequence.
     */
    @Test(dataProvider = "sortingCombinerTestDataProvider")
    public <T> void sortingCombinerTest(Comparator<? super T> comparator, List<T> expSeq, List<List<T>> listOfIncSeq, int testNo, Class<Exception> exception) {
        // Create collection of iterators for sortingCombiner method
        List<Iterator<T>> arrOfSeqIterators = new ArrayList<>();
        for (List<T> e : listOfIncSeq) arrOfSeqIterators.add(e.iterator());


        List<T> res = null; // Get resulting iterator

        // Try to catch needed Exception
        Exception catchedException = null;
        try {
            res = Iterators.toList(sortingCombiner(arrOfSeqIterators, comparator));
        } catch (Exception e) {
            catchedException = e;
        }


//        try {

//        } catch (Exception e) {
//
//            // If some Exception catched - assert it with the needed one
//            assertEquals((Object) e.getClass(), (Object) exc.getClass(), "Iterators.sortingCombinerTest #" + testNo + " FAILED!");
//
//            // Print stack trace if Exception is not the one expected
//            if (!e.getClass().equals(exc.getClass())) e.printStackTrace();
//
//            return; // !terminate
//        }

        if (exception != null) {
            // TODO: 11.05.2016 Check Exception catched by class... 
            assertEquals((Object) catchedException, (Object) exception,
                    "Iterators.sortingCombinerTest #" + testNo + " FAILED on Exception expected test!");
        } else {
            // Check the result with expect
            assertEquals((Object) res, (Object) expSeq, "Iterators.sortingCombinerTest #" + testNo + " FAILED!");
        }
    }

    /**
     * DataProvider for "sortingCombinerTest"
     *
     * @return DataProvider for sortingCombinerTest
     */
    @DataProvider(name = "sortingCombinerTestDataProvider")
    public Object[][] sortingCombinerTestDataProvider() throws FileNotFoundException {
        int testNo = 0; // initialize test number
        return new Object[][]{
                { // --- Integer 1-1 (input order 1) ---
                        (Comparator<Integer>) (a, b) -> a - b,
                        Arrays.asList(0, 0, 0, 1, 2, 3, 3, 4, 5, 6, 9, 10, 12, 15),
                        Arrays.asList(
                                Arrays.asList(0, 1, 2, 3, 4),
                                Arrays.asList(0, 3, 6, 9, 12, 15),
                                Arrays.asList(0, 5, 10)
                        ),
                        ++testNo,
                        null
                },
                { // --- Integer 1-2  (input order 2) ---
                        (Comparator<Integer>) (a, b) -> a - b,
                        Arrays.asList(0, 0, 0, 1, 2, 3, 3, 4, 5, 6, 9, 10, 12, 15),
                        Arrays.asList(
                                Arrays.asList(0, 1, 2, 3, 4),
                                Arrays.asList(0, 5, 10),
                                Arrays.asList(0, 3, 6, 9, 12, 15)
                        ),
                        ++testNo,
                        null
                },
                { // --- Integer 1-3  (input order 3) ---
                        (Comparator<Integer>) (a, b) -> a - b,
                        Arrays.asList(0, 0, 0, 1, 2, 3, 3, 4, 5, 6, 9, 10, 12, 15),
                        Arrays.asList(
                                Arrays.asList(0, 5, 10),
                                Arrays.asList(0, 3, 6, 9, 12, 15),
                                Arrays.asList(0, 1, 2, 3, 4)
                        ),
                        ++testNo,
                        null
                },
                { // --- Integer 2-1 (One of inputs is empty) ---
                        (Comparator<Integer>) (a, b) -> a - b,
                        Arrays.asList(0, 0, 3, 5, 6, 9, 10, 12, 15),
                        Arrays.asList(
                                Arrays.asList(),
                                Arrays.asList(0, 3, 6, 9, 12, 15),
                                Arrays.asList(0, 5, 10)
                        ),
                        ++testNo,
                        null
                },
                { // --- Integer 3 (always ZERO comparator) ---
                        (Comparator<Integer>) (a, b) -> 0,
                        Arrays.asList(0, 1, 2, 3, 4, 0, 3, 6, 9, 12, 15, 0, 5, 10),
                        Arrays.asList(
                                Arrays.asList(0, 1, 2, 3, 4),
                                Arrays.asList(0, 3, 6, 9, 12, 15),
                                Arrays.asList(0, 5, 10)
                        ),
                        ++testNo,
                        null
                },
                { // --- Integer 4 (with String - ClassCastException should be thrown) ---
                        (Comparator<Integer>) (a, b) -> a - b,
                        Arrays.asList(0, 0, 0, 1, 2, 3, 3, 4, 5, 6, 9, 10, 12, 15),
                        Arrays.asList(
                                Arrays.asList(0, 1, 2, 3, 4),
                                Arrays.asList("0", "3", "6", "9", "12", "15"),
                                Arrays.asList(0, 5, 10)
                        ),
                        ++testNo,
                        new ClassCastException()
                },
                { // --- String 1-1 ---
                        (Comparator<String>) String::compareTo,
                        Arrays.asList("A", "B", "B", "C", "F", "Q", "S", "V", "W", "X", "Y", "Z"),
                        Arrays.asList(
                                Arrays.asList("B", "B", "C", "Y"),
                                Arrays.asList("F", "X", "Z"),
                                Arrays.asList("A", "Q", "S", "V", "W")
                        ),
                        ++testNo,
                        null
                },
                { // --- String 1-2 ---
                        (Comparator<String>) String::compareTo,
                        Arrays.asList("A", "B", "B", "C", "F", "Q", "S", "V", "W", "X", "Y", "Z"),
                        Arrays.asList(
                                Arrays.asList("F", "X", "Z"),
                                Arrays.asList("B", "B", "C", "Y"),
                                Arrays.asList("A", "Q", "S", "V", "W")
                        ),
                        ++testNo,
                        null
                },
                { // --- String 1-3 ---
                        (Comparator<String>) String::compareTo,
                        Arrays.asList("A", "B", "B", "C", "F", "Q", "S", "V", "W", "X", "Y", "Z"),
                        Arrays.asList(
                                Arrays.asList("A", "Q", "S", "V", "W"),
                                Arrays.asList("F", "X", "Z"),
                                Arrays.asList("B", "B", "C", "Y")
                        ),
                        ++testNo,
                        null
                },
                { // --- String 2-1 (One of inputs is empty) ---
                        (Comparator<String>) String::compareTo,
                        Arrays.asList("A", "B", "B", "C", "Q", "S", "V", "W", "Y"),
                        Arrays.asList(
                                Arrays.asList("B", "B", "C", "Y"),
                                Arrays.asList(),
                                Arrays.asList("A", "Q", "S", "V", "W")
                        ),
                        ++testNo,
                        null
                },
                { // --- String 3 (always ZERO comparator) ---
                        (Comparator<String>) (a, b) -> 0,
                        Arrays.asList("B", "B", "C", "Y", "F", "X", "Z", "A", "Q", "S", "V", "W"),
                        Arrays.asList(
                                Arrays.asList("B", "B", "C", "Y"),
                                Arrays.asList("F", "X", "Z"),
                                Arrays.asList("A", "Q", "S", "V", "W")
                        ),
                        ++testNo,
                        null
                },
//                { // --- String 4 (with Integer - ClassCastException should be thrown) ---
//                        (Comparator<String>) String::compareTo,
//                        Arrays.asList("A", "B", "B", "C", "F", "Q", "S", "V", "W", "X", "Y", "Z"),
//                        Arrays.asList(
//                                Arrays.asList("B", "B", "C", "Y"),
//                                Arrays.asList(0, 1, 2),
//                                Arrays.asList("A", "Q", "S", "V", "W")
//                        ),
//                        ++testNo,
//                        new ClassCastException()
//                },
                { // --- Empty inputs 1 (Integer comparator) ---
                        (Comparator<Integer>) (a, b) -> a - b,
                        Arrays.asList(),
                        Arrays.asList(
                                Arrays.asList()
                        ),
                        ++testNo,
                        null
                },
                { // --- Empty inputs 1 (String comparator) ---
                        (Comparator<String>) String::compareTo,
                        Arrays.asList(),
                        Arrays.asList(
                                Arrays.asList()
                        ),
                        ++testNo,
                        null
                },
                { // --- Empty inputs 1 (Object comparator) ---
                        (Comparator<Object>) (a, b) -> a.equals(b) ? 0 : (-1),
                        Arrays.asList(),
                        Arrays.asList(
                                Arrays.asList()
                        ),
                        ++testNo,
                        null
                }
        };
    }
}