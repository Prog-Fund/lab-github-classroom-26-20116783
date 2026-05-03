package models;

import utils.BirdUtility;

public class Parrot extends Bird {

    private String vocabularySize = "Amazing"; // converted from int using BirdUtility

    public Parrot(String name, int age, Owner owner, int id,
                  double wingSpan, boolean canFly, int vocabularySize) {
        super(name, age, owner, id, wingSpan, canFly);
        String converted = BirdUtility.convertToVocabSize(vocabularySize);
        if (converted != null) this.vocabularySize = converted;
    }

    public String getVocabularySize() { return vocabularySize; }

    public void setVocabularySize(int numWords) {
        String converted = BirdUtility.convertToVocabSize(numWords);
        if (converted != null) this.vocabularySize = converted;
    }

    @Override
    public double calculateWeeklyFee() {
        return 10 * numOfDaysAttending();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parrot)) return false;
        return vocabularySize.equals(((Parrot) o).vocabularySize) && super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString() + " | Vocabulary: " + vocabularySize;
    }
}
