package librarymanagement.admin;

import java.sql.Date;


public class returnBook {
    
    private final String studentNumber;
    private final String bookTitle;
    private final String author;
    private final String bookType;
    private final Date date;
    
    public returnBook(String studentNumber, String bookTitle, String author, String bookType, Date date){
        this.studentNumber = studentNumber;
        this.bookTitle = bookTitle;
        this.author = author;
        this.bookType = bookType;
        this.date = date;
    }
    
    public String getstudentNumber(){
        return studentNumber;
    }
    public String getbookTitle(){
        return bookTitle;
    }
    public String getauthor(){
        return author;
    }
    public String getbookType(){
        return bookType;
    }
    
    public Date getdate(){
        return date;
    }
    
}
