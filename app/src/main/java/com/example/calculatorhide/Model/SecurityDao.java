package com.example.calculatorhide.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SecurityDao {
    @Insert
    public void addData(Securityitem securityitem);

    @Query("select * from security")
    public List<Securityitem> getSecurity();

    @Query("select * from security WHERE password= :password")
    public Securityitem getpassword(String password);

    @Query("UPDATE security SET password=:password")
    void updatePassword(String password);


    @Query("select * from security WHERE question= :question")
    public Securityitem getquestion(String question);

    @Query("select * from security")
    public Securityitem getqueans();

    @Query("UPDATE security SET question=:question,answer=:answer")
    void updateQuestionAndAnswer(String question, String answer);
}
