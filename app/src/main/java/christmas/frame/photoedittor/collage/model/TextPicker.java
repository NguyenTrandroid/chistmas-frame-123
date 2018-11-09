package christmas.frame.photoedittor.collage.model;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

public class TextPicker {
    private int textsize;
    private Typeface typeface;
    private int textcolor;
    private String text;
    private boolean bold=false;
    private boolean italic=false;
    private boolean underline=false;

    public TextPicker(int textsize, Typeface typeface, int textcolor, String text, boolean bold, boolean italic, boolean underline) {
        this.textsize = textsize;
        this.typeface = typeface;
        this.textcolor = textcolor;
        this.text = text;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;


    }
    public void setToTextView(TextView textView){
        textView.setText(text);
        textView.setTypeface(typeface);
        textView.setTextColor(textcolor);
        textView.setTextSize(textsize);
        if(bold){
            if(italic){
                textView.setTypeface(typeface,Typeface.BOLD_ITALIC);
            }else textView.setTypeface(typeface,Typeface.BOLD);
        }
        if(underline)textView.setPaintFlags(textView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }

    public int getTextsize() {
        return textsize;
    }

    public void setTextsize(int textsize) {
        this.textsize = textsize;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public int getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(int textcolor) {
        this.textcolor = textcolor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }
}
