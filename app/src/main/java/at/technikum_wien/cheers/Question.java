package at.technikum_wien.cheers;

/**
 * Created by DominikRossmanith on 12.01.2018.
 */

public class Question {
    private String text;
    private String category;
    private String text2;

    public Question(String text, String category){
        this.text = text;
        this.category = category;
    }

    public Question(String text, String text2, String category){
        this.text = text;
        this.text2 = text2;
        this.category = category;
    }

    public String getText(){
        return text;
    }

    public String getText2(){
        return text2;
    }

    public void setText2(String s){
        text2 = s;
    }

    public String getCategory(){
        return category;
    }
}
