package com.example.calculatorhide.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "security")
public class Securityitem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "answer")
    public String answer;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
