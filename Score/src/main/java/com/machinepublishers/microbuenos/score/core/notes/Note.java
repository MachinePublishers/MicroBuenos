/*
 * Copyright 2009-2023 MicroBuenos committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.machinepublishers.microbuenos.score.core.notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Note {

  //TODO maybe this would come in handy for the UI.  but not really needed now
  private static final String[][] HUMAN_READABLE = new String[][]{{"A", "Bbb"},
      {"Bb", "Cbb", "A#"},
      {"B", "Cb", "A##"},
      {"C", "Dbb", "B#"},
      {"Db", "C#", "B##"},
      {"D", "Ebb", "C##"},
      {"Eb", "Fbb", "D#"},
      {"E", "Fb", "D##"},
      {"F", "Gbb", "E#"},
      {"Gb", "F#", "E##"},
      {"G", "Abb", "F##"},
      {"Ab", "G#"},
      {"A", "Bbb", "G##"},
      {"Bb", "Cbb", "A#"},
      {"B", "Cb", "A##"},
      {"C", "Dbb", "B#"},
      {"Db", "C#", "B##"},
      {"D", "Ebb", "C##"},
      {"Eb", "Fbb", "D#"},
      {"Eb", "Fbb", "D#"},
      {"E", "Fb", "D##"},
      {"F", "Gbb", "E#"},
      {"Gb", "F#", "E##"},
      {"G", "Abb", "F##"},
      {"Ab", "G#"},
      {"A", "Bbb", "G##"}};

  /**
   * A map of notes in the scale by their numbering. The key (of type String) is the note's name.
   * The value (of type Integer) is the note's position in the scale. This map would be useful in
   * chord creation.
   */
  private static final Map<String, Integer> FORMULA_NOTES;
  private static final Map<String, double[]> CHORD_NOTES;
  /**
   * A simple piano-roll style map of note-names to frequencies.
   */
  private static final Map<String, Double> PIANOROLL_NOTES = new LinkedHashMap<String, Double>();
  private static final double[] ALL_NOTES;

  static {
    Map<String, double[]> chordNotes;
    /*
     * NUMS
     * About two octaves worth of notes.  The first dimension <em>index</em> is the note number
     * in the scale.  The second dimension <em>value</em> is the number-name of the note.
     */
    final String[][] NUMS = new String[][]{
        {"1", "bb2"}, //note: do not change the ordering, CHORDS depends on this
        {"b2", "#1"}, {"2", "bb3", "##1"},
        {"b3", "bb4", "#2"}, {"3", "b4", "##2"},
        {"4", "bb5", "#3"}, {"b5", "#4", "##3"},
        {"5", "bb6", "##4"}, {"b6", "#5"},
        {"6", "bb7", "##5"}, {"b7", "bb8", "#6"},
        {"7", "b8", "##6"}, {"8", "bb9", "#7"},
        {"b9", "#8", "##7"}, {"9", "bb10", "##8"},
        {"b10", "bb11", "#9"}, {"10", "b11", "##9"},
        {"11", "bb12", "#10"}, {"b12", "#11", "##10"},
        {"12", "bb13", "##11"}, {"b13", "#12"},
        {"13", "bb14", "##12"}, {"b14", "bb15", "#13"},
        {"14", "b15", "##13"}, {"15", "bb16", "#14"}};

    FORMULA_NOTES = new HashMap<String, Integer>();
    for (int i = 0; i < NUMS.length; i++) {
      for (int j = 0; j < NUMS[i].length; j++) {
        FORMULA_NOTES.put(NUMS[i][j], new Integer(i));
      }
    }

    final String[][] CHORDS = new String[][]{
        {"maj", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString()},
        {"5", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("5").toString()},
        {"sus 4", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("4").toString(),
            FORMULA_NOTES.get("5").toString()},
        {"sus 2", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("2").toString(),
            FORMULA_NOTES.get("5").toString()},
        {"add 9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"6", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("6").toString()},
        {"6 add 9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("6").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"maj 7", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("7").toString()},
        {"maj 9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("7").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"maj 7#11", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("7").toString(),
            FORMULA_NOTES.get("#11").toString()},
        {"maj 13", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("7").toString(),
            FORMULA_NOTES.get("9").toString(),
            FORMULA_NOTES.get("13").toString()},
        {"min", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString()},
        {"min add 9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"min 6", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("6").toString()},
        {"min b6", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b6").toString()},
        {"min 6 / add 9",
            FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("6").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"min 7", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("6").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"min 7 b5", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("b5").toString(),
            FORMULA_NOTES.get("b7").toString()},
        {"min / maj 7",
            FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("7").toString()},
        {"min 9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"min 9 b5", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("b5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"min 9 / maj 7",
            FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("7").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"min 11", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString(),
            FORMULA_NOTES.get("11").toString()},
        {"min 13", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString(),
            FORMULA_NOTES.get("11").toString(),
            FORMULA_NOTES.get("13").toString()},
        {"dom 7", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString()},
        {"7 sus 4", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("4").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString()},
        {"7 b5", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("b5").toString(),
            FORMULA_NOTES.get("b7").toString()},
        {"9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"9 sus 4", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("4").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"9 b5", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("4").toString(),
            FORMULA_NOTES.get("b5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"7 b9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("b9").toString()},
        {"7 #9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("#9").toString()},
        {"7 b5 #9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("b5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("#9").toString()},
        {"11", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString(),
            FORMULA_NOTES.get("11").toString()},
        {"7 #11", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("#11").toString()},
        {"13", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString(),
            FORMULA_NOTES.get("13").toString()},
        {"13 sus 4", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("4").toString(),
            FORMULA_NOTES.get("5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString(),
            FORMULA_NOTES.get("13").toString()},
        {"aug", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("#5").toString()},
        {"+7", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("#5").toString(),
            FORMULA_NOTES.get("b7").toString()},
        {"+9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("#5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("9").toString()},
        {"7 +7b9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("#5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("b9").toString()},
        {"+7#9", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("3").toString(),
            FORMULA_NOTES.get("#5").toString(),
            FORMULA_NOTES.get("b7").toString(),
            FORMULA_NOTES.get("#9").toString()},
        {"o", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("b5").toString()},
        {"o7", FORMULA_NOTES.get("1").toString(),
            FORMULA_NOTES.get("b3").toString(),
            FORMULA_NOTES.get("b5").toString(),
            FORMULA_NOTES.get("bb7").toString()}};
    chordNotes = new HashMap<String, double[]>();
    for (int i = 0; i < CHORDS.length; i++) {
      double[] freqs = new double[CHORDS[i].length - 1];
      for (int j = 0; j < CHORDS[i].length - 1; j++) {
        freqs[j] = Double.parseDouble(CHORDS[i][j + 1]);
      }
      chordNotes.put(CHORDS[i][0], freqs);
    }
    CHORD_NOTES = Collections.unmodifiableMap(chordNotes);
  }

  static {
    final String[] NAMES_FLAT = new String[]{"A-", "Bb-", "B-", "C+", "Db+", "D+", "Eb+",
        "E+", "F+", "Gb+", "G+", "Ab+"};
    final String[] NAMES_SHARP = new String[]{"A-", "A#-", "B-", "C+", "C#+", "D+", "D#+",
        "E+", "F+", "F#+", "G+", "G#+"};
    final double A0 = 27.5; //low A
    final ArrayList<Double> allNotes = new ArrayList<Double>(
        NAMES_FLAT.length * Note.TwelveTET.instance()
            .size());
    for (int octave = 0; octave < 9; octave++) {
      for (int et = 0; et < 12; et++) {
        double value = Note.TwelveTET.instance().getFreq(et, A0) * Math.pow(2, octave);
        PIANOROLL_NOTES.put(NAMES_FLAT[et].replace('-', (octave + "").charAt(0))
                .replace('+', ((octave + 1) + "").charAt(0)),
            value);
        if (!PIANOROLL_NOTES.containsKey(NAMES_SHARP[et])) {
          PIANOROLL_NOTES.put(NAMES_SHARP[et].replace('-', (octave + "").charAt(0))
                  .replace('+', ((octave + 1) + "").charAt(0)),
              value);
        }
        allNotes.add(value);
      }
    }

    ALL_NOTES = new double[allNotes.size()];
    int i = 0;
    for (double d : allNotes) {
      ALL_NOTES[i++] = d;
    }
  }

  public void T(Tuning mt) {

  }

  public static interface Tuning {

    public double getFreq(String noteNumber);

    public double getFreq(int noteNumber, double root);

    public double[] getFreqs(String chord, double root);

    public int size();
  }

  public static class TwelveTET implements Tuning {

    public static final int UNISON = 0;
    public static final int MINOR_SECOND = 1;
    public static final int MAJOR_SECOND = 2;
    public static final int MINOR_THIRD = 3;
    public static final int MAJOR_THIRD = 4;
    public static final int PERFECT_FOURTH = 5;
    public static final int AUGMENTED_FOURTH = 6;
    public static final int PERFECT_FIFTH = 7;
    public static final int MINOR_SIXTH = 8;
    public static final int MAJOR_SIXTH = 9;
    public static final int MINOR_SEVENTH = 10;
    public static final int MAJOR_SEVENTH = 11;
    private static final double T0 = Math.pow(2, 0. / 12.); //1,bb2 -- root == A,Bbb
    private static final double T1 = Math.pow(2, 1. / 12.); //   b2,#1 == Bb,Cbb,A#
    private static final double T2 = Math.pow(2, 2. / 12.); //2,bb3,##1 -- second == B,Cb,A##
    private static final double T3 = Math.pow(2,
        3. / 12.); //   b3,bb4,#2 -- minor third == C,Dbb,B#
    private static final double T4 = Math.pow(2, 4. / 12.); //3,b4,##2 -- major third == Db,C#,B##
    private static final double T5 = Math.pow(2, 5. / 12.); //4,bb5,#3 -- fourth == D,Ebb,C##
    private static final double T6 = Math.pow(2, 6. / 12.); //   b5,#4,##3 == Eb,Fbb,D#
    private static final double T7 = Math.pow(2, 7. / 12.); //5,bb6,##4 -- fifth == E,Fb,D##
    private static final double T8 = Math.pow(2, 8. / 12.); //   b6,#5 -- minor sixth == F,Gbb,E#
    private static final double T9 = Math.pow(2, 9. / 12.); //6,bb7,##5 -- major sixth == Gb,F#,E##
    private static final double T10 = Math.pow(2,
        10. / 12.); //   b7,bb8,#6 -- minor seventh == G,Abb,F##
    private static final double T11 = Math.pow(2, 11. / 12.); //7,b8,##6 -- major seventh == Ab,G#
    private static final double T12 = Math.pow(2, 12. / 12.);
    private static final double T13 = Math.pow(2, 13. / 12.);
    private static final double T14 = Math.pow(2, 14. / 12.);
    private static final double T15 = Math.pow(2, 15. / 12.);
    private static final double T16 = Math.pow(2, 16. / 12.);
    private static final double T17 = Math.pow(2, 17. / 12.);
    private static final double T18 = Math.pow(2, 18. / 12.);
    private static final double T19 = Math.pow(2, 19. / 12.);
    private static final double T20 = Math.pow(2, 20. / 12.);
    private static final double[] ETS = new double[]{T0, T1, T2, T3, T4, T5, T6, T7, T8, T9,
        T10, T11, T12, T13, T14, T15, T16, T17,
        T18, T19, T20};
    private static final TwelveTET instance = new TwelveTET();

    private TwelveTET() {

    }

    public static TwelveTET instance() {
      return instance;
    }

    public double getFreq(String noteNumber) {
      return PIANOROLL_NOTES.get(noteNumber);
    }

    public double getFreq(int noteNumber, double octave) {
      return ETS[noteNumber] * octave;
    }

    public double[] getFreqs(String chord, double root) {
      double[] freqs = CHORD_NOTES.get(chord);
      double[] freqsOutput = new double[freqs.length];
      for (int i = 0; i < freqs.length; i++) {
        freqsOutput[i] = ETS[(int) freqs[i]] * root;//FIXME
      }
      return freqsOutput;
    }

    public int size() {
      return ETS.length;
    }
  }
}
