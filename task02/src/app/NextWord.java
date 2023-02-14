package app;

public class NextWord {

    private String word;
    private int count;

    public NextWord(String word) {
        this(word, 1);
    }
    
    public NextWord(String word, int count){
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "NextWord [word=" + word + ", count=" + count + "]";
    }
}