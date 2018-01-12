package at.technikum_wien.cheers;

/**
 * Created by DominikRossmanith on 12.01.2018.
 */

public class Question {
    private String text;
    private String category;

    public Question(String text, String category){
        this.text = text;
        this.category = category;
    }

    public String getText(){
        return text;
    }

    public String getCategory(){
        return category;
    }
}
