package at.technikum_wien.cheers;

import java.io.Serializable;

/**
 * Created by DominikRossmanith on 12.01.2018.
 */

public class Instruction implements Serializable {

    private String text;
    private String category;
    private String type;
    private String id;
    private String text2;

    public Instruction(String tempText, String category, String type, String id){
        this.category = category;
        this.type = type;
        this.id = id;
        seperateText(tempText);
    }

    private void seperateText(String text){
        int index = text.indexOf("|");
        if(index!=-1){
            this.text = text.substring(0,index);
            this.text2 = text.substring(index+1,text.length());
        }else{
            this.text = text;
            this.text2 = "";
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        seperateText(text);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText2() {
        return text2;
    }
}
